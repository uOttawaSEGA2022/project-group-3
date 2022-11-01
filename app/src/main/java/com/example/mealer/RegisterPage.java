package com.example.mealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
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

public class RegisterPage extends AppCompatActivity {
TextView registerEmail, registerPassword;
private static ArrayList<String> clientUsernames = new ArrayList<>();
private static ArrayList<String> clientPasswords = new ArrayList<>();
private static ArrayList<String> cookUsernames = new ArrayList<>();
private static ArrayList<String> cookPasswords = new ArrayList<>();

private static ArrayList<Client> clientList = new ArrayList<>();
private static ArrayList<Cook> cookList = new ArrayList<>();

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public boolean isFieldValid(){

        String usernameInput = registerEmail.getText().toString();
        String passwordInput = registerPassword.getText().toString();

        //Check if a field is empty
        if ((usernameInput.equals(""))||(passwordInput.equals(""))){
            displayToast("No empty fields");
            return false;
        }

        //Check if there is whitespace
        for (char c : usernameInput.toCharArray()) {
            if (Character.isWhitespace(c)) {
                displayToast("No whitespace in username");
                return false;
            }
        }
        for (char c : passwordInput.toCharArray()) {
            if (Character.isWhitespace(c)) {
                displayToast("No whitespace in password");
                return false;
            }
        }

        //Check and make sure email is username
        int atCounter = 0;
        int dotCounter = 0;
        for (char c : usernameInput.toCharArray()) {
            if (c == '.'){
                dotCounter += 1;
            }
            if (c == '@'){
                atCounter += 1;
            }
        }

        if ((dotCounter!=1)||(atCounter!=1)){
            displayToast("Invalid username. Please input an e-mail.");
            return false;
        }
        return true;
    }

    public void registerAsCook(View view){
        if (isFieldValid()) {
            cookUsernames.add(registerEmail.getText().toString());
            cookPasswords.add(registerPassword.getText().toString());

            // Create a new Cook instance with inputted username and password, and add it to list of cooks
            Cook cook = new Cook(registerEmail.getText().toString(), registerPassword.getText().toString());
            cookList.add(cook);

            mAuth.createUserWithEmailAndPassword(registerEmail.getText().toString(), registerPassword.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                sendToExtraRegistration(view,cook);
                            }
                        }
                    });
        }
    }

    public void registerAsClient(View view){
        if (isFieldValid()) {
            clientUsernames.add(registerEmail.getText().toString());
            clientPasswords.add(registerPassword.getText().toString());

            // Create a new Client instance with inputted username and password, and add it to list of clients
            Client client = new Client(registerEmail.getText().toString(), registerPassword.getText().toString());
            clientList.add(client);

            mAuth.createUserWithEmailAndPassword(registerEmail.getText().toString(), registerPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        sendToExtraRegistration(view,client);
                                    }
                                }
                            });


        }
    }

    public void alreadyHasAnAccount(View view){
        sendToLogin(view);
    }

    private void sendIntentToLogin(){
        Intent intent = new Intent (getApplicationContext(), LoginActivity.class);
        startActivityForResult(intent,0);
    }

    private void sendToLogin(View view){
        Intent returnIntent = new Intent();
        setResult(RESULT_OK,returnIntent);
        sendIntentToLogin();
        //finishing activity send to register screen
        finish();
    }

    private void sendIntentToExtraRegistrationCook(Account account){
        Intent intent = new Intent (getApplicationContext(), CookRegisterActivity.class);
        intent.putExtra("cookObj", account);
        startActivityForResult(intent,0);
    }

    private void sendIntentToExtraRegistrationClient(Account account){
        Intent intent = new Intent (getApplicationContext(), ClientRegisterActivity.class);
        intent.putExtra("clientObj", account);
        startActivityForResult(intent,0);
    }

    private void sendToExtraRegistration(View view, Account account){
        Intent returnIntent = new Intent();
        setResult(RESULT_OK,returnIntent);
        if (account instanceof Cook) {
            sendIntentToExtraRegistrationCook(account);
        } else {
            sendIntentToExtraRegistrationClient(account);
        }
        //finishing activity send to register screen
        finish();
    }

    public void displayToast(String message){
        Toast.makeText(RegisterPage.this, message, Toast.LENGTH_SHORT).show();
    }
}