package com.example.mealer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import java.util.ArrayList;

public class CookRegisterActivity extends AppCompatActivity {
    EditText cookFirstName, cookLastName, cookDescription, cookAddress;
    ArrayList<String> cookFirstNames = new ArrayList<>();
    ArrayList<String> cookLastNames = new ArrayList<>();
    ArrayList<String> cookDescriptions = new ArrayList<>();
    ArrayList<String> cookAddresses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_register);
        cookFirstName=findViewById(R.id.firstNameField);
        cookLastName=findViewById(R.id.lastNameField);
        cookDescription=findViewById(R.id.descriptionField);
        cookAddress=findViewById(R.id.addressField);
    }


}