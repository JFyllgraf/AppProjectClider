package com.example.nicolai.clider;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicolai.clider.model.UserPreferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserActivity extends AppCompatActivity {

    Button logOut, browse;
    TextView userEmail;
    FirebaseAuth firebaseAuth;
    RadioButton woman, man;
    UserPreferences userPreferences;
    DatabaseReference firebaseDatabase;

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

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInfo();
            }
        });

    }

    private void saveUserInfo(){
        if (man.isChecked()){
            userPreferences = new UserPreferences("Man");
        }
        if (woman.isChecked()){
            userPreferences = new UserPreferences("Woman");
        }
        FirebaseUser user = firebaseAuth.getCurrentUser();
        firebaseDatabase.child(user.getUid()).setValue(userPreferences);
        Toast.makeText(this, "Information saved", Toast.LENGTH_SHORT).show();

    }

    private void initializeComponents() {
        logOut = findViewById(R.id.buttonLogOut);
        userEmail = findViewById(R.id.userEmail);
        firebaseAuth = FirebaseAuth.getInstance();
        woman = findViewById(R.id.radioButtonWoman);
        man = findViewById(R.id.radioButtonMan);
        browse = findViewById(R.id.browse_btn);
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
    }
}
