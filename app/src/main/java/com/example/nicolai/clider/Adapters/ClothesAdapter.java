package com.example.nicolai.clider.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.example.nicolai.clider.R;

import java.util.ArrayList;

/**
 * Created by Nicolai on 11/04/2018.
 */

public class ClothesAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> clothes;
    String cloth;

    public ClothesAdapter(Context context, ArrayList<String> clothes){
        this.context = context;
        this.clothes = clothes;
    }

    @Override
    public int getCount() {
        if (clothes!=null){
            return clothes.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (clothes!=null){
            return clothes.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view==null){
            LayoutInflater clothesInflator = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = clothesInflator.inflate(R.layout.fragment_item, null);
        }
        cloth = clothes.get(i);
        if (cloth!=null){
            CheckBox checkBox = view.findViewById(R.id.clothes_checkBox);
            checkBox.setText(cloth);
        } return view;
    }
}
