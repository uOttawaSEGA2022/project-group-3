package com.example.mealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    TextView username;
    TextView password;
    Spinner spinner;

    DatabaseReference database;

    private FirebaseAuth mAuth;

    List<Account> accountList;

    boolean userExists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //sets all textviews and buttons
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        spinner = findViewById(R.id.userType);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.user_type, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        //Initialize db with accounts section
        database = FirebaseDatabase.getInstance().getReference("accounts");

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        accountList = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //attaching value event listener
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //clearing the previous artist list
                accountList.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    //getting product
                    if (postSnapshot.getValue(Account.class).getRole() == "Cook") {
                        Cook account = (Cook) postSnapshot.getValue();

                        //adding account to the list
                        accountList.add(account);
                    } else if (postSnapshot.getValue(Account.class).getRole() == "Client") {
                        Client account = (Client) postSnapshot.getValue();

                        //adding account to the list
                        accountList.add(account);
                    } else if (postSnapshot.getValue(Account.class).getRole() == "Admin") {
                        Admin account = (Admin) postSnapshot.getValue();

                        //adding account to the list
                        accountList.add(account);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    public void cookButtonPress(View view){
            //checks for correct username and password
            hasLogin("Cook",username.getText().toString(),password.getText().toString());

    }

    public void clientButtonPress(View view){
            //checks for correct username and password
            hasLogin("Client",username.getText().toString(),password.getText().toString());

    }

    //this method should return to the main activity where the app will actually happen, not working as of Oct 16 2022
    private void returnToMain (String typeOfLogin){
        Intent returnIntent = new Intent();
        returnIntent.putExtra(typeOfLogin,true);
        setResult(RESULT_OK,returnIntent);
        sendIntentToMain(typeOfLogin);
        //finishing activity and return to main screen
        finish();
    }
    public void registerButtonPress(View view){
        Intent returnIntent = new Intent();
        setResult(RESULT_OK,returnIntent);
        sendIntentToRegister();
        //finishing activity send to register screen
        finish();
    }
    private void sendIntentToRegister(){
        Intent intent = new Intent (getApplicationContext(), RegisterPage.class);
        startActivityForResult(intent,0);
    }

    private void sendToWelcomeCook(){
        Intent intent;
        intent = new Intent(getApplicationContext(), WelcomeCookPage.class);
        startActivityForResult(intent, 0);
    }

    private void sendToWelcomeClient(){
        Intent intent;
        intent = new Intent(getApplicationContext(), WelcomeClientPage.class);
        startActivityForResult(intent, 0);
    }

    private void sendToWelcomeAdmin(){
        Intent intent;
        intent = new Intent(getApplicationContext(), WelcomeAdminPage.class);
        startActivityForResult(intent, 0);
    }

    private void sendIntentToMain(String typeOfLogin){
        //Application Context and Activity
        switch (typeOfLogin){
            case "Cook":
                sendToWelcomeCook();
                break;
            case "Client":
                sendToWelcomeClient();
                break;
            case "Admin":
                sendToWelcomeAdmin();
                break;

            default:
                displayToast("Error, could not login.");
        }


    }

    private void hasLogin(String typeOfLogin, String username, String password){

        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    /* Add a call to a method that accesses database with UID and checks role
                       then calls sendIntentToMain with that role*/
                    sendIntentToMain(typeOfLogin);
                } else {
                    displayToast("Incorrect password or username");
                }
            }
        });

    }

    public void displayToast(String message){
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        //Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}