package com.example.mealer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    TextView username;
    TextView password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //sets all textviews and buttons
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);

    }

    public void cookButtonPress(View view){
                //checks for correct username and password
                if (hasLogin("Cook",username.getText().toString(),password.getText().toString())){
                    returnToMain("Cook");
                } else{
                    if (hasLogin("Admin",username.getText().toString(),password.getText().toString())){
                        returnToMain("Admin");
                    }

                }

    }

    public void clientButtonPress(View view){
            //checks for correct username and password
                if (hasLogin("Client",username.getText().toString(),password.getText().toString())){
                    returnToMain("Client");
                } else{
                    if (hasLogin("Admin",username.getText().toString(),password.getText().toString())){
                        returnToMain("Admin");
                    }
                }

    }

    //this method should return to the main activity where the app will actually happen, not working as of Oct 16 2022
    private void returnToMain (String typeOfLogin){
        Intent returnIntent = new Intent();
        returnIntent.putExtra(typeOfLogin,true);
        setResult(RESULT_OK,returnIntent);
        sendIntentToMain();
        //finishing activity and return to main screen
        finish();
    }
    public void registerButtonPress(View view){
        Intent returnIntent = new Intent();
        setResult(RESULT_OK,returnIntent);
        sendIntentToRegister();
        //finishing activity send to register screen
        finish();
    }
    private void sendIntentToRegister(){
        Intent intent = new Intent (getApplicationContext(), RegisterPage.class);
        startActivityForResult(intent,0);
    }

    private void sendIntentToMain(){
        //Application Context and Activity
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(intent, 0);
    }

    private boolean hasLogin(String typeOfLogin, String username, String password){
        //checks if they are trying to login and verifies
        switch (typeOfLogin){
            case ("Cook"):
                if ((username.equals("cook")&&password.equals("cook123"))||(RegisterPage.getCookUsername(username)&&RegisterPage.getCookPassword(password))) {
                    return true;
                }
                else{
                    return false;
                }
            case ("Client"):
                if ((username.equals("client")&&password.equals("client123"))||(RegisterPage.getClientUsername(username)&&RegisterPage.getClientPassword(password))){
                    return true;
                }
                else{
                    return false;
                }
            case ("Admin"):
                if (username.equals("admin")&&password.equals("admin123")) {
                    return true;
                }
                else{
                    displayToast("Wrong password or username");
                    return false;
                }
            default:
                return false;
        }
    }

    private void displayToast(String message){
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

}