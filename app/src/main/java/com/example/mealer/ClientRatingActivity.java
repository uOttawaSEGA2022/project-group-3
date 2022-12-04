package com.example.mealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Formatter;

public class ClientRatingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    FirebaseUser thisClient;
    DatabaseReference cooks;
    DatabaseReference ratingDB;
    Boolean isSelected;
    String selectedCookID;
    TextView cookName;
    Spinner ratingSpinner;
    String newRating;
    double currentRating;
    boolean submitted;

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
        submitted = false;

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.cookRatings, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ratingSpinner.setAdapter(adapter);
        ratingSpinner.setOnItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        LinearLayout linearLayout = findViewById(R.id.cooksLayout);

        cooks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // create a food item for each snapshot in db reference
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    // create layout for each meal
                    LinearLayout cooksLayout = new LinearLayout(ratingsPage);
                    LinearLayout.LayoutParams cooksLayoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    cooksLayoutParams.setMargins(0, 20, 0, 0);

                    String thisFirstName = postSnapshot.child("firstName").getValue().toString();
                    String thisLastName = postSnapshot.child("lastName").getValue().toString();

                    cookName = new TextView(ratingsPage);
                    RelativeLayout relativeLayoutForCookTitle = new RelativeLayout(ratingsPage);
                    // set layout params
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(450, 120); //Width just has to be bigger than screen width size
                    params.leftMargin = 30;
                    params.topMargin = 20;
                    params.bottomMargin = 20;

                    // add meal to layout, add layout to main layout
                    relativeLayoutForCookTitle.addView(cookName, params);
                    cooksLayout.addView(relativeLayoutForCookTitle);

                    // set custom settings
                    cookName.setTextSize(18);
                    cookName.setGravity(Gravity.CENTER_VERTICAL);
                    cookName.setText(thisFirstName + " " + thisLastName);

                    Button selectButton = new Button(ratingsPage);

                    // set text colour & background
                    selectButton.setTextColor(Color.parseColor("#ffffff"));
                    selectButton.setBackgroundResource(R.drawable.green_rounded_button_20dp);

                    isSelected = false;
                    selectButton.setBackgroundResource(R.drawable.green_rounded_button_20dp);
                    selectButton.setText("Select");

                    RelativeLayout buttonRelativeLayout = new RelativeLayout(ratingsPage);

                    params = new RelativeLayout.LayoutParams(250, 150);
                    params.topMargin = 25;
                    params.bottomMargin = 25;
                    params.leftMargin = 100;
                    buttonRelativeLayout.addView(selectButton, params);

                    cooksLayout.addView(buttonRelativeLayout);

                    cooksLayout.setBackgroundResource(R.drawable.grey_rounded_backround_20dp);

                    // listener for setting offered/unoffering
                    selectButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isSelected == false){
                                selectButton.setBackgroundResource(R.drawable.light_green_rounded_background);
                                selectButton.setText("Selected");
                                selectedCookID = postSnapshot.child("id").getValue().toString();
                                isSelected = true;
                            }else{
                                selectButton.setBackgroundResource(R.drawable.green_rounded_button_20dp);
                                selectButton.setText("Select");
                                selectedCookID = null;
                                isSelected = false;
                            }
                        }
                    });

                    linearLayout.addView(cooksLayout, cooksLayoutParams);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        newRating = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // do nothing
    }

    public void submitRating(View view) {
        if (newRating == null) {
            displayToast("No rating provided");
            return;
        } else if (selectedCookID == null) {
            displayToast("No Cook Selected");
        } else {
            ratingDB = FirebaseDatabase.getInstance().getReference("accounts").child("cooks").child(selectedCookID).child("rating");

            ratingDB.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue() == null && !submitted) {
                        ratingDB.setValue(Float.parseFloat(newRating.split(" ")[0]));
                        displayToast("Rating submitted!");
                        exitPage();
                    } else if (!submitted) {
                        currentRating = snapshot.getValue(Float.class);
                        submitted = true;
                        sendRatingToDBAndExit();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    // method for sending to current cook's profile
    public void sendToWelcome(View view){
        Intent intent = new Intent(getApplicationContext(), WelcomeClientPage.class);
        startActivityForResult(intent,0);
    }

    // method for reloading activity
    public void sendRatingToDBAndExit(){
        double tmp = currentRating;
        tmp += Float.parseFloat(newRating.split(" ")[0]);
        tmp = tmp/2;

        if (tmp % 1 == 0) {
            tmp = tmp/1;
        }

        currentRating = tmp;
        currentRating = Math.floor(currentRating * 100) / 100;

        ratingDB.setValue(currentRating);

        displayToast("Rating submitted!");

        Intent intent = new Intent(getApplicationContext(), WelcomeClientPage.class);
        startActivityForResult(intent,0);
    }

    public void exitPage() {
        Intent intent = new Intent(getApplicationContext(), WelcomeClientPage.class);
        startActivityForResult(intent,0);
    }

    public void displayToast(String message){
        Toast.makeText(ClientRatingActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}