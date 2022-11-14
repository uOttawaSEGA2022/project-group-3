package com.example.mealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WelcomeCookPage extends AppCompatActivity {

    // Instance variables
    FirebaseUser user;
    DatabaseReference database;
    private String userID;
    TextView welcomeText;

    /*
     * OnCreate method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_cook);

        // get welcome text view
        welcomeText=findViewById(R.id.welcomeMessage);

        // variables for storing firebase info
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference("accounts");
        userID = user.getUid();

        // adding a listener to the database object with current user id to get values
        database.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // check if cook is permanently banned
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
                // check for temporary ban
                String tempBan = snapshot.child("temporaryBan").getValue().toString();

                if (!tempBan.equals("null")){

                    String[] unbanDate = tempBan.split("-");


                    if (Integer.parseInt(unbanDate[0]) <= Calendar.getInstance().get(Calendar.YEAR)){
                        if (Integer.parseInt(unbanDate[1]) <= Calendar.getInstance().get(Calendar.MONTH)+1){
                            if (Integer.parseInt(unbanDate[2]) <= Calendar.getInstance().get(Calendar.DAY_OF_MONTH)){
                                database.child(userID).child("temporaryBan").setValue("null");
                                setContentView(R.layout.activity_welcome_cook);
                            } else{
                                setContentView(R.layout.activity_temporary_ban);
                                TextView textView = findViewById(R.id.temporaryBanMessage);
                                textView.setText(getString(R.string.temp_ban_message, tempBan));
                            }
                        } else {
                            setContentView(R.layout.activity_temporary_ban);
                            TextView textView = findViewById(R.id.temporaryBanMessage);
                            textView.setText(getString(R.string.temp_ban_message, tempBan));
                        }
                    } else {
                        setContentView(R.layout.activity_temporary_ban);
                        TextView textView = findViewById(R.id.temporaryBanMessage);
                        textView.setText(getString(R.string.temp_ban_message, tempBan));
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

                    welcomeTextView.setText(getString(R.string.welcome_username, name));
                } else {
                    welcomeTextView.setText(getString(R.string.welcome_cook_default));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                displayToast("Something went wrong.");
            }
        });
    }

    // method for logging
    public void logOutOfApp(View view){
        FirebaseAuth.getInstance().signOut();
        sendIntentToLogin();
    }

    // send user to login
    private void sendIntentToLogin(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivityForResult(intent, 0);
    }

    // method for displaying toast
    public void displayToast(String message){
        Toast.makeText(WelcomeCookPage.this, message, Toast.LENGTH_SHORT).show();
    }

    // method for sending to current cook's personal menu
    public void sendToPersonalMenu(View view){
        Intent intent = new Intent(getApplicationContext(), CookPersonalMenuActivity.class);
        startActivityForResult(intent,0);
    }

}