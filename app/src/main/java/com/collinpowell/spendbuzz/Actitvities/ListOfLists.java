package com.collinpowell.spendbuzz.Actitvities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.collinpowell.spendbuzz.Adapters.ListRecyclerViewAdapter;
import com.collinpowell.spendbuzz.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ListOfLists extends AppCompatActivity {

    RecyclerView listsRecyclerView;
    FloatingActionButton floatingActionButton;
    ListRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_lists);

        listsRecyclerView = findViewById(R.id.list_recycler_view);
        floatingActionButton = findViewById(R.id.floating_action);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MarketList.class));
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        listsRecyclerView.setHasFixedSize(true);
        listsRecyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ListRecyclerViewAdapter(10);
        listsRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ListRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                startActivity(new Intent(getApplicationContext(), MarketList.class));
            }
        });


    }
}