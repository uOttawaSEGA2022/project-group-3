package com.example.mealer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class AddMenuItem extends AppCompatActivity {
    TextView title;
    TextView description;
    TextView ingredients;
    private MenuItem thisItem;
    DatabaseReference masterMenu;
    DatabaseReference thisCooksMenus;
    FirebaseUser thisCook;
    private String cookID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_item);
        title=findViewById(R.id.titleField);
        description=findViewById(R.id.descriptionField);
        ingredients=findViewById(R.id.ingredientsField);
        masterMenu = FirebaseDatabase.getInstance().getReference("master-menu");
        thisCook = FirebaseAuth.getInstance().getCurrentUser();
        cookID = thisCook.getUid();
        thisCooksMenus = FirebaseDatabase.getInstance().getReference("accounts").child(cookID).child("Cook Menu");

    }

    public void addMenuItem(View view){
        if (!cannotSubmit()){
            submitMenuItemToDatabase();
            displayToast("Item added");
            sendToCookMenu(view);
            return;
        }

        displayToast("Invalid fields");
    }


    public void sendToCookMenu(View view){
        Intent intent = new Intent(getApplicationContext(), CookPersonalMenuActivity.class);
        startActivityForResult(intent, 0);
    }


    private boolean cannotSubmit(){
        boolean titleEmpty = title.getText().toString().isEmpty();
        boolean descriptionEmpty = description.getText().toString().isEmpty();
        boolean ingredientsEmpty = ingredients.getText().toString().isEmpty();

        return titleEmpty || descriptionEmpty || ingredientsEmpty;

    }

    private void submitMenuItemToDatabase(){
        thisItem = new MenuItem(title.getText().toString(),description.getText().toString(),ingredients.getText().toString(),cookID);
        masterMenu.child(title.getText().toString()).setValue(thisItem);
        thisItem = new MenuItem(title.getText().toString(),description.getText().toString(),ingredients.getText().toString());
        thisCooksMenus.child(title.getText().toString()).setValue(thisItem);

    }

    public void displayToast(String message){
        Toast.makeText(AddMenuItem.this, message, Toast.LENGTH_SHORT).show();
    }

}