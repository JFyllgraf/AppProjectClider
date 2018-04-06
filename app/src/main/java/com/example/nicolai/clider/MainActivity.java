package com.example.nicolai.clider;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Button register;
    EditText email_edit, password_edit;
    TextView alreadyUser_txt;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeComponents();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

    }

    private void registerUser(){
        String email = email_edit.getText().toString().trim();
        String password = password_edit.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            email_edit.setError("Please fill");
        }
        if (TextUtils.isEmpty(password)){
            password_edit.setError("Please fill");
        }

        progressDialog.setMessage("Registering.. ");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Registered!", Toast.LENGTH_SHORT).show();
                    progressDialog.hide();
                }else {
                    Toast.makeText(MainActivity.this, "Could not register", Toast.LENGTH_SHORT).show();
                    progressDialog.hide();
                }
            }
        });
    }

    private void initializeComponents() {
        register = findViewById(R.id.buttonRegister);

        email_edit = findViewById(R.id.textEmail);
        password_edit = findViewById(R.id.textPassword);

        alreadyUser_txt = findViewById(R.id.link);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
    }

}
