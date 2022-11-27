package com.example.mealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MasterMenu extends AppCompatActivity {

    DatabaseReference masterMenu, allTheCooks;
    TextView textView;
    private static Context menu;
    LinearLayout innerLayout, buttonLayout, mainLayout;
    ArrayList<String> title, price, mealType, ingredients, description, cuisineType, cookID, allergens;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_menu);


        masterMenu = FirebaseDatabase.getInstance().getReference("master-menu");
        allTheCooks = FirebaseDatabase.getInstance().getReference("accounts").child("cooks");
        menu=this;

    }

    @Override
    protected void onStart() {

        super.onStart();

        mainLayout = findViewById(R.id.masterMenu);

        masterMenu.addValueEventListener(new ValueEventListener() {
            String isOffered;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()){
                    isOffered = postSnapshot.child("isOffered").getValue().toString();
                    if (isOffered.equals("true")){
                        title.add(postSnapshot.child("title").getValue().toString());
                        description.add(postSnapshot.child("description").getValue().toString());
                        price.add(postSnapshot.child("price").getValue().toString());
                        mealType.add(postSnapshot.child("mealType").getValue().toString());
                        ingredients.add(postSnapshot.child("ingredients").getValue().toString());
                        cuisineType.add(postSnapshot.child("cuisineType").getValue().toString());
                        cookID.add(postSnapshot.child("cookID").getValue().toString());
                        allergens.add(postSnapshot.child("allergens").getValue().toString());
                    }





                }

                for (int i = 0; i < title.size(); i++){

                    innerLayout = new LinearLayout(menu);





                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}