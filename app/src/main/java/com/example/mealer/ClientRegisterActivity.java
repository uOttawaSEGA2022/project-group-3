package com.example.mealer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ClientRegisterActivity extends AppCompatActivity {
    EditText clientFirstName, clientLastName, clientAddress, clientCreditCardNumber;
    Client currentAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_register);
        clientFirstName=findViewById(R.id.firstNameField);
        clientLastName=findViewById(R.id.lastNameField);
        clientCreditCardNumber=findViewById(R.id.creditCardNumberField);
        clientAddress=findViewById(R.id.addressField);
        currentAccount = (Client) getIntent().getSerializableExtra("clientObj");
    }

    public boolean isFieldValid() {
        // SAM SAM SAM SAM
        return true;
    }

    public void registerAsCook(View view){
        if (isFieldValid()) {
            currentAccount.setFirstName(clientFirstName.getText().toString());
            currentAccount.setLastName(clientLastName.getText().toString());
            currentAccount.setAddress(clientAddress.getText().toString());
            currentAccount.setCreditCardNumber(clientCreditCardNumber.getText().toString());
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