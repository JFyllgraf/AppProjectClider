package com.example.nicolai.clider.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.nicolai.clider.Utils.Utils;
import com.example.nicolai.clider.model.Clothe;

import java.util.ArrayList;
import java.util.List;

public class BackgroundService extends Service {
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
}
