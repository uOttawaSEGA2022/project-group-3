package com.example.mealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    EditText searchField;
    private static Context menu;
    LinearLayout innerLayout, mainLayout,searchLayout;
    Button purchaseButton;
    ArrayList<String> title, price, mealType, ingredients, description, cuisineType, cookID, allergens,cookFirstName, cookLastName;





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

        title = new ArrayList<>();
        description = new ArrayList<>();
        price = new ArrayList<>();
        mealType = new ArrayList<>();
        ingredients = new ArrayList<>();
        cuisineType = new ArrayList<>();
        cookID = new ArrayList<>();
        allergens = new ArrayList<>();
        cookFirstName = new ArrayList<>();
        cookLastName = new ArrayList<>();

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
                               cookFirstName.add(snapshot.child("firstName").getValue().toString());
                               cookLastName.add(snapshot.child("lastName").getValue().toString());
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

                    textView.setText("Title: "+title.get(i)+'\n'+"Description: "+description.get(i)+'\n'+"Cook Name: "+cookFirstName.get(i)+" "+cookLastName.get(i)+'\n'+"Price: "+price.get(i)+'\n'+"Meal Type: "+mealType.get(i)+'\n'+"Ingredients: "+ingredients.get(i)+'\n'+"Cuisine Type: "+cuisineType.get(i)+'\n'+"Allergens: "+allergens.get(i));

                    innerLayout.addView(textView);

                    purchaseButton = new Button(menu); //button not aligned or styled

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

    public void searchForMeal (){
        searchField = findViewById(R.id.searchField);
        String searchVals = searchField.getText().toString();
        if (searchVals.contains(",")){
           String [] theSearchVals = searchVals.split(",");
           searchLayout = new LinearLayout(menu);
           textView = new TextView(menu);
           for (int i = 0; i<theSearchVals.length;i++){
               int finalI = i;
               masterMenu.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot postSnapshot: snapshot.getChildren()){
                            if (postSnapshot.child("title").getValue().toString().equals(theSearchVals[finalI])||postSnapshot.child("mealType").getValue().toString().equals(theSearchVals[finalI])||postSnapshot.child("cuisineType").getValue().toString().equals(theSearchVals[finalI])){
                                int whereItIs;
                                try{
                                    whereItIs = title.indexOf(postSnapshot.child("title").getValue().toString());
                                }catch (Exception e){
                                    try{
                                        whereItIs = mealType.indexOf(postSnapshot.child("mealType").getValue().toString());
                                    }catch (Exception u){
                                        try{
                                            whereItIs = cuisineType.indexOf(postSnapshot.child("cuisineType").getValue().toString());
                                        }catch(Exception a){
                                            displayToast("No such item found");
                                            continue;
                                        }

                                    }

                                }
                                textView = new TextView(menu);
                                textView.setText("Title: "+title.get(whereItIs)+'\n'+"Description: "+description.get(whereItIs)+'\n'+"Cook Name: "+cookFirstName.get(whereItIs)+" "+cookLastName.get(whereItIs)+'\n'+"Price: "+price.get(whereItIs)+'\n'+"Meal Type: "+mealType.get(whereItIs)+'\n'+"Ingredients: "+ingredients.get(whereItIs)+'\n'+"Cuisine Type: "+cuisineType.get(whereItIs)+'\n'+"Allergens: "+allergens.get(whereItIs));
                            }
                            searchLayout.addView(textView);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

           }

            mainLayout.removeView(innerLayout);
            mainLayout.addView(searchLayout);

        }else if (!searchVals.isEmpty()){
            masterMenu.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot postSnapshot: snapshot.getChildren()){
                        if (postSnapshot.child("title").getValue().toString().equals(searchVals)||postSnapshot.child("mealType").getValue().toString().equals(searchVals)||postSnapshot.child("cuisineType").getValue().toString().equals(searchVals)){
                            int whereItIs;
                            try{
                                whereItIs = title.indexOf(postSnapshot.child("title").getValue().toString());
                            }catch (Exception e){
                                try{
                                    whereItIs = mealType.indexOf(postSnapshot.child("mealType").getValue().toString());
                                }catch (Exception u){
                                    try{
                                        whereItIs = cuisineType.indexOf(postSnapshot.child("cuisineType").getValue().toString());
                                    }catch (Exception a){
                                        displayToast("No such item found");
                                        continue;
                                    }

                                }

                            }
                            textView = new TextView(menu);
                            textView.setText("Title: "+title.get(whereItIs)+'\n'+"Description: "+description.get(whereItIs)+'\n'+"Cook Name: "+cookFirstName.get(whereItIs)+" "+cookLastName.get(whereItIs)+'\n'+"Price: "+price.get(whereItIs)+'\n'+"Meal Type: "+mealType.get(whereItIs)+'\n'+"Ingredients: "+ingredients.get(whereItIs)+'\n'+"Cuisine Type: "+cuisineType.get(whereItIs)+'\n'+"Allergens: "+allergens.get(whereItIs));
                        }
                        searchLayout.addView(textView);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            mainLayout.removeView(innerLayout);
            mainLayout.addView(searchLayout);
        }

    }

    public void displayToast(String message){
        Toast.makeText(MasterMenu.this, message, Toast.LENGTH_SHORT).show();
    }


}