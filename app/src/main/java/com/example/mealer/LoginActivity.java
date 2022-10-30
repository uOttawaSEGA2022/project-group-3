package com.example.mealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class LoginActivity extends AppCompatActivity {
    TextView username;
    TextView password;

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
            if (hasLogin("Cook",username.getText().toString(),password.getText().toString())){
                returnToMain("Cook");
            } else{
                if (hasLogin("Admin",username.getText().toString(),password.getText().toString())){
                    returnToMain("Admin");
                }

            }

    }

    public void clientButtonPress(View view){
            //checks for correct username and password
            if (hasLogin("Client",username.getText().toString(),password.getText().toString())){
                returnToMain("Client");
            } else{
                if (hasLogin("Admin",username.getText().toString(),password.getText().toString())){
                    returnToMain("Admin");
                }
            }

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

    private boolean hasLogin(String typeOfLogin, String username, String password){
        //checks if they are trying to login and verifies
        switch (typeOfLogin){
            case ("Cook"):
                if ((username.equals("cook")&&password.equals("cook123"))||(RegisterPage.checkCookInfo(username, password))) {
                    return true;
                }
                else{
                    return signInAuth("Cook");
                }
            case ("Client"):
                if ((username.equals("client")&&password.equals("client123"))||(RegisterPage.checkClientInfo(username, password))){
                    return true;
                }
                else{
                    signInAuth("Client");
                }
            case ("Admin"):
                if (username.equals("admin")&&password.equals("admin123")) {
                    return true;
                }
                else{
                    return signInAuth("Admin");
                }
            default:
                return false;
        }
    }

    private boolean signInAuth(String loginType) {

        mAuth.signInWithEmailAndPassword(username.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    userExists = true;
                    sendIntentToMain(loginType);
                } else {
                    userExists = false;
                    displayToast("Incorrect password or username");
                }
            }
        });

        return userExists;
    }

    public void displayToast(String message){
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

}