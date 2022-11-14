package com.example.mealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CookPersonalMenuActivity extends AppCompatActivity {
    FirebaseUser thisCook;
    DatabaseReference thisCooksMenus;
    DatabaseReference specificMeals;
    private static Context personalMenu;
    private String cookID;
    TextView generatedText;
    RelativeLayout.LayoutParams params;
    Button deleteMeal;
    Button setOffered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_personal_menu);
        thisCook = FirebaseAuth.getInstance().getCurrentUser();
        cookID = thisCook.getUid();
        thisCooksMenus = FirebaseDatabase.getInstance().getReference("accounts").child(cookID).child("Cook Menu");
        specificMeals = FirebaseDatabase.getInstance().getReference("master-menu");
        personalMenu = this;
    }

    @Override
    protected void onStart() {

        super.onStart();

        LinearLayout linearLayout = findViewById(R.id.linearLayout);

        thisCooksMenus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ArrayList<String> title = new ArrayList<>();
                ArrayList<String> description = new ArrayList<>();
                ArrayList<String> ingredients = new ArrayList<>();
                ArrayList<String> areOffered = new ArrayList<>();
                int i = 0;

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    LinearLayout linearLayoutNew = new LinearLayout(personalMenu);

                    String thisDescription = postSnapshot.child("description").getValue().toString();
                    String thisTitle = postSnapshot.child("title").getValue().toString();
                    String thisIngredients = postSnapshot.child("ingredients").getValue().toString();

                    description.add(thisDescription);
                    title.add(thisTitle);
                    ingredients.add(thisIngredients);
                    areOffered.add(postSnapshot.child("isOffered").getValue().toString());

                    generatedText = new TextView(personalMenu);
                    RelativeLayout relativeLayoutForText = new RelativeLayout(personalMenu);
                    params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT); //Width just has to be bigger than screen width size
                    params.topMargin = 25;
                    params.bottomMargin = 25;
                    generatedText.setText("Title: " + title.get(i) +'\n'+"Description: " + description.get(i) +'\n'+"Ingredients: " + ingredients.get(i) +'\n');

                    relativeLayoutForText.addView(generatedText, params);

                    setOffered = new Button(personalMenu);
                    String areOfferedValue = areOffered.get(i);
                    String oppositeAreOffered;
                    if (areOfferedValue.equals("true")){
                        setOffered.setBackgroundColor(Color.RED);
                        setOffered.setText("Un-Offer");
                        oppositeAreOffered = "false";
                    }else{
                        setOffered.setBackgroundColor(Color.GREEN);
                        setOffered.setText("Set Offer");
                        oppositeAreOffered = "true";
                    }

                    RelativeLayout relativeLayout = new RelativeLayout(personalMenu);
                    params = new RelativeLayout.LayoutParams(250, 200);
                    params.leftMargin = 100;
                    params.rightMargin = 75;
                    params.topMargin = 75;
                    params.bottomMargin = 50;
                    relativeLayout.addView(setOffered, params);

                    deleteMeal = new Button(personalMenu);
                    deleteMeal.setText("DELETE");
                    deleteMeal.setBackgroundColor(Color.RED);
                    RelativeLayout relativeLayoutNew = new RelativeLayout(personalMenu);
                    params = new RelativeLayout.LayoutParams(250, 200);
                    params.rightMargin = 100;
                    params.topMargin = 75;
                    relativeLayoutNew.addView(deleteMeal, params);

                    linearLayoutNew.addView(relativeLayout);
                    linearLayoutNew.addView(relativeLayoutNew);
                    String currentTitle = title.get(i);
                    deleteMeal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            specificMeals.child(currentTitle).removeValue();
                            thisCooksMenus.child(currentTitle).removeValue();
                            displayToast("Meal deleted!");
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }
                    });

                    setOffered.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boolean offered;
                            if (oppositeAreOffered.equals("true")){
                                offered = true;
                            }else{
                                offered = false;
                            }
                            specificMeals.child(currentTitle).setValue(new MenuItem(thisTitle,thisDescription,thisIngredients,cookID,offered));
                            thisCooksMenus.child(postSnapshot.child("title").getValue().toString()).setValue(new MenuItem(thisTitle,thisDescription,thisIngredients,offered));
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }
                    });

                    linearLayout.addView(linearLayoutNew);
                    linearLayoutNew.addView(relativeLayoutForText);
                    i++;
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });


    }

    public void displayToast(String message){
        Toast.makeText(personalMenu, message, Toast.LENGTH_SHORT).show();
    }

    public void sendToAddMeal(View view) {
        Intent intent = new Intent(getApplicationContext(), AddMenuItem.class);
        startActivityForResult(intent, 0);
    }
}