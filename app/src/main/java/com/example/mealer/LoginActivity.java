package com.example.mealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    // Instance variables

    TextView username;
    TextView password;
    Spinner spinner;

    FirebaseUser user;
    String userID;
    DatabaseReference database;
    private FirebaseAuth mAuth;

    List<Account> accountList;

    String loginType;

    /*
     * OnCreate method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //sets all TextViews and buttons
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

    /*
     * OnStart method
     */
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

                    // NOTE: == instead of .equals() due to HashMap casting exception

                    if (postSnapshot.getValue(Account.class).getRole() == "Cook") {
                        Cook account = postSnapshot.getValue(Cook.class);

                        //adding account to the list
                        accountList.add(account);
                    } else if (postSnapshot.getValue(Account.class).getRole() == "Client") {
                        Client account = postSnapshot.getValue(Client.class);

                        //adding account to the list
                        accountList.add(account);
                    } else if (postSnapshot.getValue(Account.class).getRole() == "Admin") {
                        Admin account = postSnapshot.getValue(Admin.class);

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

    /*
     * method for verifying login information
     */
    public void loginButtonPress(View view) {
        Spinner spinner = (Spinner)findViewById(R.id.userType);
        String text = spinner.getSelectedItem().toString();
        try{
            hasLogin(text,username.getText().toString(),password.getText().toString());
        }catch (Exception e){
            displayToast("Error, try again,");
            System.out.println(e);
        }

    }

    // method for registering a new account
    public void registerButtonPress(View view){
        Intent returnIntent = new Intent();
        setResult(RESULT_OK,returnIntent);
        sendIntentToRegister();
        //finishing activity send to register screen
        finish();
    }

    // method for going to register activity
    private void sendIntentToRegister(){
        Intent intent = new Intent (getApplicationContext(), RegisterPage.class);
        startActivityForResult(intent,0);
    }

    // method for going to welcome cook activity
    private void sendToWelcomeCook(){
        Intent intent;
        intent = new Intent(getApplicationContext(), WelcomeCookPage.class);
        startActivityForResult(intent, 0);
    }

    // method for going to welcome client activity
    private void sendToWelcomeClient(){
        Intent intent;
        intent = new Intent(getApplicationContext(), WelcomeClientPage.class);
        startActivityForResult(intent, 0);
    }

    // method for going to welcome admin activity
    private void sendToWelcomeAdmin(){
        Intent intent;
        intent = new Intent(getApplicationContext(), WelcomeAdminPage.class);
        startActivityForResult(intent, 0);
    }

    /*
     * send to proper activity based on type of login
     * @param typeOfLogin indicates the type of login
     */
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

    /*
     * verifies login based on accounts in database
     * @param typeOfLogin indicates the type of login
     * @param username indicates user's name
     * @param typeOfLogin indicates user's password
     */
    private void hasLogin(String typeOfLogin, String username, String password){

        // fail fast checks to see if login is null or if login is admin
        if (typeOfLogin == null) {
            displayToast("No login type selected");
            return;
        }
        if (typeOfLogin.equals("Admin")){
            if (username.equals("admin") && password.equals("admin123")){
                sendIntentToMain(typeOfLogin);
                return;
            }else{
                displayToast("Incorrect admin credentials");
                return;
            }

        }

        // attempt to sign in user with Firebase Authentication
        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // set vars for UID and current user
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    userID = user.getUid();

                    database.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Log.d("TAG", snapshot.child("clients").child(userID).toString());
                            if (typeOfLogin.equals("Client")){
                                if (snapshot.child("clients").child(userID).hasChildren()){
                                    sendIntentToMain(typeOfLogin);
                                }else{
                                    displayToast("No "+typeOfLogin+" under that username");
                                }

                            }else if (typeOfLogin.equals("Cook")){
                                if (snapshot.child("cooks").child(userID).hasChildren()){
                                    sendIntentToMain(typeOfLogin);
                                }else{
                                    displayToast("No "+typeOfLogin+" under that username");
                                }

                            }else{
                                displayToast("Incorrect login");
                            }








//                            Log.d("bruh",snapshot.toString());
//                            Client thisClient = snapshot.getValue(Client.class);
//                            Log.d("TAG",thisClient.getRole());
//                            // get UID account from db and check its role (FINISH IMPLEMENTATION)
//                            if (snapshot.child("role").getValue(String.class).equals(typeOfLogin)) {
//                                sendIntentToMain(typeOfLogin);
//                            } else {
//                                displayToast("No " + typeOfLogin +" account exists for " + username);
//                                return;
//                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    //sendIntentToMain(typeOfLogin);
                } else {
                    displayToast("Incorrect password or username");
                    return;
                }
            }
        });

    }

    // method for displaying toast
    public void displayToast(String message){
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    // set login type to what is selected in the dropdown
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        loginType = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}