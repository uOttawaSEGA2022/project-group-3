package com.example.mealer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ClientRegisterActivity extends AppCompatActivity {
    EditText clientFirstName, clientLastName, clientAddress, clientCreditCardNumber;
    Client currentAccount;
    Button buttonRegister;

    DatabaseReference database;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_register);
        clientFirstName=findViewById(R.id.firstNameField);
        clientLastName=findViewById(R.id.lastNameField);
        clientCreditCardNumber=findViewById(R.id.creditCardNumberField);
        clientAddress=findViewById(R.id.addressField);
        buttonRegister = findViewById(R.id.registerButton);
        currentAccount = (Client) getIntent().getSerializableExtra("clientObj");

        //Initialize db with accounts section
        database = FirebaseDatabase.getInstance().getReference("accounts");

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

    }

    public boolean isFieldValid(){

        String firstNameInput = clientFirstName.getText().toString();
        String lastNameInput = clientLastName.getText().toString();
        String creditCardInput = clientCreditCardNumber.getText().toString();
        String addressInput = clientAddress.getText().toString();

        //CHECK IF A FIELD IS EMPTY
        if ((firstNameInput.equals(""))||(lastNameInput.equals(""))||(creditCardInput.equals(""))||(addressInput.equals(""))){
            displayToast("No empty fields");
            return false;
        }



        //FIRST NAME CHECKS
        if (firstNameInput.length() > 30) {
            displayToast("First name too long.");
            return false;
        }
        for(char c : firstNameInput.toCharArray()){
            if (!isAlphaNumeric(c)) {
                displayToast("First name contains illegal characters.");
                return false;
            }
        }
        if (Character.isWhitespace(firstNameInput.charAt(0))){
            displayToast("First name cannot start with whitespace");
            return false;
        }



        //LAST NAME CHECKS
        if (lastNameInput.length() > 30) {
            displayToast("Last name too long.");
            return false;
        }
        for(char c : lastNameInput.toCharArray()){
            if (!isAlphaNumeric(c)) {
                displayToast("Last name contains illegal characters.");
                return false;
            }
        }
        if (Character.isWhitespace(lastNameInput.charAt(0))){
            displayToast("Last name cannot start with whitespace");
            return false;
        }


        //CREDIT CARD CHECKS
        if (creditCardInput.length() != 16) {
            displayToast("Credit card number must be 16 digits.");
            return false;
        }

        //ADDRESS CHECKS
        if (addressInput.length() > 100) {
            displayToast("Address too long.");
            return false;
        }
        if (Character.isWhitespace(addressInput.charAt(0))){
            displayToast("Address cannot start with whitespace");
            return false;
        }

        return true;
    }

    public static boolean isAlphaNumeric(char char1) {
        return (char1 >= 'a' && char1 <= 'z') || (char1 >= 'A' && char1 <= 'Z') || (char1 == ' ');
    }

    public static boolean isNumeric(char char1) {
        return (char1 >= '1' && char1 <= 'z') || (char1 >= 'A' && char1 <= 'Z') || (char1 == ' ');
    }

    public void displayToast(String message){
        Toast.makeText(ClientRegisterActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    public void registerAsClient(View view){
        if (isFieldValid()) {
            currentAccount.setFirstName(clientFirstName.getText().toString());
            currentAccount.setLastName(clientLastName.getText().toString());
            currentAccount.setAddress(clientAddress.getText().toString());
            currentAccount.setCreditCardNumber(clientCreditCardNumber.getText().toString());
            currentAccount.setRole("Client");

            addAccountToDB();

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

    private void addAccountToDB() {
        currentAccount.setId(mAuth.getCurrentUser().getUid());

        //Add cook to database
        database.child(currentAccount.getId()).setValue(currentAccount);
    }
}