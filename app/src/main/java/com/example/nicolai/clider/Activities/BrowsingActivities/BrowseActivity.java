package com.example.nicolai.clider.Activities.BrowsingActivities;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Point;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.nicolai.clider.Activities.ListActivity;
import com.example.nicolai.clider.R;
import com.example.nicolai.clider.Services.BackgroundService;
import com.example.nicolai.clider.Utils.Globals;
import com.example.nicolai.clider.Utils.Utils;
import com.example.nicolai.clider.model.Clothe;
import com.example.nicolai.clider.model.ClotheCardView;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

import java.util.List;

public class BrowseActivity extends AppCompatActivity {

    private SwipePlaceHolderView mSwipeView;
    private Context mContext;
    private Button toList;
    private boolean serviceBound;

    private BackgroundService backgroundService;
    private ServiceConnection backgroundServiceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse1);
        toList = findViewById(R.id.toListButton);

        mSwipeView = (SwipePlaceHolderView)findViewById(R.id.swipeView);
        mContext = getApplicationContext();
        setUpConnectionToBackgroundService();
        bindToBackgroundService();



        toList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                startActivity(intent);
            }
        });

    }

    private void buildSwipeView() {
        int bottomMargin = Utils.dpToPx(160);
        Point windowSize = Utils.getDisplaySize(getWindowManager());
        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setIsUndoEnabled(true)
                .setHeightSwipeDistFactor(10)
                .setWidthSwipeDistFactor(5)
                .setSwipeDecor(new SwipeDecor()
                        .setViewWidth(windowSize.x)
                        .setViewHeight(windowSize.y - bottomMargin)
                        .setViewGravity(Gravity.TOP)
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeMaxChangeAngle(2f)
                        .setSwipeInMsgLayoutId(R.layout.swipe_in_msg)
                        .setSwipeOutMsgLayoutId(R.layout.swipe_out_msg));

        List<Clothe> clothes = backgroundService.getClotheByPreferences();
        for(Clothe clothe : clothes){
            mSwipeView.addView(new ClotheCardView(clothe, mContext, mSwipeView));
        }
    }

    private void setUpConnectionToBackgroundService(){
        backgroundServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                backgroundService = ((BackgroundService.BackgroundServiceBinder)service).getService();
                buildSwipeView();
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
        bindService(new Intent(BrowseActivity.this,
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
    private BroadcastReceiver onCardSwipeResult = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("broadcast", "Broadcast reveiced from card");
            //String message = intent.getStringExtra(Globals.cardSwipeMessage);
            Clothe clothe = (Clothe) intent.getSerializableExtra(Globals.cardSwipeMessage);
            backgroundService.addClothe(clothe);

        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(onCardSwipeResult, new IntentFilter(Globals.clotheBroadcast));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unBindFromBackgroundService();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onCardSwipeResult);
    }
}
