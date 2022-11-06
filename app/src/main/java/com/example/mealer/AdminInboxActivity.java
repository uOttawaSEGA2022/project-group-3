package com.example.mealer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ButtonBarLayout;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;


public class AdminInboxActivity extends AppCompatActivity implements View.OnClickListener{

    DatabaseReference database;
    DatabaseReference accountsDB;
    private static int whichComplaint = 0;
    TextView textView;
    private static Context inbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_inbox);

        database = FirebaseDatabase.getInstance().getReference("complaints");

        inbox = this;

    }
    @Override
    protected void onStart() {

        super.onStart();

        LinearLayout linearLayout = findViewById(R.id.adminInbox);
        //attaching value event listener
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //clearing the previous artist list

                ArrayList<String> title = new ArrayList<>();
                ArrayList<String> description = new ArrayList<>();
                ArrayList<String> username = new ArrayList<>();

                for (DataSnapshot postSnapshot: snapshot.getChildren()){
                    description.add(postSnapshot.child("Description").getValue().toString());
                    title.add(postSnapshot.child("Title").getValue().toString());
                    username.add(postSnapshot.child("username").getValue().toString());
                }

                for (int i = 0; i <= description.size()-1; i++) {
                    textView = new TextView(inbox);
                    textView.setText("Title: "+title.get(i)+'\n'+"Description: "+description.get(i));
                    linearLayout.addView(textView);

                    EditText year = new EditText(inbox);
                    year.setText("Year");
                    linearLayout.addView(year);
                    EditText month = new EditText(inbox);
                    month.setText("Month");
                    linearLayout.addView(month);
                    EditText day = new EditText(inbox);
                    day.setText("Day");
                    linearLayout.addView(day);

                    String currentUsername = username.get(i);

                    Button tempBanButton = new Button(inbox);
                    tempBanButton.setText("Temporary Ban");
                    tempBanButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    linearLayout.addView(tempBanButton);

                    Button permBanButton = new Button(inbox);
                    permBanButton.setText("Permanent Ban");
                    permBanButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    linearLayout.addView(permBanButton);

                    Button dismissButton = new Button(inbox);
                    dismissButton.setText("Dismiss");
                    dismissButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    linearLayout.addView(dismissButton);

                    textView = new TextView(inbox);
                    textView.setText("");
                    linearLayout.addView(textView);

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

    public void addComplaint(Cook a){
        database.child("complaint"+" "+whichComplaint).setValue(a);
        whichComplaint++;
    }

    public void addToDescpritionAndTitleToComplaint (int i, String title, String description){
        database.child("complaint"+" "+i).child("Title").setValue(title);
        database.child("complaint"+" "+i).child("Description").setValue(description);
    }

    @Override
    public void onClick(View view) {

    }

    public void displayToast(String message){
        Toast.makeText(AdminInboxActivity.this, message, Toast.LENGTH_SHORT).show();
    }



}