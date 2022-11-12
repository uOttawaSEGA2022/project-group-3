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
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    private static Context personalMenu;
    private String cookID;
    TextView generateText;
    Button setOffered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_personal_menu);
        thisCook = FirebaseAuth.getInstance().getCurrentUser();
        cookID = thisCook.getUid();
        thisCooksMenus = FirebaseDatabase.getInstance().getReference("accounts").child(cookID).child("Cook Menu");
        personalMenu = this;
    }

    @Override
    protected void onStart() {

        super.onStart();

        RelativeLayout relativeLayout = findViewById(R.id.cookPersonalMenu);

        thisCooksMenus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ArrayList<String> title = new ArrayList<>();
                ArrayList<String> description = new ArrayList<>();
                ArrayList<String> ingredients = new ArrayList<>();
                ArrayList<String> areOffered = new ArrayList<>();
                int i = 0;

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    String thisDescription = postSnapshot.child("description").getValue().toString();
                    String thisTitle = postSnapshot.child("title").getValue().toString();
                    String thisIngredients = postSnapshot.child("ingredients").getValue().toString();

                    description.add(thisDescription);
                    title.add(thisTitle);
                    ingredients.add(thisIngredients);
                    areOffered.add(postSnapshot.child("isOffered").getValue().toString());

                    generateText = new TextView(personalMenu);
                    generateText.setText("Title: " + title.get(i) + '\n' + "Description: " + description.get(i) + '\n' + "Ingredients: " + ingredients.get(i));
                    relativeLayout.addView(generateText);
                    setOffered = new Button(personalMenu);
                    String areOfferedValue = areOffered.get(i);
                    String oppositeAreOffered;
                    if (areOfferedValue.equals("true")){
                        setOffered.setBackgroundColor(Color.RED);
                        setOffered.setText("Cancel offer");
                        oppositeAreOffered = "false";
                    }else{
                        setOffered.setBackgroundColor(Color.GREEN);
                        setOffered.setText("Set offer");
                        oppositeAreOffered = "true";
                    }
                    relativeLayout.addView(setOffered);
                    setOffered.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boolean offered;
                            if (oppositeAreOffered.equals("true")){
                                offered=true;
                            }else{
                                offered=false;
                            }
                            thisCooksMenus.child(postSnapshot.child("title").getValue().toString()).setValue(new MenuItem(thisTitle,thisDescription,thisIngredients,offered));
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }
                    });
                    i++;
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });


    }

    public void sendToAddMeal(View view) {
        Intent intent = new Intent(getApplicationContext(), AddMenuItem.class);
        startActivityForResult(intent, 0);
    }
}