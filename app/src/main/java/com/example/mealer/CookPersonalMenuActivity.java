package com.example.mealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

    // Instance variables
    FirebaseUser thisCook;
    DatabaseReference thisCooksMenus;
    DatabaseReference specificMeals;
    private static Context personalMenu;
    private String cookID;
    TextView mealTitle;
    RelativeLayout.LayoutParams params;
    Button deleteMeal;
    Button setOffered;

    /*
     * OnCreate method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_personal_menu);

        // setting variables
        thisCook = FirebaseAuth.getInstance().getCurrentUser();
        cookID = thisCook.getUid();
        thisCooksMenus = FirebaseDatabase.getInstance().getReference("accounts").child("cooks").child(cookID).child("Cook Menu");
        specificMeals = FirebaseDatabase.getInstance().getReference("master-menu");
        personalMenu = this;
    }

    @Override
    protected void onStart() {

        super.onStart();

        // get reference to layout
        LinearLayout linearLayout = findViewById(R.id.linearLayout);

        // get values from firebase
        thisCooksMenus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // create arraylists to create food items from
                ArrayList<String> title = new ArrayList<>();
                ArrayList<String> description = new ArrayList<>();
                ArrayList<String> ingredients = new ArrayList<>();
                ArrayList<String> areOffered = new ArrayList<>();
                int i = 0;

                // create a food item for each snapshot in db reference
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    /* Set up */

                    // create layout for each meal
                    LinearLayout mealLayout = new LinearLayout(personalMenu);
                    LinearLayout.LayoutParams mealLayoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    mealLayoutParams.setMargins(20, 0, 20, 20);

                    // create strings and add them to lists
                    String thisDescription = postSnapshot.child("description").getValue().toString();
                    String thisTitle = postSnapshot.child("title").getValue().toString();
                    String thisIngredients = postSnapshot.child("ingredients").getValue().toString();
                    String thisAllergens = postSnapshot.child("allergens").getValue().toString();
                    String thisMealType = postSnapshot.child("mealType").getValue().toString();
                    String thisCuisineType = postSnapshot.child("cuisineType").getValue().toString();
                    String thisPrice = postSnapshot.child("price").getValue().toString();

                    description.add(thisDescription);
                    title.add(thisTitle);
                    ingredients.add(thisIngredients);
                    areOffered.add(postSnapshot.child("isOffered").getValue().toString());

                    /* Food Title Text */

                    mealTitle = new TextView(personalMenu);
                    RelativeLayout relativeLayoutForFoodTitle = new RelativeLayout(personalMenu);

                    // set layout params
                    params = new RelativeLayout.LayoutParams(450, 120); //Width just has to be bigger than screen width size
                    params.leftMargin = 30;

                    // set custom settings
                    mealTitle.setTextSize(18);
                    mealTitle.setGravity(Gravity.CENTER_VERTICAL);
                    mealTitle.setText(title.get(i));
                    mealTitle.setVerticalScrollBarEnabled(true);
                    mealTitle.setMovementMethod(new ScrollingMovementMethod());

                    // add meal to layout, add layout to main layout
                    relativeLayoutForFoodTitle.addView(mealTitle, params);
                    mealLayout.addView(relativeLayoutForFoodTitle);

                    /* Offer/Un-offer button */

                    setOffered = new Button(personalMenu);

                    // set text colour & background
                    setOffered.setTextColor(Color.parseColor("#ffffff"));
                    setOffered.setBackgroundResource(R.drawable.green_rounded_button_20dp);

                    String areOfferedValue = areOffered.get(i);
                    String oppositeAreOffered;
                    if (areOfferedValue.equals("true")){
                        setOffered.setBackgroundResource(R.drawable.red_rounded_button_20dp);
                        setOffered.setText("Un-Offer");
                        oppositeAreOffered = "false";
                    }else{
                        setOffered.setBackgroundResource(R.drawable.green_rounded_button_20dp);
                        setOffered.setText("Set Offer");
                        oppositeAreOffered = "true";
                    }

                    RelativeLayout relativeLayout = new RelativeLayout(personalMenu);
                    params = new RelativeLayout.LayoutParams(250, 150);
                    params.leftMargin = 50;
                    params.rightMargin = 25;
                    params.topMargin = 25;
                    params.bottomMargin = 25;
                    relativeLayout.addView(setOffered, params);

                    /* Delete button */

                    deleteMeal = new Button(personalMenu);
                    deleteMeal.setTextColor(Color.parseColor("#ffffff"));
                    deleteMeal.setText("DELETE");
                    deleteMeal.setBackgroundResource(R.drawable.red_rounded_button_20dp);
                    RelativeLayout relativeLayoutNew = new RelativeLayout(personalMenu);
                    params = new RelativeLayout.LayoutParams(250, 150);
                    params.rightMargin = 25;
                    params.topMargin = 25;
                    params.bottomMargin = 25;
                    relativeLayoutNew.addView(deleteMeal, params);

                    mealLayout.addView(relativeLayout);
                    mealLayout.addView(relativeLayoutNew);
                    mealLayout.setBackgroundResource(R.drawable.grey_rounded_backround_20dp);

                    String currentTitle = title.get(i);

                    /* Listeners */

                    // listener for selecting a meal
                    mealTitle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            // create popup
                            showMealPopup(view, thisTitle, thisDescription, thisIngredients, thisAllergens, thisMealType, thisCuisineType, thisPrice);
                        }
                    });

                    // listener for deleting meal
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

                    // listener for setting offered/unoffering
                    setOffered.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boolean offered;
                            if (oppositeAreOffered.equals("true")){
                                offered = true;
                            }else{
                                offered = false;
                            }
                            specificMeals.child(currentTitle).setValue(new MenuItem(thisTitle,thisDescription,thisIngredients,thisMealType,thisCuisineType,thisAllergens,thisPrice,thisCook.getUid(),offered));
                            thisCooksMenus.child(postSnapshot.child("title").getValue().toString()).setValue(new MenuItem(thisTitle,thisDescription,thisIngredients,thisMealType,thisCuisineType,thisAllergens,thisPrice,thisCook.getUid(),offered));
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }
                    });

                    linearLayout.addView(mealLayout, mealLayoutParams);

                    i++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void showMealPopup(View view, String name, String desc, String ingredients, String allergensVal, String mealTypeVal, String cuisineTypeVal, String priceVal) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_cook_meal_info, null);

        TextView mealTitle = popupView.findViewById(R.id.mealTitlePlaceholder);
        TextView mealDescription = popupView.findViewById(R.id.mealDescriptionPlaceholder);
        TextView mealIngredients = popupView.findViewById(R.id.mealIngredientsPlaceholder);
        TextView mealType = popupView.findViewById(R.id.mealTypePlaceholder);
        TextView cuisineType = popupView.findViewById(R.id.cuisineTypePlaceholder);
        TextView mealAllergens = popupView.findViewById(R.id.mealAllergensPlaceholder);
        TextView mealPrice = popupView.findViewById(R.id.mealPricePlaceholder);

        mealTitle.setText(name);
        mealType.setText(mealTypeVal);
        cuisineType.setText(cuisineTypeVal);
        mealDescription.setText(desc);
        mealIngredients.setText(ingredients);
        mealAllergens.setText(allergensVal);
        mealPrice.setText(priceVal);


        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    public void highlightMeal(LinearLayout mealLayout) {
        mealLayout.setBackgroundResource(R.drawable.light_green_rounded_background);
    }

    public void unHighlightMeal(LinearLayout mealLayout) {
        mealLayout.setBackgroundResource(R.drawable.grey_rounded_backround_20dp);
    }

    // display toast
    public void displayToast(String message){
        Toast.makeText(personalMenu, message, Toast.LENGTH_SHORT).show();
    }

    // send user to activity for adding a meal
    public void sendToAddMeal(View view) {
        Intent intent = new Intent(getApplicationContext(), AddMenuItem.class);
        startActivityForResult(intent, 0);
    }

    public void sendToHome(View view) {
        Intent intent = new Intent(getApplicationContext(), WelcomeCookPage.class);
        startActivityForResult(intent, 0);
    }
}