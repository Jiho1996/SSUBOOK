package org.techtown.ssubook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class Post extends AppCompatActivity {
    ArrayList<BookItem> booklist = new ArrayList<>();
    BookItem book;
    String userID = "123";
    List<String> titles = new ArrayList<>();
    private ActionBar feedActionbar;
    ArrayList<ListViewItem> book_list = new ArrayList<>();
   Drawable drawable;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        setTitle("Post");
        drawable = getResources().getDrawable(R.drawable.ic_baseline_assignment_24);

        ListView listview;
        ListView listview2;
        final ListViewAdapter adapter;
        final ListViewAdapter adapter2;
        adapter = new ListViewAdapter();
        adapter2 = new ListViewAdapter();
        listview = (ListView) findViewById(R.id.MyPostTab);
        listview.setAdapter(adapter);
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.ic_baseline_assignment_24),
                "1",
                "내 글1"); // 첫 번째 아이템 추가.
        listview2 = (ListView) findViewById(R.id.Intested_Tab);
        listview2.setAdapter(adapter2);
        adapter2.addItem(ContextCompat.getDrawable(this, R.drawable.ic_baseline_assignment_24),
                "1",
                "관심있는 글1");
        adapter2.addItem(ContextCompat.getDrawable(this, R.drawable.ic_baseline_perm_identity_24),
                "2",
                "관심있는 글2");



        FirebaseFirestore firebaseDB = FirebaseFirestore.getInstance();
        firebaseDB.collection("Post").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                if (task.isSuccessful())
                {
                    for (QueryDocumentSnapshot document : task.getResult()) //Task 종료 시 getResult는 QuerySnapShot을 return, QuerySnapShot은 Iterable이므로 for-each 문으로 QueryDocumentSnapshot으로 사용가능.
                    {
                        Map<String,Object> dataMap = document.getData();
                        String title = dataMap.get("title").toString();   //제목
                        String author = dataMap.get("author").toString();  //use firebase UID, 저자
                        String UID = dataMap.get("UID").toString(); //게시글 UID
                        int price = Integer.parseInt(dataMap.get("price").toString());  //가격
                        book_list.add(new ListViewItem(drawable,title,author));
                        Log.i("Post","Data Added, title : "+book_list.get(0).getTitle());
                    }
                    adapter.notifyDataSetChanged();
                    adapter2.notifyDataSetChanged();
                }
                else
                {
                    Log.w("Feed","Feed Get FAILED");
                }
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Log.w("Feed","Feed Get FAILED",e);
            }
        });

        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.ic_baseline_assignment_24),
                book_list.get(0).getTitle(),
                book_list.get(0).getDesc()) ;



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

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) { //2번째: 클릭된 아이템 뷰, 세번째: 아이템 위치

                //ListViewItem obj = (ListViewItem) paramAdapterView.getAdapter().getItem(paramInt);
                //String title=find_by_title(booklist,obj.getTitle());
                Intent to_post = new Intent();
                ComponentName componentName = new ComponentName(
                        "org.techtown.ssubook",
                        "org.techtown.ssubook.Mypage");
                to_post.setComponent(componentName);
                //to_post.putExtra("BookID", title);
                startActivity(to_post);

            }
        });

        Intent intent = getIntent(); //마이 페이지에서 이 액티비티에 접근할 때 interested tab으로 보내주기 위해서.
        int action = intent.getIntExtra("activity", 0);
        if (action == 1) {
            listview2 = (ListView) findViewById(R.id.Intested_Tab);
            tabHost.setCurrentTab(1);
        }
    }

}
