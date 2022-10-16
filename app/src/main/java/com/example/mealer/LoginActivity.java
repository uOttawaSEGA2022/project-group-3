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
    Button cookLoginButton, clientLoginButton, signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        cookLoginButton = findViewById(R.id.LoginCook);
        clientLoginButton = findViewById(R.id.LoginClient);
        signUpButton = findViewById(R.id.signUpButton);
        cookButtonListen();
        clientButtonListen();

    }

    private boolean hasLogin(String typeOfLogin,String username, String password){

        switch (typeOfLogin){
            case ("Cook"):
                if (username.equals("cook")&&password.equals("cook123")){
                    return true;
                }
                break;
            case ("Client"):
                if (username.equals("client")&&password.equals("client123")){
                    return true;
                }
                break;
            case ("Admin"):
                if (username.equals("admin")&&password.equals("admin123")) {
                    return true;
                }
                break;

            default:
                return false;

        }
        return false;
    }

    private void cookButtonListen(){
        cookLoginButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if (hasLogin("Cook",username.getText().toString(),password.getText().toString())){
                    returnToMain("Cook");
                } else{
                    if (hasLogin("Admin",username.getText().toString(),password.getText().toString())){
                        returnToMain("Admin");
                    }
                }
            }
        });
    }

    private void clientButtonListen(){
        clientLoginButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if (hasLogin("Client",username.getText().toString(),password.getText().toString())){
                    returnToMain("Client");
                } else{
                    if (hasLogin("Admin",username.getText().toString(),password.getText().toString())){
                        returnToMain("Admin");
                    }
                }

            }
        });
    }


    private void returnToMain (String typeOfLogin){
        Intent returnIntent = new Intent();
        returnIntent.putExtra(typeOfLogin,true);
        setResult(RESULT_OK,returnIntent);

        //finishing activity and return to main screen
        finish();
    }

}