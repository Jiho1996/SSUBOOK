package org.techtown.ssubook;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.Toast;


public class Post extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
/*
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Post");
*/
        ListView listview ;
        ListView listview2 ;
        ListViewAdapter adapter;
        ListViewAdapter adapter2;

        // Adapter 생성
        adapter = new ListViewAdapter() ;
        adapter2= new ListViewAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.MyPostTab);
        listview.setAdapter(adapter);

        //첫 번째 텝 만들기.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.ic_baseline_assignment_24),
                "1",
                "내 글1") ; // 첫 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.ic_baseline_perm_identity_24),
                "2",
                "내 글2") ;
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.ic_baseline_search_24),
                "3",
                "내 글3");

        //두 번째 텝 만들기.
        listview2 = (ListView) findViewById(R.id.Intested_Tab);
        listview2.setAdapter(adapter2);
        adapter2.addItem(ContextCompat.getDrawable(this, R.drawable.ic_baseline_assignment_24),
                "1",
                "관심있는 글1") ;
        adapter2.addItem(ContextCompat.getDrawable(this, R.drawable.ic_baseline_perm_identity_24),
                "2",
                "관심있는 글2") ;
        adapter2.addItem(ContextCompat.getDrawable(this, R.drawable.ic_baseline_search_24),
                "3",
                "관심있는 글3");

        TabHost tabHost = findViewById(R.id.host);
        tabHost.setup();

        TabHost.TabSpec spec = tabHost.newTabSpec("My post");
        spec.setIndicator(null, ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_stars_24, null));
        spec.setContent(R.id.MyPostTab);
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("Interested post");
        spec.setIndicator(null, ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_assignment_24, null));
        spec.setContent(R.id.Intested_Tab);
        tabHost.addTab(spec);

        Intent intent = getIntent(); //마이 페이지에서 이 액티비티에 접근할 때 interested tab으로 보내주기 위해서.
        int action = intent.getIntExtra("activity", 0);
        if(action==1) {
            listview2 = (ListView) findViewById(R.id.Intested_Tab);
            tabHost.setCurrentTab(1);
        }
    }
}