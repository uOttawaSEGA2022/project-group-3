package com.example.mealer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class CookRegisterActivity extends AppCompatActivity {
    private static final int GET_FROM_GALLERY = 3;
    EditText cookFirstName, cookLastName, cookDescription, cookAddress;
    ArrayList<String> cookFirstNames = new ArrayList<>();
    ArrayList<String> cookLastNames = new ArrayList<>();
    ArrayList<String> cookDescriptions = new ArrayList<>();
    ArrayList<String> cookAddresses = new ArrayList<>();

    Uri selectedImage = null;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
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
}