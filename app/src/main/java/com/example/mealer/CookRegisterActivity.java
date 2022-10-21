package com.example.mealer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class CookRegisterActivity extends AppCompatActivity {
    EditText cookFirstName, cookLastName, cookDescription, cookAddress;
    Cook currentAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_register);
        cookFirstName=findViewById(R.id.firstNameField);
        cookLastName=findViewById(R.id.lastNameField);
        cookDescription=findViewById(R.id.descriptionField);
        cookAddress=findViewById(R.id.addressField);
        currentAccount = (Cook) getIntent().getSerializableExtra("cookObj");
    }

    public boolean isFieldValid() {
        // SAM SAM SAM SAM
        return true;
    }

    public void registerAsCook(View view){
        if (isFieldValid()) {
            currentAccount.setFirstName(cookFirstName.getText().toString());
            currentAccount.setLastName(cookLastName.getText().toString());
            currentAccount.setDescription(cookDescription.getText().toString());
            currentAccount.setAddress(cookAddress.getText().toString());

            sendToLogin(view);
        }
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