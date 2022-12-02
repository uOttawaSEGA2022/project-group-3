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

public class WelcomeClientPage extends AppCompatActivity {

    // Instance variables

    FirebaseUser user;
    DatabaseReference database;
    String userID;

    /*
     * OnCreate method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_client);

        // variables for storing firebase info
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference("accounts").child("clients");
        userID = user.getUid();

        // get welcome text view
        TextView welcomeTextView = (TextView) findViewById(R.id.welcomeMessage);

        // adding a listener to the database object with current user id to get values
        database.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Client userProfile = snapshot.getValue(Client.class);

                if (userProfile != null) {
                    String name = userProfile.getFirstName();

                    welcomeTextView.setText(getString(R.string.welcome_username, name));
                } else {
                    welcomeTextView.setText(getString(R.string.welcome_client_default));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                displayToast("Something went wrong.");
            }
        });
    }

    // method for logging out
    public void logOutOfApp(View view){
        FirebaseAuth.getInstance().signOut();
        sendIntentToLogin();
    }

    // send user to login
    private void sendIntentToLogin(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivityForResult(intent, 0);
    }

    public void submitComplaint(View view){
        sendIntentToComplain();
    }

    // send user to login
    private void sendIntentToComplain(){
        Intent intent = new Intent(getApplicationContext(), ClientSubmitComplaint.class);
        startActivityForResult(intent, 0);
    }

    public void sendToMenu(View view){
        Intent intent = new Intent(getApplicationContext(), MasterMenu.class);
        startActivityForResult(intent, 0);
    }

    public void sendToRatings(View view){
        Intent intent = new Intent(getApplicationContext(), MasterMenu.class);
        startActivityForResult(intent, 0);
    }

    // method for displaying toast
    public void displayToast(String message){
        Toast.makeText(WelcomeClientPage.this, message, Toast.LENGTH_SHORT).show();
    }
}