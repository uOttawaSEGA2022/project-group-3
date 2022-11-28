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

public class AddMenuItem extends AppCompatActivity {

    TextView title;
    TextView description;
    TextView ingredients;
    TextView mealType;
    TextView cuisineType;
    TextView price;
    TextView allergens;

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
        mealType=findViewById(R.id.mealTypeField);
        cuisineType=findViewById(R.id.cuisineTypeField);
        allergens=findViewById(R.id.allergensField);
        price=findViewById(R.id.priceField);

        masterMenu = FirebaseDatabase.getInstance().getReference("master-menu");
        thisCook = FirebaseAuth.getInstance().getCurrentUser();
        cookID = thisCook.getUid();
        thisCooksMenus = FirebaseDatabase.getInstance().getReference("accounts").child("cooks").child(cookID).child("Cook Menu");

    }

    public void addMenuItem(View view){
        if (!cannotSubmit()){
            submitMenuItemToDatabase();
            displayToast("Meal added!");
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
        boolean mealTypeEmpty = mealType.getText().toString().isEmpty();
        boolean cuisineTypeEmpty = cuisineType.getText().toString().isEmpty();
        boolean allergensEmpty = allergens.getText().toString().isEmpty();
        boolean priceEmpty = price.getText().toString().isEmpty();

        return titleEmpty || descriptionEmpty || ingredientsEmpty ||
                mealTypeEmpty || cuisineTypeEmpty || priceEmpty || allergensEmpty;

    }

    private void submitMenuItemToDatabase(){
        String title = this.title.getText().toString();
        String desc = description.getText().toString();
        String ingred = ingredients.getText().toString();
        String mType = mealType.getText().toString();
        String cType = cuisineType.getText().toString();
        String al = allergens.getText().toString();
        String price = this.price.getText().toString();

        thisItem = new MenuItem(title,desc,ingred,mType,cType,al,price,cookID,false);
        masterMenu.child(this.title.getText().toString()).setValue(thisItem);
        thisCooksMenus.child(this.title.getText().toString()).setValue(thisItem);

    }

    public void displayToast(String message){
        Toast.makeText(AddMenuItem.this, message, Toast.LENGTH_SHORT).show();
    }

}