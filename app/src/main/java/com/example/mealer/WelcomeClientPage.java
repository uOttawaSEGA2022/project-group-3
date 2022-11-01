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

    FirebaseUser user;
    DatabaseReference database;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_client);

        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference("accounts");
        userID = user.getUid();

        TextView welcomeTextView = (TextView) findViewById(R.id.welcomeMessage);

        database.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Client userProfile = snapshot.getValue(Client.class);

                if (userProfile != null) {
                    String name = userProfile.getFirstName();

                    welcomeTextView.setText("Welcome Client: " + name);
                } else {
                    welcomeTextView.setText("Welcome Client (null)");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                displayToast("Something went wrong.");
            }
        });
    }

    public void logOutOfApp(View view){
        FirebaseAuth.getInstance().signOut();
        sendIntentToLogin();
    }

    private void sendIntentToLogin(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivityForResult(intent, 0);
    }

    public void displayToast(String message){
        Toast.makeText(WelcomeClientPage.this, message, Toast.LENGTH_SHORT).show();
    }
}