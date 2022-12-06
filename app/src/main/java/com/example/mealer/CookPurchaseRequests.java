package com.example.mealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.content.Context;
import android.graphics.Color;
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

public class CookPurchaseRequests extends AppCompatActivity {

    // Instance variables
    FirebaseUser thisCook;
    DatabaseReference thisCooksPurchaseRequests, specificMeals, clientDB;
    private static Context cookReqs;
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
        clientDB = FirebaseDatabase.getInstance().getReference("accounts").child("clients");
        cookReqs = this;
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

                    String purchaseID = postSnapshot.getKey();

                    // create layout for each meal
                    LinearLayout requestLayout = new LinearLayout(cookReqs);
                    LinearLayout.LayoutParams mealLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    mealLayoutParams.setMargins(20, 0, 20, 20);

                    // create strings and add them to lists
                    String thisClientID = postSnapshot.child("ClientID").getValue().toString();
                    String thisTitle = postSnapshot.child("Title").getValue().toString();
                    title.add(thisTitle);
                    clientID.add(thisClientID);

                    mealTitle = new TextView(cookReqs);
                    RelativeLayout relativeLayoutForPurchase = new RelativeLayout(cookReqs);

                    // set layout params
                    params = new RelativeLayout.LayoutParams(1250, 125); //Width just has to be bigger than screen width size
                    params.leftMargin = 30;

                    // set custom settings
                    mealTitle.setTextSize(16);
                    mealTitle.setGravity(Gravity.CENTER_VERTICAL);
                    mealTitle.setText("Order: " + title.get(i) + "\nClient: " + clientID.get(i));
                    relativeLayoutForPurchase.addView(mealTitle, params);


                    Button approveButton = new Button(cookReqs);

                    // set text colour & background
                    approveButton.setTextColor(Color.parseColor("#ffffff"));
                    approveButton.setBackgroundResource(R.drawable.green_rounded_button_20dp);
                    approveButton.setText("Approve");

                    params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 125); //Width just has to be bigger than screen width size
                    params.leftMargin = 20;
                    params.topMargin = 155;
                    params.bottomMargin = 20;

                    // listener for approving
                    approveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // approve on client side
                            clientDB.child(thisClientID).child("Purchase Requests").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot postSnapshot1 : snapshot.getChildren()) {
                                        if (postSnapshot1.getKey().equals(purchaseID)) {
                                            clientDB.child(thisClientID).child("Purchase Requests").child(purchaseID).child("Status").setValue("Approved");
                                            displayToast("Approved Purchase");
                                            postSnapshot.getRef().removeValue();
                                            Intent intent = new Intent(getApplicationContext(), CookPurchaseRequests.class);
                                            startActivityForResult(intent, 0);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                        }
                    });

                    relativeLayoutForPurchase.addView(approveButton, params);

                    Button rejectButton = new Button(cookReqs);

                    // set text colour & background
                    rejectButton.setTextColor(Color.parseColor("#ffffff"));
                    rejectButton.setBackgroundResource(R.drawable.red_rounded_button_20dp);
                    rejectButton.setText("Reject");

                    params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 125); //Width just has to be bigger than screen width size
                    params.leftMargin = 750;
                    params.topMargin = 155;
                    params.bottomMargin = 20;

                    // listener for rejecting
                    rejectButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // reject on client side
                            clientDB.child(thisClientID).child("Purchase Requests").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot postSnapshot2 : snapshot.getChildren()) {
                                        if (postSnapshot2.getKey().equals(purchaseID)) {
                                            clientDB.child(thisClientID).child("Purchase Requests").child(purchaseID).child("Status").setValue("Rejected");
                                            displayToast("Rejected Purchase");
                                            postSnapshot.getRef().removeValue();
                                            Intent intent = new Intent(getApplicationContext(), CookPurchaseRequests.class);
                                            startActivityForResult(intent, 0);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    });

                    relativeLayoutForPurchase.addView(rejectButton, params);

                    requestLayout.addView(relativeLayoutForPurchase);
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

    public void displayToast(String message){
        Toast.makeText(CookPurchaseRequests.this, message, Toast.LENGTH_SHORT).show();
    }

    public void sendToWelcome(View view) {
        Intent intent = new Intent(getApplicationContext(), WelcomeCookPage.class);
        startActivityForResult(intent, 0);
    }

}