package com.example.nicolai.clider.model;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.example.nicolai.clider.R;
import com.example.nicolai.clider.Utils.Globals;
import com.example.nicolai.clider.Utils.Utils;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeHead;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;
import com.mindorks.placeholderview.annotations.swipe.SwipeView;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

@Layout(R.layout.clothes_view)
public class ClotheCardView {

    @View(R.id.clotheImage)
    private ImageView clotheImage;

    @View(R.id.clotheType)
    private TextView clotheType;

    @View(R.id.descriptionDetails)
    private TextView descriptionDetails;

    @SwipeView
    private android.view.View cardView;

    private Clothe mClothe;
    private Context mContext;
    private SwipePlaceHolderView mSwipeView;
    public static ArrayList<Clothe> arrayList;

    public static ArrayList<Clothe> getArrayList() {
        return arrayList;
    }

    public ClotheCardView(Clothe clothe, Context mContext, SwipePlaceHolderView mSwipeView) {
        this.mClothe = clothe;
        this.mContext = mContext;
        this.mSwipeView = mSwipeView;
        arrayList = new ArrayList<>();
    }

    @Resolve
    private void onResolved(){
        MultiTransformation multi = new MultiTransformation(
                new BlurTransformation(mContext, 30),
                new RoundedCornersTransformation(
                        mContext, Utils.dpToPx(7), 0,
                        RoundedCornersTransformation.CornerType.TOP));

        Glide.with(mContext).load(mClothe.getImageUrl())
                .bitmapTransform(multi)
                .into(clotheImage);
        clotheType.setText(mClothe.getName());
        descriptionDetails.setText(mClothe.getLocation());
    }

    @SwipeHead
    private void onSwipeHeadCard() {
        Glide.with(mContext).load(mClothe.getImageUrl())
                .bitmapTransform(new RoundedCornersTransformation(
                        mContext, Utils.dpToPx(7), 0,
                        RoundedCornersTransformation.CornerType.TOP))
                .into(clotheImage);
        cardView.invalidate();
    }

    @Click(R.id.clotheImage)
    private void onClick(){
        Log.d("EVENT", "profileImageView click");
//        mSwipeView.addView(this);
    }

    @SwipeOut
    private void onSwipedOut(){
        Log.d("EVENT", "onSwipedOut");
//        mSwipeView.addView(this);
    }

    @SwipeCancelState
    private void onSwipeCancelState(){
        Log.d("EVENT", "onSwipeCancelState");
    }

    @SwipeIn
    private void onSwipeIn(){
        arrayList.add(mClothe);
        broadcastClothe(mClothe);
        Log.d("EVENT", "onSwipedIn");
    }

    @SwipeInState
    private void onSwipeInState(){
        Log.d("EVENT", "onSwipeInState");
    }

    @SwipeOutState
    private void onSwipeOutState(){
        Log.d("EVENT", "onSwipeOutState");
    }

    private void broadcastClothe(Clothe clothe){
        Log.d("Sender", "broadcastClothe: ");
        Intent broadcastIntent = new Intent(Globals.clotheBroadcast);
        broadcastIntent.putExtra(Globals.cardSwipeMessage, clothe);
        //broadcastIntent.setAction(BROADCAST_BACKGROUND_SERVICE_RESULT);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(broadcastIntent);
    }

}
