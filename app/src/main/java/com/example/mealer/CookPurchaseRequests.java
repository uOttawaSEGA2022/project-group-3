package com.example.mealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.os.Bundle;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class CookPurchaseRequests extends AppCompatActivity {

    // Instance variables
    FirebaseUser thisCook;
    DatabaseReference thisCooksPurchaseRequests;
    DatabaseReference specificMeals;
    private static Context personalMenu;
    private String cookID;
    TextView mealTitle;
    RelativeLayout.LayoutParams params;
    Button deleteMeal;
    Button setOffered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_purchase_requests);

        // setting variables
        thisCook = FirebaseAuth.getInstance().getCurrentUser();
        cookID = thisCook.getUid();
        thisCooksPurchaseRequests = FirebaseDatabase.getInstance().getReference("accounts").child("cooks").child(cookID).child("Purchase Requests");
        personalMenu = this;
    }

    @Override
    protected void onStart() {

        super.onStart();

        // get reference to layout
        LinearLayout linearLayout = findViewById(R.id.linearLayout);

        // get values from firebase
        thisCooksPurchaseRequests.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // create arraylists to create food items from
                ArrayList<String> title = new ArrayList<>();
                ArrayList<String> clientID = new ArrayList<>();
                int i = 0;

                // create a food item for each snapshot in db reference
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    // create layout for each meal
                    LinearLayout requestLayout = new LinearLayout(personalMenu);
                    LinearLayout.LayoutParams mealLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    mealLayoutParams.setMargins(20, 0, 20, 20);

                    // create strings and add them to lists
                    String thisClientID = postSnapshot.child("ClientID").getValue().toString();
                    String thisTitle = postSnapshot.child("Title").getValue().toString();
                    title.add(thisTitle);
                    clientID.add(thisClientID);

                    mealTitle = new TextView(personalMenu);
                    RelativeLayout relativeLayoutForFoodTitle = new RelativeLayout(personalMenu);

                    // set layout params
                    params = new RelativeLayout.LayoutParams(1250, 225); //Width just has to be bigger than screen width size
                    params.leftMargin = 30;

                    // set custom settings
                    mealTitle.setTextSize(16);
                    mealTitle.setGravity(Gravity.CENTER_VERTICAL);
                    mealTitle.setText("Title: "+ title.get(i) + " \nClientID: " + clientID.get(i));
//                    mealTitle.setVerticalScrollBarEnabled(true);
//                    mealTitle.setMovementMethod(new ScrollingMovementMethod());

                    // add meal to layout, add layout to main layout
                    relativeLayoutForFoodTitle.addView(mealTitle, params);
                    requestLayout.addView(relativeLayoutForFoodTitle);
//
//                    RelativeLayout relativeLayout = new RelativeLayout(personalMenu);
//                    params = new RelativeLayout.LayoutParams(250, 150);
//                    params.leftMargin = 50;
//                    params.rightMargin = 25;
//                    params.topMargin = 25;
//                    params.bottomMargin = 25;
//                    relativeLayout.addView(setOffered, params);

                    linearLayout.addView(requestLayout, mealLayoutParams);

                    i++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}