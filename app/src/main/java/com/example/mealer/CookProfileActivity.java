package com.example.mealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CookProfileActivity extends AppCompatActivity {

    // Instance variables
    FirebaseUser user;
    DatabaseReference accountsDatabase;
    String userID;
    float ratingAverage;

    TextView nameText;
    TextView descriptionText;
    TextView addressText;
    TextView ratingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_profile);

        // set text views
        nameText = findViewById(R.id.name);
        descriptionText = findViewById(R.id.cookProfileDescriptionField);
        addressText = findViewById(R.id.address);
        ratingText = findViewById(R.id.rating);

        // variables for storing firebase info
        user = FirebaseAuth.getInstance().getCurrentUser();
        accountsDatabase = FirebaseDatabase.getInstance().getReference("accounts").getRef();
        userID = user.getUid();

        // init rating average;
        ratingAverage = 0;

        // Setting general information for cook
        accountsDatabase.child("cooks").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Cook userProfile = snapshot.getValue(Cook.class);

                if (userProfile != null) {
                    String name = userProfile.getFirstName() + " " + userProfile.getLastName();
                    String desc = userProfile.getDescription();
                    String addr = userProfile.getAddress();

                    nameText.setText("Name: " + name);
                    descriptionText.setText(desc);
                    addressText.setText("Address: " + addr);
                } else {
                    displayToast("Could not get user data.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                displayToast("Could not get user data.");
            }
        });

        // Checking ratings information
        accountsDatabase.child("cooks").child(userID).child("rating").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Float cooksRating = snapshot.getValue(Float.class);
                if (cooksRating != null && cooksRating > 0) {
                    String ratingString = "Rating: " + cooksRating + " stars";
                    ratingText.setText(ratingString);
                } else {
                    String ratingString = "Rating: N/A";
                    ratingText.setText(ratingString);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // method for displaying toast
    public void displayToast(String message){
        Toast.makeText(CookProfileActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    // method for sending to current cook's welcome page
    public void sendToWelcome(View view){
        Intent intent = new Intent(getApplicationContext(), WelcomeCookPage.class);
        startActivityForResult(intent,0);
    }
}