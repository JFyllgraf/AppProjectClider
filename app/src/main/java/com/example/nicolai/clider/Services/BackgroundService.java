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
    ArrayList<String> mLikeClotheIds = new ArrayList<>();
    List<String> clothesIds;

    public BackgroundService() {
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
        retriveLikedClothesIds();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void retriveUserPreferences(){
        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("userPreferences").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUserPreferences = dataSnapshot.getValue(UserPreferences.class);
                broadcastUserPreferences();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void retriveLikedClothesIds(){
        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("clotheIds").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                clothesIds = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Log.d("clothesIDS", "onDataChange: from" + snapshot.getValue().toString());

                        clothesIds.add(snapshot.getValue().toString());
                        Log.d("clothesIDS", "onDataChange: from");

                }
                broadcastSwipedClothe();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addClothe(Clothe clothe){

        backgroundServiceClotheList.add(clothe);

        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference.child(user.getUid()).child("clotheIds").push().setValue(clothe.getId().toString());
        Log.d("From service", "addClothe: added");
        Log.d("List size", "size: " + backgroundServiceClotheList.size());
        Log.d("clothe ID", "addClothe: " + clothe.getId());
        Log.d("Liked Items size", "addClothe: " + clothesIds.size());
     }

     public List<Clothe> getAllClothes(){
        return Utils.loadClothes(this.getApplicationContext());
     }

     public List<Clothe> getClotheByPreferences(){
        List<Clothe> allClothes = getAllClothes();
        List<Clothe> filteredClothes = new ArrayList<>();
         for (Clothe clothe: allClothes) {
             if (mUserPreferences.getTags().contains(clothe.getTag())){
                 filteredClothes.add(clothe);
             }
         }
         return filteredClothes;
     }

     public void saveUserInfo(UserPreferences userPreferences){
         Log.d("User stored", "saveUserInfo: ");
        FirebaseUser user = firebaseAuth.getCurrentUser();
         Log.d("User", "saveUserInfo: " + user.getEmail());
        databaseReference.child(user.getUid()).child("userPreferences").setValue(userPreferences);
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

    private void broadcastSwipedClothe(){
        Log.d("Sender", "broadcast swipedClothe: ");
        Intent broadcastIntent = new Intent(Globals.swipedClotheBroadcast);
        broadcastIntent.putExtra(Globals.swipedClotheUpdated, Globals.swipedClotheUpdated);

        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }

    public ArrayList<String> getmLikeClotheIds() {
        return mLikeClotheIds;
    }

    public ArrayList<Clothe>getLikedCloth(){
        List<Clothe> allClothes = getAllClothes();
        ArrayList<Clothe> likedClothes = new ArrayList<>();
        for (Clothe clothe: allClothes) {
            if (clothesIds.contains(clothe.getId().toString())){
                likedClothes.add(clothe);
            }
        }
        return likedClothes;
    }
}
