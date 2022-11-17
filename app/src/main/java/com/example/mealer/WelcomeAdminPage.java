package com.example.mealer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class WelcomeAdminPage extends AppCompatActivity {

    /*
     * OnCreate method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_admin);
    }

    // method for logging out and returning to login page
    public void logOutOfApp(View view){
        FirebaseAuth.getInstance().signOut();
        sendIntentToLogin();
    }

    // method for sending to complaint inbox
    public void sendIntentToComplaintInbox(View view){
        Intent intent = new Intent(getApplicationContext(), AdminInboxActivity.class);
        startActivityForResult(intent, 0);
    }

    // method for sending user to login
    private void sendIntentToLogin(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivityForResult(intent, 0);
    }


}