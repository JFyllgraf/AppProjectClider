package com.example.nicolai.clider.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.nicolai.clider.Utils.Globals;
import com.example.nicolai.clider.Utils.Utils;
import com.example.nicolai.clider.model.Clothe;
import com.example.nicolai.clider.model.UserPreferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BackgroundService extends Service {
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    UserPreferences mUserPreferences;

    public BackgroundService() {
    }

    public ArrayList<Clothe> getBackgroundServiceClotheList() {
        return backgroundServiceClotheList;
    }

    public ArrayList<Clothe> backgroundServiceClotheList = new ArrayList<>();

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class BackgroundServiceBinder extends Binder {
        public BackgroundService getService() {return BackgroundService.this;}
    }
    private final IBinder binder = new BackgroundServiceBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference  = FirebaseDatabase.getInstance().getReference();
        retriveUserPreferences();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void retriveUserPreferences(){
        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUserPreferences = dataSnapshot.getValue(UserPreferences.class);
                //Log.d("user info", "onDataChange: " + mUserPreferences.getAge());
                broadcastUserPreferences();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void addClothe(Clothe clothe){
        backgroundServiceClotheList.add(clothe);
        Log.d("From service", "addClothe: added");
        Log.d("List size", "size: " + backgroundServiceClotheList.size());
        Log.d("clothe ID", "addClothe: " + clothe.getId());
     }

     public List<Clothe> getAllClothes(){
        return Utils.loadClothes(this.getApplicationContext());
     }

     public void saveUserInfo(UserPreferences userPreferences){
         Log.d("User stored", "saveUserInfo: ");
        FirebaseUser user = firebaseAuth.getCurrentUser();
         Log.d("User", "saveUserInfo: " + user.getEmail());
        databaseReference.child(user.getUid()).setValue(userPreferences);
     }

    public UserPreferences getUserPreferences(){
        return mUserPreferences;
    }

    private void broadcastUserPreferences(){
        Log.d("Sender", "broadcast userpreferences: ");
        Intent broadcastIntent = new Intent(Globals.userPreferencesBroadCast);
        broadcastIntent.putExtra(Globals.userPreferencesUpdated, Globals.userPreferencesUpdated);

        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }


}
