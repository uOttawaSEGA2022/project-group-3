package com.example.mealer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileNotFoundException;
import java.io.IOException;

public class CookRegisterActivity extends AppCompatActivity {
    private static final int GET_FROM_GALLERY = 3;
    EditText cookFirstName, cookLastName, cookDescription, cookAddress;
    Cook currentAccount;
    Button buttonRegister;

    DatabaseReference database;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_register);
        cookFirstName=findViewById(R.id.firstNameField);
        cookLastName=findViewById(R.id.lastNameField);
        cookDescription=findViewById(R.id.descriptionField);
        cookAddress = findViewById(R.id.addressField);
        buttonRegister = findViewById(R.id.registerButton);
        currentAccount = (Cook) getIntent().getSerializableExtra("cookObj");

        //Initialize db with accounts section
        database = FirebaseDatabase.getInstance().getReference("accounts");

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                currentAccount.setVoidCheque(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public boolean isFieldValid(){

        String firstNameInput = cookFirstName.getText().toString();
        String lastNameInput = cookLastName.getText().toString();
        String descriptionInput = cookDescription.getText().toString();
        String addressInput = cookAddress.getText().toString();

        //CHECK IF A FIELD IS EMPTY
        if ((firstNameInput.equals(""))||(lastNameInput.equals(""))||(descriptionInput.equals(""))||(addressInput.equals(""))){
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



        //DESCRIPTION CHECKS
        if (descriptionInput.length() > 100) {
            displayToast("Description too long.");
            return false;
        }
        if (Character.isWhitespace(descriptionInput.charAt(0))){
            displayToast("Description cannot start with whitespace");
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

    public void displayToast(String message){
        Toast.makeText(CookRegisterActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    public void registerAsCook(View view){
        if (isFieldValid()) {
            currentAccount.setFirstName(cookFirstName.getText().toString());
            currentAccount.setLastName(cookLastName.getText().toString());
            currentAccount.setDescription(cookDescription.getText().toString());
            currentAccount.setAddress(cookAddress.getText().toString());
            currentAccount.setRole("Cook");

            addAccountToDB();

            sendToLogin(view);
        }
    }

    public void uploadChequeImage(View view) {
        startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
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