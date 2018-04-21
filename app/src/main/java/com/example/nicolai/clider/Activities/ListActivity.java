package com.example.nicolai.clider.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.nicolai.clider.Adapters.ClotheAdapter;
import com.example.nicolai.clider.R;
import com.example.nicolai.clider.model.Clothe;
import com.example.nicolai.clider.model.ClotheCardView;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    ListView clothesListView;
    ArrayList<Clothe> arrayList;
    ClotheAdapter clotheAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        clothesListView = findViewById(R.id.clothesListView);
        arrayList = new ArrayList<Clothe>(ClotheCardView.getArrayList());
        clotheAdapter = new ClotheAdapter(this, arrayList);
        clothesListView.setAdapter(clotheAdapter);
    }
}
