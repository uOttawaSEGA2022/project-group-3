package com.example.mealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MasterMenu extends AppCompatActivity {

    DatabaseReference masterMenu, allTheCooks;
    TextView textView;
    private static Context menu;
    LinearLayout innerLayout, mainLayout;
    Button purchaseButton;
    ArrayList<String> title, price, mealType, ingredients, description, cuisineType, cookID, allergens;
    String cookFirstName, cookLastName;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_menu);


        masterMenu = FirebaseDatabase.getInstance().getReference("master-menu");
        allTheCooks = FirebaseDatabase.getInstance().getReference("accounts").child("cooks");
        menu=this;

    }

    @Override
    protected void onStart() {

        super.onStart();

        mainLayout = findViewById(R.id.masterMenu);

        masterMenu.addValueEventListener(new ValueEventListener() {
            String isOffered;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()){
                    isOffered = postSnapshot.child("isOffered").getValue().toString();
                    if (isOffered.equals("true")){
                        title.add(postSnapshot.child("title").getValue().toString());
                        description.add(postSnapshot.child("description").getValue().toString());
                        price.add(postSnapshot.child("price").getValue().toString());
                        mealType.add(postSnapshot.child("mealType").getValue().toString());
                        ingredients.add(postSnapshot.child("ingredients").getValue().toString());
                        cuisineType.add(postSnapshot.child("cuisineType").getValue().toString());
                        cookID.add(postSnapshot.child("cookID").getValue().toString());
                        allergens.add(postSnapshot.child("allergens").getValue().toString());


                    }






                }

                for (int i = 0; i < title.size(); i++){

                   allTheCooks.child(cookID.get(i)).addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                           if (snapshot.exists()){
                               cookFirstName = snapshot.child("firstName").getValue().toString();
                               cookLastName = snapshot.child("lastName").getValue().toString();
                           }else{
                               Log.d("COOKERROR", "Cook does not exist");
                           }

                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {

                       }
                   });


                    innerLayout = new LinearLayout(menu);

                    textView = new TextView(menu);

                    textView.setText("Title: "+title.get(i)+'\n'+"Description: "+description.get(i)+'\n'+"Cook Name: "+cookFirstName+" "+cookLastName+'\n'+"Price: "+price.get(i)+'\n'+"Meal Type: "+mealType.get(i)+'\n'+"Ingredients: "+ingredients.get(i)+'\n'+"Cuisine Type: "+cuisineType.get(i)+'\n'+"Allergens: "+allergens.get(i));

                    innerLayout.addView(textView);

                    purchaseButton = new Button(menu);

                    purchaseButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //DO STUFF HERE FOR WHEN THE BUTTON IS CLICKED
                        }
                    });

                    innerLayout.addView(purchaseButton);

                    mainLayout.addView(innerLayout);









                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }


}