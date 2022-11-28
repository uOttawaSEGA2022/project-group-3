package com.example.mealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
    ArrayList<String> title, price, mealType, ingredients, description, cuisineType, cookID, allergens;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_master_menu);

        //init database references and instance variables
        masterMenu = FirebaseDatabase.getInstance().getReference("master-menu");

        allTheCooks = FirebaseDatabase.getInstance().getReference("accounts").child("cooks");

        menu=this;

        mainLayout = findViewById(R.id.masterMenu);

    }

    @Override
    protected void onStart() {

        super.onStart();
        //init arraylists that will store the string values
        title = new ArrayList<>();

        description = new ArrayList<>();

        price = new ArrayList<>();

        mealType = new ArrayList<>();

        ingredients = new ArrayList<>();

        cuisineType = new ArrayList<>();

        cookID = new ArrayList<>();

        allergens = new ArrayList<>();

        mainLayout = findViewById(R.id.masterMenu);

        masterMenu.addValueEventListener(new ValueEventListener() {

            String isOffered;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()){
                    //checks only the meals which are offered
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
                    //makes preliminary menu
                    innerLayout = makeMenu(i);
                    mainLayout.addView(innerLayout);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    public void searchForMeal (View view){
        //have not searched yet
        boolean hasSearched = false;

        searchField = findViewById(R.id.searchField);

        searchLayout = new LinearLayout(menu);
        //gets rid of whitespace
        String searchVals = searchField.getText().toString().replaceAll("\\s","");
        //holds already searched values, so if two criteria (ex. meal type and meal name) are the same, it will only show that one result
        ArrayList<String> alreadySearchedVals = new ArrayList<>();
        //checks for comma which seperates criteria
        if (searchVals.contains(",")){

           String [] theSearchVals = searchVals.split(",");

           textView = new TextView(menu);

           for (int i = 0; i<theSearchVals.length;i++){
               int finalI = i;
               masterMenu.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot postSnapshot: snapshot.getChildren()){

                            String titleVal = postSnapshot.child("title").getValue().toString();

                            String mealTypeVal = postSnapshot.child("mealType").getValue().toString();

                            String cuisineTypeVal = postSnapshot.child("cuisineType").getValue().toString();
                            //checks and searches database
                            if (titleVal.equals(theSearchVals[finalI])||mealTypeVal.equals(theSearchVals[finalI])||cuisineTypeVal.equals(theSearchVals[finalI])){

                                if (!alreadySearchedVals.contains(titleVal)&&!alreadySearchedVals.contains(mealTypeVal)&&!alreadySearchedVals.contains(cuisineTypeVal)){

                                    int whereItIs;

                                    try{

                                        whereItIs = title.indexOf(titleVal); //finds where this meal is in the instance arraylists

                                        alreadySearchedVals.add(titleVal); //stores it here in case a duplicate criteria is called
                                    }catch (Exception e){

                                        try{
                                            whereItIs = mealType.indexOf(mealTypeVal);

                                            alreadySearchedVals.add(mealTypeVal);
                                        }catch (Exception u){

                                            try{

                                                whereItIs = cuisineType.indexOf(cuisineTypeVal);

                                                alreadySearchedVals.add(cuisineTypeVal);

                                            }catch(Exception a){

                                                displayToast("No such item found");

                                                continue;

                                            }

                                        }

                                    }
                                    //makes the layout for that meal
                                    searchLayout.addView(makeMenu(whereItIs));

                                }


                            }


                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


           }

        hasSearched=true; //you have searched, so it is set to true
        alreadySearchedVals.clear(); //clears the arraylist after searching

        }else if (!searchVals.isEmpty()){

            masterMenu.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot postSnapshot: snapshot.getChildren()){

                        String titleVal = postSnapshot.child("title").getValue().toString();

                        String mealTypeVal = postSnapshot.child("mealType").getValue().toString();

                        String cuisineTypeVal = postSnapshot.child("cuisineType").getValue().toString();

                        //same idea except we don't check for duplicate values since there is only one criteria to check
                        if (titleVal.equals(searchVals)||mealTypeVal.equals(searchVals)||cuisineTypeVal.equals(searchVals)){

                            int whereItIs;

                            try{

                                whereItIs = title.indexOf(titleVal);

                            }catch (Exception e){

                                try{

                                    whereItIs = mealType.indexOf(mealTypeVal);

                                }catch (Exception u){

                                    try{

                                        whereItIs = cuisineType.indexOf(cuisineTypeVal);

                                    }catch (Exception a){

                                        displayToast("No such item found");

                                        continue;

                                    }

                                }

                            }

                            searchLayout.addView(makeMenu(whereItIs));

                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            hasSearched = true;

        }else{
            //handles blank search bar edittext field, clears layout and shows all available meals
            mainLayout.removeAllViewsInLayout();

            mainLayout.invalidate();

            for (int i = 0; i < title.size(); i++){

                innerLayout = makeMenu(i);

                mainLayout.addView(innerLayout);

            }

        }

        if (hasSearched){
            //if you have searched, clear the menu and only show the searched meals
            mainLayout.removeAllViewsInLayout();

            mainLayout.invalidate();

            mainLayout.addView(searchLayout);


        }



    }

    public void displayToast(String message){
        Toast.makeText(MasterMenu.this, message, Toast.LENGTH_SHORT).show();
    }

    private LinearLayout makeMenu(int index){

        //makes a menu item and returns its linearlayout
        innerLayout = new LinearLayout(menu);

        textView = new TextView(menu);

        textView.setText("Title: "+title.get(index)+'\n'+"Description: "+description.get(index)+'\n'+"Price: "+price.get(index)+'\n'+"Meal Type: "+mealType.get(index)+'\n'+"Ingredients: "+ingredients.get(index)+'\n'+"Cuisine Type: "+cuisineType.get(index)+'\n'+"Allergens: "+allergens.get(index));

        innerLayout.addView(textView);

        purchaseButton = new Button(menu); //button not aligned or styled

        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //DO STUFF HERE FOR WHEN THE BUTTON IS CLICKED
            }
        });

        innerLayout.addView(purchaseButton);

        return innerLayout;

    }


}