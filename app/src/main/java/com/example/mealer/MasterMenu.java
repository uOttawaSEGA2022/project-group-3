package com.example.mealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MasterMenu extends AppCompatActivity {

    DatabaseReference masterMenu, allTheCooks, specificCookPurchaseRequests, clientPurchaseRequests;
    TextView mealInfoTextView;
    EditText searchField, clientUsername;
    private static Context menu;
    LinearLayout innerLayout, mainLayout,searchLayout;
    Button purchaseButton;
    ArrayList<String> title, price, mealType, ingredients, description, cuisineType, cookID, allergens;
    ArrayList<ArrayList<String>> purchaseRequests = new ArrayList<>();
    ArrayList<Integer> searchedMeals;
    private boolean thisCookBanned;
    boolean alreadyRequested;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_master_menu);

        //init database references and instance variables
        masterMenu = FirebaseDatabase.getInstance().getReference("master-menu");

        allTheCooks = FirebaseDatabase.getInstance().getReference("accounts").child("cooks");

        clientPurchaseRequests = FirebaseDatabase.getInstance().getReference("accounts").child("clients").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Purchase Requests");

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

        searchedMeals = new ArrayList<>();

        mainLayout = findViewById(R.id.masterMenu);

        masterMenu.addValueEventListener(new ValueEventListener() {

            String isOffered;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()){
                    //checks only the meals which are offered
                    isOffered = postSnapshot.child("isOffered").getValue().toString();
                    if (isOffered.equals("true")){
                        isCookSuspended(postSnapshot.child("cookID").getValue().toString());
                        if (!thisCookBanned){
                            cookID.add(postSnapshot.child("cookID").getValue().toString());
                            title.add(postSnapshot.child("title").getValue().toString());
                            description.add(postSnapshot.child("description").getValue().toString());
                            price.add(postSnapshot.child("price").getValue().toString());
                            mealType.add(postSnapshot.child("mealType").getValue().toString());
                            ingredients.add(postSnapshot.child("ingredients").getValue().toString());
                            cuisineType.add(postSnapshot.child("cuisineType").getValue().toString());
                            allergens.add(postSnapshot.child("allergens").getValue().toString());
                        }
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

        // reset searched meals
        searchedMeals = new ArrayList<>();

        searchField = findViewById(R.id.searchField);

        searchLayout = new LinearLayout(menu);
        //gets rid of whitespace
        String searchVals = searchField.getText().toString().replaceAll("\\s","");
        //holds already searched values, so if two criteria (ex. meal type and meal name) are the same, it will only show that one result
        ArrayList<String> alreadySearchedVals = new ArrayList<>();
        //checks for comma which seperates criteria
        if (searchVals.contains(",")){

           String [] theSearchVals = searchVals.split(",");
           mealInfoTextView = new TextView(menu);

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
                            if (titleVal.contains(theSearchVals[finalI])||mealTypeVal.contains(theSearchVals[finalI])||cuisineTypeVal.contains(theSearchVals[finalI])){

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
                                    if (whereItIs >= 0) {
                                        // add whereIsIs to arraylist of searched meals
                                        searchedMeals.add(whereItIs);
                                    }
                                }
                            }
                        }
                        getResults();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
           }

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
                        if (titleVal.contains(searchVals)||mealTypeVal.contains(searchVals)||cuisineTypeVal.contains(searchVals)){

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
                            if (whereItIs >= 0) {
                                // add whereIsIs to arraylist of searched meals
                                searchedMeals.add(whereItIs);
                            }
                        }
                    }
                    getResults();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }else{
            //handles blank search bar edittext field, clears layout and shows all available meals
            mainLayout.removeAllViewsInLayout();
            mainLayout.invalidate();
            for (int i = 0; i < title.size(); i++){
                innerLayout = makeMenu(i);
                mainLayout.addView(innerLayout);
            }

        }

    }

    private void getResults() {
        mainLayout.removeAllViewsInLayout();
        mainLayout.invalidate();
        for (int i = 0; i < searchedMeals.size(); i++){
            searchLayout = makeMenu(searchedMeals.get(i));
            mainLayout.addView(searchLayout);
        }
    }

    public void displayToast(String message){
        Toast.makeText(MasterMenu.this, message, Toast.LENGTH_SHORT).show();
    }

    private LinearLayout makeMenu(int index){

        //makes a menu item and returns its linearlayout
        innerLayout = new LinearLayout(menu);

        mealInfoTextView = new TextView(menu);

        mealInfoTextView.setText("Title: "+title.get(index)+'\n'+"Description: "+description.get(index)+'\n'+"Price: "+price.get(index)+'\n'+"Meal Type: "+mealType.get(index)+'\n'+"Ingredients: "+ingredients.get(index)+'\n'+"Cuisine Type: "+cuisineType.get(index)+'\n'+"Allergens: "+allergens.get(index));

        innerLayout.addView(mealInfoTextView);

        purchaseButton = new Button(menu); //button not aligned or styled
        purchaseButton.setText("Request Purchase");

        specificCookPurchaseRequests = allTheCooks.child(cookID.get(index)).child("Purchase Requests");

        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                requestMeal(index);


            }
        });

        innerLayout.addView(purchaseButton);

        return innerLayout;

    }

    private void requestMeal(int index) {
        ArrayList<String> purchaseRequestClientIsTryingToRequest = new ArrayList<String>();
        purchaseRequestClientIsTryingToRequest.add(allergens.get(index));
        purchaseRequestClientIsTryingToRequest.add(FirebaseAuth.getInstance().getCurrentUser().getUid());
        purchaseRequestClientIsTryingToRequest.add(cuisineType.get(index));
        purchaseRequestClientIsTryingToRequest.add(description.get(index));
        purchaseRequestClientIsTryingToRequest.add(ingredients.get(index));
        purchaseRequestClientIsTryingToRequest.add(mealType.get(index));
        purchaseRequestClientIsTryingToRequest.add(price.get(index));
        purchaseRequestClientIsTryingToRequest.add(title.get(index));

        for (int counter = 0; counter < purchaseRequests.size(); counter++){
            if ((purchaseRequests.get(counter)).equals(purchaseRequestClientIsTryingToRequest)) {
                displayToast("Meal already requested!");
                return;
            }
        }

        // add purchase request to cook side
        String key1 = specificCookPurchaseRequests.push().getKey();
        specificCookPurchaseRequests.child(key1).child("ClientID").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
        specificCookPurchaseRequests.child(key1).child("Title").setValue(title.get(index));
        specificCookPurchaseRequests.child(key1).child("Description").setValue(description.get(index));
        specificCookPurchaseRequests.child(key1).child("Price").setValue(price.get(index));
        specificCookPurchaseRequests.child(key1).child("Meal Type").setValue(mealType.get(index));
        specificCookPurchaseRequests.child(key1).child("Ingredients").setValue(ingredients.get(index));
        specificCookPurchaseRequests.child(key1).child("Cuisine Type").setValue(cuisineType.get(index));
        specificCookPurchaseRequests.child(key1).child("Allergens").setValue(allergens.get(index));
        specificCookPurchaseRequests.child(key1).child("Status").setValue("Pending");

        // add purchase request to client's side
        String key2 = clientPurchaseRequests.push().getKey();
        clientPurchaseRequests.child(key2).child("CookID").setValue(cookID.get(index));
        clientPurchaseRequests.child(key2).child("Title").setValue(title.get(index));
        clientPurchaseRequests.child(key2).child("Description").setValue(description.get(index));
        clientPurchaseRequests.child(key2).child("Price").setValue(price.get(index));
        clientPurchaseRequests.child(key2).child("Meal Type").setValue(mealType.get(index));
        clientPurchaseRequests.child(key2).child("Ingredients").setValue(ingredients.get(index));
        clientPurchaseRequests.child(key2).child("Cuisine Type").setValue(cuisineType.get(index));
        clientPurchaseRequests.child(key2).child("Allergens").setValue(allergens.get(index));
        clientPurchaseRequests.child(key2).child("Status").setValue("Pending");

        displayToast("Meal requested!");
    }

    private void isCookSuspended (String id){

        setThisCookBanned(false);

        allTheCooks.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String tempBan = snapshot.child(id).child("temporaryBan").getValue().toString();
                String permBan = snapshot.child(id).child("permanentBan").getValue().toString();
                if (tempBan.equals("true")||permBan.equals("true")){

                    setThisCookBanned(true);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setThisCookBanned(boolean b){
        thisCookBanned = b;
    }

    public void sendToHome(View view) {
        Intent intent = new Intent(getApplicationContext(), WelcomeClientPage.class);
        startActivityForResult(intent, 0);
    }


}