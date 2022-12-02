package com.example.mealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ClientRatingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    FirebaseUser thisClient;
    DatabaseReference cooks;
    Spinner ratingSpinner;
    String rating;

    private static Context ratingsPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_rating);

        // setting variables
        thisClient = FirebaseAuth.getInstance().getCurrentUser();
        cooks = FirebaseDatabase.getInstance().getReference("accounts").child("cooks");
        ratingsPage = this;
        ratingSpinner = findViewById(R.id.ratingSelect);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.cookRatings, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ratingSpinner.setAdapter(adapter);
        ratingSpinner.setOnItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        cooks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // create a food item for each snapshot in db reference
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        rating = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // do nothing
    }

    // method for sending to current cook's profile
    public void sendToWelcome(View view){
        Intent intent = new Intent(getApplicationContext(), WelcomeClientPage.class);
        startActivityForResult(intent,0);
    }

    public void displayToast(String message){
        Toast.makeText(ClientRatingActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}