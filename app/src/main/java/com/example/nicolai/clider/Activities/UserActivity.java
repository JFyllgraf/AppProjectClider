package com.example.nicolai.clider.Activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicolai.clider.Activities.BrowsingActivities.BrowseActivity;
import com.example.nicolai.clider.R;
import com.example.nicolai.clider.Services.BackgroundService;
import com.example.nicolai.clider.Utils.Globals;
import com.example.nicolai.clider.model.TagItem;
import com.example.nicolai.clider.model.UserPreferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserActivity extends AppCompatActivity {

    Button logOut, browse;
    TextView userEmail;
    FirebaseAuth firebaseAuth;
    RadioButton rbWoman, rbMan;
    UserPreferences userPreferences;
    UserPreferences userPreferencesFromFirebasee;
    DatabaseReference firebaseDatabase;
    NumberPicker pickerAge;
    ListView clothesList;
    List<String> clothes;
    RadioGroup radioGroup;
    HashMap<String, Boolean> clothesPreferences1;
    ProgressDialog progressDialog;
    boolean serviceBound;
    List<TagItem> tagList;

    private BackgroundService backgroundService;
    private ServiceConnection backgroundServiceConnection;

    int age;
    String sex;
    ArrayList<String> clothesPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        initializeComponents();
        setUpConnectionToBackgroundService();
        bindToBackgroundService();
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
        progressDialog.setMessage("Fetching preferences.. ");
        progressDialog.show();

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
                    clothesPreferences1.put(tagList.get(i).getTagName(), false);
                } else {
                    Toast.makeText(UserActivity.this, "Added", Toast.LENGTH_SHORT).show();
                    clothesPreferences1.put(clothesList.getItemAtPosition(i).toString(), true);
                }


            }
        });


    }


    private void saveUserInfo(){
        if (rbMan.isChecked()){
            sex = Globals.male;
        }
        if (rbWoman.isChecked()){
            sex  = Globals.female;
        }
        age = pickerAge.getValue();
        userPreferences = new UserPreferences(sex, age, clothesPreferences1);
        backgroundService.saveUserInfo(userPreferences);
        Toast.makeText(this, "Information saved", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, BrowseActivity.class));
        finish();

    }

    private void setUpClothesList(){
        clothes = new ArrayList<String>();
        for (TagItem tagitem: tagList) {
            clothes.add(tagitem.getName());
        }
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
        rbWoman = findViewById(R.id.radioButtonWoman);
        rbMan = findViewById(R.id.radioButtonMan);
        browse = findViewById(R.id.browse_btn);
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        pickerAge = findViewById(R.id.numberPicker_age);
        progressDialog = new ProgressDialog(this);
        radioGroup = findViewById(R.id.radioGroup);
        setUpTagList();


    }

    private void setUpTagList() {
        tagList = new ArrayList<>();
        tagList.add(new TagItem(Globals.shoeTagName, 0, false, Globals.shoeTag));
        tagList.add(new TagItem(Globals.dressTagName, 1, false, Globals.dressTag));
        tagList.add(new TagItem(Globals.tshirtTagName, 2, false, Globals.tshirtTag));
        tagList.add(new TagItem(Globals.hatTagName, 3, false, Globals.hatTag));
        tagList.add(new TagItem(Globals.accessoriesTagName, 4, false, Globals.accessoriesTag));
        tagList.add(new TagItem(Globals.sportTagName, 5, false, Globals.sportTag));
        tagList.add(new TagItem(Globals.jacketTagName, 6, false, Globals.jacketTag));
        tagList.add(new TagItem(Globals.shortsTagName, 7, false, Globals.shortsTag));
    }

    private void setUpConnectionToBackgroundService(){
        backgroundServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                backgroundService = ((BackgroundService.BackgroundServiceBinder)service).getService();
                Log.d("From service", "onServiceConnected: Connected");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                backgroundService = null;
                Log.d("From service", "onServiceDisconnected: disconnected");
            }
        };
    }

    void bindToBackgroundService() {
        bindService(new Intent(UserActivity.this,
                BackgroundService.class), backgroundServiceConnection, Context.BIND_AUTO_CREATE);
        serviceBound = true;
    }

    void unBindFromBackgroundService() {
        if (serviceBound) {
            // Detach our existing connection.
            unbindService(backgroundServiceConnection);
            serviceBound = false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(onUserPreferencesRecieved, new IntentFilter(Globals.userPreferencesBroadCast));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unBindFromBackgroundService();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onUserPreferencesRecieved);
    }

    private BroadcastReceiver onUserPreferencesRecieved = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("broadcast", "Broadcast recived, userpreferences updated");
            //String message = intent.getStringExtra(Globals.cardSwipeMessage);

            UserPreferences userPreferences = backgroundService.getUserPreferences();
            if (userPreferences!=null){
                pickerAge.setValue(userPreferences.getAge());
                Log.d("Age", "onReceive: Age set");
                if (userPreferences.getSex()!=null){
                    Log.d("get Sex", "onReceive: " + userPreferences.getSex());
                }
                if (userPreferences.getSex().equalsIgnoreCase(Globals.female)){
                    Log.d("radiobutton", "onReceive: trying to set radiobutton female");
                    radioGroup.check(R.id.radioButtonWoman);
                    //rbWoman.setChecked(true);
                }
                if (userPreferences.getSex().equalsIgnoreCase(Globals.male)){
                    //rbMan.setChecked(true);
                    Log.d("radiobutton", "onReceive: trying to set radiobutton male");
                    radioGroup.check(R.id.radioButtonMan);
                }
            }
            progressDialog.dismiss();
        }
    };
}
