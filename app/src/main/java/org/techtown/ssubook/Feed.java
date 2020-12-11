package org.techtown.ssubook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.ViewGroup;

import java.util.ArrayList;

public class Feed extends AppCompatActivity
{
    private RecyclerView feedRecyclerView;
    private FeedAdapter feedAdapter;
    private RecyclerView.LayoutManager feedManager;

    ArrayList<FeedItem> feedItemBundle = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        feedRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_feed);
        feedRecyclerView.setHasFixedSize(true);
        feedManager = new LinearLayoutManager(this);

        feedRecyclerView.setLayoutManager(feedManager);
        //feedAdapter = new FeedAdapter();
        feedRecyclerView.setAdapter(feedAdapter);


    }
}

