package org.techtown.ssubook;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class search extends AppCompatActivity {
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
/*
        ActionBar ab = getSupportActionBar();
       ab.setTitle("검색");
*/
        ListView listView = findViewById(R.id.search_list);
        List<String> mylist = new ArrayList<>(); //검색어 예시를 담을 배열

        //데이터 베이스 관리에 따라 가능하면 이를 서버에서 그때끄때 불러올 수 있게 만들 예정.
        mylist.add("현대인과 성서");
        mylist.add("창의적 사고와 글쓰기");
        mylist.add("Probability & Statistics");
        mylist.add("미분적분학");

        //검색어들 예시를 보여준다.
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mylist);
        listView.setAdapter(arrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.item_search);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("책 제목이나 게시글 제목을 입력하세요");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { // 검색어 입력.
                Intent intent = new Intent();
                ComponentName componentName = new ComponentName (
                        "com.example.soongsilbook",
                        "com.example.soongsilbook.searched_post");
                intent.setComponent(componentName);
                intent.putExtra("title", query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) { //검색어 입력시
                arrayAdapter.getFilter().filter(newText);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}