package com.example.mealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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

public class ClientPurchaseStatusActivity extends AppCompatActivity {

    // Instance variables
    FirebaseUser thisClient;
    DatabaseReference thisClientPurchaseRequests;
    DatabaseReference specificMeals;
    private static Context requestsPage;
    private String clientID;
    TextView mealTitle;
    RelativeLayout.LayoutParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_purchase_status);

        // setting variables
        thisClient = FirebaseAuth.getInstance().getCurrentUser();
        clientID = thisClient.getUid();
        thisClientPurchaseRequests = FirebaseDatabase.getInstance().getReference("accounts").child("clients").child(clientID).child("Purchase Requests");
        requestsPage = this;
    }

    @Override
    protected void onStart() {

        super.onStart();

        // get reference to layout
        LinearLayout linearLayout = findViewById(R.id.listOfPurchases);

        // get values from firebase
        thisClientPurchaseRequests.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // create arraylists to create food items from
                ArrayList<String> title = new ArrayList<>();
                ArrayList<String> price = new ArrayList<>();
                int i = 0;

                // create a food item for each snapshot in db reference
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    // create layout for each meal
                    LinearLayout requestLayout = new LinearLayout(requestsPage);
                    LinearLayout.LayoutParams mealLayoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    mealLayoutParams.setMargins(20, 0, 20, 20);

                    // create strings from db
                    String thisPrice = postSnapshot.child("Price").getValue().toString();
                    String thisTitle = postSnapshot.child("Title").getValue().toString();
                    String thisStatus = postSnapshot.child("Status").getValue().toString();

                    title.add(thisTitle);
                    price.add(thisPrice);

                    mealTitle = new TextView(requestsPage);
                    RelativeLayout relativeLayoutForFoodTitle = new RelativeLayout(requestsPage);

                    // set layout params
                    params = new RelativeLayout.LayoutParams(500, 125); //Width just has to be bigger than screen width size
                    params.leftMargin = 30;
                    params.topMargin = 20;
                    params.bottomMargin = 20;

                    // set custom settings
                    mealTitle.setTextSize(20);
                    mealTitle.setGravity(Gravity.CENTER_VERTICAL);
                    mealTitle.setText(title.get(i) + " ($" + price.get(i) + ")");
                    relativeLayoutForFoodTitle.addView(mealTitle, params);
                    requestLayout.addView(relativeLayoutForFoodTitle);

                    TextView purchaseStatus = new TextView(requestsPage);
                    RelativeLayout relativeLayoutForPurchaseStatus = new RelativeLayout(requestsPage);

                    // set layout params
                    params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 125); //Width just has to be bigger than screen width size
                    params.leftMargin = 750;
                    params.topMargin = 20;
                    params.bottomMargin = 20;

                    // set custom settings
                    purchaseStatus.setTextSize(20);
                    purchaseStatus.setGravity(Gravity.CENTER);
                    purchaseStatus.setText(thisStatus); // set to DB status
                    purchaseStatus.setPadding(10, 10, 10, 10);
                    purchaseStatus.setTextColor(Color.parseColor("#ffffff"));

                    // Set background based on status
                    if (thisStatus.equals("Approved")) {
                        purchaseStatus.setBackgroundResource(R.drawable.green_rounded_button_20dp);
                    } else if (thisStatus.equals("Rejected")) {
                        purchaseStatus.setBackgroundResource(R.drawable.red_rounded_button_20dp);
                    } else {
                        purchaseStatus.setBackgroundResource(R.drawable.dark_grey_rounded_button_20dp);
                    }

                    purchaseStatus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (thisStatus.equals("Approved") || thisStatus.equals("Rejected")) {
                                postSnapshot.getRef().removeValue();
                                displayToast("Dismissed Purchase");
                                Intent intent = new Intent(getApplicationContext(), ClientPurchaseStatusActivity.class);
                                startActivityForResult(intent, 0);
                            }
                        }
                    });

                    relativeLayoutForFoodTitle.addView(purchaseStatus, params);
                    requestLayout.addView(relativeLayoutForPurchaseStatus);

                    requestLayout.setBackgroundResource(R.drawable.grey_rounded_backround_20dp);

                    linearLayout.addView(requestLayout, mealLayoutParams);

                    i++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void sendToWelcome(View view) {
        Intent intent = new Intent(getApplicationContext(), WelcomeClientPage.class);
        startActivityForResult(intent, 0);
    }

    public void displayToast(String message){
        Toast.makeText(ClientPurchaseStatusActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}