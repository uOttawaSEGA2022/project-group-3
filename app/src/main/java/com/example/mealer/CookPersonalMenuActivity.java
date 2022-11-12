package com.example.mealer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CookPersonalMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_personal_menu);
    }

    public void sendToAddMeal(View view){
        Intent intent = new Intent (getApplicationContext(), AddMenuItem.class);
        startActivityForResult(intent,0);
    }
}