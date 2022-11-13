package com.example.mealer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ButtonBarLayout;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.Iterator;


public class AdminInboxActivity extends AppCompatActivity implements View.OnClickListener{

    DatabaseReference complaintDatabase;
    DatabaseReference accountDatabase;
    private static int whichComplaint = 0;
    TextView textView;
    private static Context inbox;
    LinearLayout innerLayout;
    LinearLayout linearLayoutForButtons;
    LinearLayout linearLayoutForDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_inbox);

        complaintDatabase = FirebaseDatabase.getInstance().getReference("complaints");
        accountDatabase = FirebaseDatabase.getInstance().getReference("accounts");

        inbox = this;

    }
    @Override
    protected void onStart() {

        super.onStart();

        LinearLayout linearLayout = findViewById(R.id.adminInbox);

        //attaching value event listener
        complaintDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //clearing the previous lists

                ArrayList<String> title = new ArrayList<>();
                ArrayList<String> description = new ArrayList<>();
                ArrayList<String> username = new ArrayList<>();
                ArrayList<String> cookIDs = new ArrayList<>();
                ArrayList<String> complaintIDs = new ArrayList<>();

                for (DataSnapshot postSnapshot: snapshot.getChildren()){
                    description.add(postSnapshot.child("Description").getValue().toString());
                    title.add(postSnapshot.child("Title").getValue().toString());
                    username.add(postSnapshot.child("username").getValue().toString());
                    cookIDs.add(postSnapshot.child("cookID").getValue().toString());
                    complaintIDs.add(postSnapshot.getKey());
                }

                for (int i = 0; i <= description.size()-1; i++) {

                    innerLayout = new LinearLayout(inbox);

                    textView = new TextView(inbox);
                    textView.setText("Title: "+title.get(i)+'\n'+"Description: "+description.get(i) +'\n'+"Name: "+username.get(i));
                    innerLayout.addView(textView);
                    EditText year = new EditText(inbox);

                    linearLayoutForDate = new LinearLayout(inbox);
                    linearLayoutForDate.setOrientation(LinearLayout.HORIZONTAL);
                    linearLayoutForDate.setGravity(Gravity.CENTER_VERTICAL);

                    year.setHint("Year");
                    linearLayoutForDate.addView(year);
                    EditText month = new EditText(inbox);
                    month.setHint("Month");
                    linearLayoutForDate.addView(month);
                    EditText day = new EditText(inbox);
                    day.setHint("Day");
                    linearLayoutForDate.addView(day);

                    String currentCookID = cookIDs.get(i);
                    String currentComplaint = complaintIDs.get(i);
                    String yearInput, monthInput, dayInput;

                    linearLayoutForButtons = new LinearLayout(inbox);
                    linearLayoutForButtons.setOrientation(LinearLayout.HORIZONTAL);
                    linearLayoutForButtons.setGravity(Gravity.CENTER_VERTICAL);

                    Button tempBanButton = new Button(inbox);
                    tempBanButton.setText("Temporary Ban");
                    tempBanButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String yearInput = year.getText().toString();
                            String monthInput = month.getText().toString();
                            String dayInput = day.getText().toString();
                            accountDatabase.child(currentCookID).child("temporaryBan").setValue(yearInput.toString()+"-"+monthInput.toString()+"-"+dayInput.toString());
                            complaintDatabase.child(currentComplaint).removeValue();
                        }
                    });
                    linearLayoutForButtons.addView(tempBanButton);

                    Button permBanButton = new Button(inbox);
                    permBanButton.setText("Permanent Ban");
                    permBanButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            accountDatabase.child(currentCookID).child("permanentBan").setValue("true");
                            complaintDatabase.child(currentComplaint).removeValue();
                        }
                    });
                    linearLayoutForButtons.addView(permBanButton);

                    Button dismissButton = new Button(inbox);
                    dismissButton.setText("Dismiss");
                    dismissButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            complaintDatabase.child(currentComplaint).removeValue();
                        }
                    });

                    linearLayoutForButtons.addView(dismissButton);

                    innerLayout.addView(linearLayoutForDate);
                    innerLayout.addView(linearLayoutForButtons);
                    innerLayout.setOrientation(LinearLayout.VERTICAL);
                    linearLayout.addView(innerLayout);


                }

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

    public void addComplaint(Cook a){
        complaintDatabase.child("complaint"+" "+whichComplaint).setValue(a);
        whichComplaint++;
    }

    public void addToDescpritionAndTitleToComplaint (int i, String title, String description){
        complaintDatabase.child("complaint"+" "+i).child("Title").setValue(title);
        complaintDatabase.child("complaint"+" "+i).child("Description").setValue(description);
    }

    @Override
    public void onClick(View view) {

    }

    public void displayToast(String message){
        Toast.makeText(AdminInboxActivity.this, message, Toast.LENGTH_SHORT).show();
    }



}