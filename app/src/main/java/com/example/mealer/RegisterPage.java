package com.example.mealer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class RegisterPage extends AppCompatActivity {
TextView registerEmail,registerPassword;
private static ArrayList<String> clientUsernames = new ArrayList<>();
private static ArrayList<String> clientPasswords = new ArrayList<>();
private static ArrayList<String> cookUsernames = new ArrayList<>();
private static ArrayList<String> cookPasswords = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);

    }

    public void registerAsCook(View view){
        cookUsernames.add(registerEmail.getText().toString());
        cookPasswords.add(registerPassword.getText().toString());
        sendToLogin(view);
    }

    public void registerAsClient(View view){
        clientUsernames.add(registerEmail.getText().toString());
        clientPasswords.add(registerPassword.getText().toString());
        sendToLogin(view);
    }

    public void alreadyHasAnAccount(View view){
        sendToLogin(view);
    }

    protected static boolean getClientUsername(String thisUser){
        return clientUsernames.contains(thisUser);
    }

    protected static boolean getClientPassword(String thisUserPassword){
        return clientPasswords.contains(thisUserPassword);
    }

    protected static boolean getCookUsername (String thisUser){
        return cookUsernames.contains(thisUser);
    }

    protected static boolean getCookPassword (String thisCookPassword){
        return cookPasswords.contains(thisCookPassword);
    }

    private void sendIntentToLogin(){
        Intent intent = new Intent (getApplicationContext(), LoginActivity.class);
        startActivityForResult(intent,0);
    }

    private void sendToLogin(View view){
        Intent returnIntent = new Intent();
        setResult(RESULT_OK,returnIntent);
        sendIntentToLogin();
        //finishing activity send to register screen
        finish();
    }





}