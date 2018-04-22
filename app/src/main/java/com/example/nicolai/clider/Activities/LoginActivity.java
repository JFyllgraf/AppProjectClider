package com.example.nicolai.clider.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicolai.clider.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText email_login, password_login;
    Button login;
    TextView sigin;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeComponents();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logUserIn();
            }
        });
        sigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    private void logUserIn(){
        String email = email_login.getText().toString().trim();
        String password = password_login.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            email_login.setError("Please fill");
        }
        if (TextUtils.isEmpty(password)){
            password_login.setError("Please fill");
        }
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
        progressDialog.setMessage("Login in.. ");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    finish();
                    startActivity(new Intent(getApplicationContext(), UserActivity.class));

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Unknown user, try again", Toast.LENGTH_LONG).show();
            }
        });
    }
    }

    private void initializeComponents() {
        email_login = findViewById(R.id.textEmailLogin);
        password_login = findViewById(R.id.textPasswordLogin);
        login = findViewById(R.id.buttonLogin);
        sigin = findViewById(R.id.linkLogin);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
    }
}
