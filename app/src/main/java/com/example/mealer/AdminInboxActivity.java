package com.example.mealer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AdminInboxActivity extends AppCompatActivity {

    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_inbox);

        database = FirebaseDatabase.getInstance().getReference("complaints");

        addTestComplaint();

    }

    public void addTestComplaint(){
        database.child("complaint1").setValue("wahhh cook bad");
    }




}