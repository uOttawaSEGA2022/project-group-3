package com.example.mealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ClientSubmitComplaint extends AppCompatActivity {

    DatabaseReference complaintDatabase;
    DatabaseReference accountDatabase;
    String complaintCookID;
    ArrayList<String> usernames = new ArrayList<>();
    ArrayList<String> cookIDs = new ArrayList<>();
    private Complaints newComplaint;
    FirebaseUser thisUser;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_submit_complaint);

        complaintDatabase = FirebaseDatabase.getInstance().getReference("complaints");
        accountDatabase = FirebaseDatabase.getInstance().getReference("accounts");
        thisUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = thisUser.getUid();

        accountDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    if ((postSnapshot.child("role").getValue().toString()).equals("Cook")) {
                        usernames.add(postSnapshot.child("username").getValue().toString());
                        cookIDs.add(postSnapshot.child("id").getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Button submitComplaintButton = findViewById(R.id.submitButton);
        submitComplaintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText complaintTitleElement = findViewById(R.id.complaintTitle);
                String complaintTitle = String.valueOf(complaintTitleElement.getText());

                EditText complaintDescriptionElement = findViewById(R.id.complaintDescription);
                String complaintDescription = String.valueOf(complaintDescriptionElement.getText());

                EditText complaintUsernameElement = findViewById(R.id.complaintUsername);
                String complaintUsername = String.valueOf(complaintUsernameElement.getText());

                int indexOfUsernameAndCookID = usernames.indexOf(complaintUsername);

                if (indexOfUsernameAndCookID == -1) {
                    displayToast("That cook does not exist!");
                } else {

                    complaintCookID = cookIDs.get(indexOfUsernameAndCookID);

                    newComplaint = new Complaints(complaintTitle, complaintDescription, complaintUsername, complaintCookID);

                    String key = complaintDatabase.push().getKey();
                    complaintDatabase.child(key).child("Title").setValue(complaintTitle);
                    complaintDatabase.child(key).child("Description").setValue(complaintDescription);
                    complaintDatabase.child(key).child("cookID").setValue(complaintCookID);
                    complaintDatabase.child(key).child("username").setValue(complaintUsername);

                    sendIntentToHome();

                }
            }
        });
    }

    public void displayToast(String message){
        Toast.makeText(ClientSubmitComplaint.this, message, Toast.LENGTH_SHORT).show();
    }

    public void goHome(View view){
        sendIntentToHome();
    }

    // send user to login
    private void sendIntentToHome(){
        Intent intent = new Intent(getApplicationContext(), WelcomeClientPage.class);
        startActivityForResult(intent, 0);
    }
}