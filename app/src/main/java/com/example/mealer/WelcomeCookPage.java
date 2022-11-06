package com.example.mealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.time.Year;
import java.util.Date;
import java.util.Calendar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WelcomeCookPage extends AppCompatActivity {

    FirebaseUser user;
    DatabaseReference database;
    private String userID;
    private static Context viewPage;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_cook);

        viewPage = this;

        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference("accounts");
        userID = user.getUid();

        database.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String permBan = snapshot.child("permanentBan").getValue().toString();
                if (permBan.equals("true")){
                    setContentView(R.layout.activity_permanent_ban);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                displayToast("Something went wrong.");
            }
        });

        database.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String tempBan = snapshot.child("temporaryBan").getValue().toString();
                if (!tempBan.equals("null")){

                    String[] unbanDate = tempBan.split("-");

                    if (Integer.parseInt(unbanDate[0]) <= Calendar.getInstance().get(Calendar.YEAR)){

                        if (Integer.parseInt(unbanDate[1]) <= Calendar.getInstance().get(Calendar.MONTH)+1){

                            if (Integer.parseInt(unbanDate[2]) <= Calendar.getInstance().get(Calendar.DAY_OF_MONTH)){

                                database.child(userID).child("temporaryBan").setValue("null");
                            }
                            else{
                                textView = (TextView) findViewById(R.id.temporaryBanMessage);
                                textView.setText("YOU ARE TEMPORARILY BANNED! YOU WILL BE UNBANNED ON (Y/M/D): "+tempBan);
                                setContentView(R.layout.activity_temporary_ban);
                            }
                        }
                        else{
                            textView = (TextView) findViewById(R.id.temporaryBanMessage);
                            textView.setText("YOU ARE TEMPORARILY BANNED! YOU WILL BE UNBANNED ON (Y/M/D): "+tempBan);
                            setContentView(R.layout.activity_temporary_ban);
                        }
                    } else {
                        textView = (TextView) findViewById(R.id.temporaryBanMessage);
                        textView.setText("YOU ARE TEMPORARILY BANNED! YOU WILL BE UNBANNED ON (Y/M/D): "+tempBan);
                        setContentView(R.layout.activity_temporary_ban);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                displayToast("Something went wrong.");
            }
        });

        TextView welcomeTextView = (TextView) findViewById(R.id.welcomeMessage);

        database.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Client userProfile = snapshot.getValue(Client.class);

                if (userProfile != null) {
                    String name = userProfile.getFirstName();

                    welcomeTextView.setText("Welcome Client: " + name);
                } else {
                    welcomeTextView.setText("Welcome Client (null)");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                displayToast("Something went wrong.");
            }
        });
    }

    public void logOutOfApp(View view){
        FirebaseAuth.getInstance().signOut();
        sendIntentToLogin();
    }

    private void sendIntentToLogin(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivityForResult(intent, 0);
    }

    public void displayToast(String message){
        Toast.makeText(WelcomeCookPage.this, message, Toast.LENGTH_SHORT).show();
    }
}