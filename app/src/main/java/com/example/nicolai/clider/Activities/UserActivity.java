package com.example.nicolai.clider.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicolai.clider.Activities.BrowsingActivities.BrowseActivity1;
import com.example.nicolai.clider.R;
import com.example.nicolai.clider.model.UserPreferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserActivity extends AppCompatActivity {

    Button logOut, browse;
    TextView userEmail;
    FirebaseAuth firebaseAuth;
    RadioButton woman, man;
    UserPreferences userPreferences;
    UserPreferences userPreferencesFromFirebasee;
    DatabaseReference firebaseDatabase;
    NumberPicker pickerAge;
    ListView clothesList;
    List<String> clothes;
    HashMap<String, Boolean> clothesPreferences1;

    int age;
    String sex;
    ArrayList<String> clothesPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        initializeComponents();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        userEmail.setText("Welcome: "+ user.getEmail());
        clothesPreferences = new ArrayList<>();
        clothesPreferences1 = new HashMap<String, Boolean>();

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

        pickerAge.setMinValue(12);
        pickerAge.setMaxValue(45);
        setUpClothesList();

        //Hvordan skal det lige hentes ned igen fra firebase? Altså den her kører på array agtig format, og pt bruger jeg et hashmap
        SparseBooleanArray checkedItems = clothesList.getCheckedItemPositions();
        for (int j = 0; j < checkedItems.size() ; j++) {
            if (checkedItems.get(j)){
                clothesList.setItemChecked(j, true);
            } else {
                clothesList.setItemChecked(j, false);}
            }
        clothesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //mangler en bedre løsnign til dette

                if (clothesPreferences1.get(clothesList.getItemAtPosition(i).toString())!=null &&
                        clothesPreferences1.get(clothesList.getItemAtPosition(i).toString())== true){
                    Toast.makeText(UserActivity.this, "Removed", Toast.LENGTH_SHORT).show();
                    clothesPreferences1.put(clothesList.getItemAtPosition(i).toString(), false);
                } else {
                    Toast.makeText(UserActivity.this, "Added", Toast.LENGTH_SHORT).show();
                    clothesPreferences1.put(clothesList.getItemAtPosition(i).toString(), true);
                }


                //overvej at bruge et hashmap med key string og value checked / unchecked
              /* if (!clothesPreferences.contains(clothesList.getItemAtPosition(i))){
                    Toast.makeText(UserActivity.this, "AddedItem", Toast.LENGTH_SHORT).show();
                    clothesPreferences.add((String)clothesList.getItemAtPosition(i));
                }
                else if (clothesPreferences.contains(clothesList.getItemAtPosition(i))){
                    Toast.makeText(UserActivity.this, "Removed item", Toast.LENGTH_SHORT).show();
                    clothesPreferences.remove((String)clothesList.getItemAtPosition(i));
                }*/

            }
        });

        //clothesList.set
        retrieveDataFromFirebase();
    }

    private void retrieveDataFromFirebase(){
        firebaseDatabase.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userPreferencesFromFirebasee = dataSnapshot.getValue(UserPreferences.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void saveUserInfo(){
        if (man.isChecked()){
            sex = "Man";
        }
        if (woman.isChecked()){
            sex  = "Woman";
        }
        age = pickerAge.getValue();
        userPreferences = new UserPreferences(sex, age, clothesPreferences1);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        firebaseDatabase.child(user.getUid()).setValue(userPreferences);
        Toast.makeText(this, "Information saved", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, BrowseActivity1.class));
        finish();

    }

    private void setUpClothesList(){
        clothes = new ArrayList<String>();
        clothes.add("Shoes");
        clothes.add("Dresses");
        clothes.add("Hats");
        clothes.add("Jackets");
        Adapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, clothes);
        clothesList = (ListView) findViewById(R.id.clothes_list);
        clothesList.setAdapter((ListAdapter) adapter);
        clothesList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        //clothesList.setAdapter(new ClothesAdapter(this, clothes));
    }

    private void initializeComponents() {
        logOut = findViewById(R.id.buttonLogOut);
        userEmail = findViewById(R.id.userEmail);
        firebaseAuth = FirebaseAuth.getInstance();
        woman = findViewById(R.id.radioButtonWoman);
        man = findViewById(R.id.radioButtonMan);
        browse = findViewById(R.id.browse_btn);
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        pickerAge = findViewById(R.id.numberPicker_age);


    }
}
