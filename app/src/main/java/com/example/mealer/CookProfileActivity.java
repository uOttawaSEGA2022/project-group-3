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

    TextView nameText;
    TextView descriptionText;
    TextView addressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_profile);

        // set text views
        nameText = findViewById(R.id.name);
        descriptionText = findViewById(R.id.cookProfileDescriptionField);
        addressText = findViewById(R.id.address);

        // variables for storing firebase info
        user = FirebaseAuth.getInstance().getCurrentUser();
        accountsDatabase = FirebaseDatabase.getInstance().getReference("accounts").getRef();
        userID = user.getUid();

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
    }

    // method for displaying toast
    public void displayToast(String message){
        Toast.makeText(CookProfileActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    // method for sending to current cook's ratings page
    public void sendToRatings(View view){
        Intent intent = new Intent(getApplicationContext(), WelcomeCookPage.class);
        startActivityForResult(intent,0);
    }

    // method for sending to current cook's welcome page
    public void sendToWelcome(View view){
        Intent intent = new Intent(getApplicationContext(), WelcomeCookPage.class);
        startActivityForResult(intent,0);
    }
}