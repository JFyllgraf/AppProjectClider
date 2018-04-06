package com.example.nicolai.clider;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserActivity extends AppCompatActivity {

    Button logOut;
    TextView userEmail;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        initializeComponents();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        userEmail.setText("Welcome: "+ user.getEmail());

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

    }

    private void initializeComponents() {
        logOut = findViewById(R.id.buttonLogOut);
        userEmail = findViewById(R.id.userEmail);
        firebaseAuth = FirebaseAuth.getInstance();
    }
}
