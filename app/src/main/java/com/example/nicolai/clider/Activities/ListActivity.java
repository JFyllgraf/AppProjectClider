package com.example.nicolai.clider.Activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.nicolai.clider.Activities.BrowsingActivities.BrowseActivity;
import com.example.nicolai.clider.Adapters.ClotheAdapter;
import com.example.nicolai.clider.R;
import com.example.nicolai.clider.Services.BackgroundService;
import com.example.nicolai.clider.model.Clothe;
import com.example.nicolai.clider.model.ClotheCardView;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    ListView clothesListView;
    ArrayList<Clothe> arrayList;
    ClotheAdapter clotheAdapter;
    private BackgroundService backgroundService;
    private ServiceConnection backgroundServiceConnection;
    boolean serviceBound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        clothesListView = findViewById(R.id.clothesListView);

        setUpConnectionToBackgroundService();
        bindToBackgroundService();
    }

    void bindToBackgroundService() {
        bindService(new Intent(ListActivity.this,
                BackgroundService.class), backgroundServiceConnection, Context.BIND_AUTO_CREATE);
        serviceBound = true;
    }

    private void setUpConnectionToBackgroundService(){
        backgroundServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                backgroundService = ((BackgroundService.BackgroundServiceBinder)service).getService();
                Log.d("From service", "onServiceConnected: Connected");
                arrayList = new ArrayList<Clothe>(backgroundService.getBackgroundServiceClotheList());
                clotheAdapter = new ClotheAdapter(getApplicationContext(), arrayList);
                clothesListView.setAdapter(clotheAdapter);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                backgroundService = null;
                Log.d("From service", "onServiceDisconnected: disconnected");
            }
        };
    }
}
