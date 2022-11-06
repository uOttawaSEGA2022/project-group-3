package com.example.mealer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
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


public class AdminInboxActivity extends AppCompatActivity {

    DatabaseReference database;
    private static int whichComplaint = 0;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_inbox);

        database = FirebaseDatabase.getInstance().getReference("complaints");

        textView = new TextView(this);




//        addToDescpritionAndTitleToComplaint(0,"potato raw","uncooked potoat");
//        addToDescpritionAndTitleToComplaint(1,"Eggs benedict","The eggs were dry");
//        addToDescpritionAndTitleToComplaint(2,"The bread was moldy","Moldy");
//        addToDescpritionAndTitleToComplaint(3,"The banana was expired","rotten bananada");





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


                for (DataSnapshot postSnapshot: snapshot.getChildren()){
                    description.add(postSnapshot.child("Description").getValue().toString());
                    title.add(postSnapshot.child("Title").getValue().toString());

                }

                for (int i = 1; i <= description.size(); i++) {

                    textView.setText("Title: "+title.get(i)+'\n'+"Description"+description.get(i));
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




}