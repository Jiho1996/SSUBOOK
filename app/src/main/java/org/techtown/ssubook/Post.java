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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;


public class Post extends AppCompatActivity {
    @Override

    BookItem[] itemlist;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        setTitle("Post");
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
                        //QueryDocumentSnapshot은 모두 document형, getData()로 Map<String,Object>를 return
                        Map<String,Object> dataMap = document.getData();
                        String title = dataMap.get("title").toString();   //제목
                        String author = dataMap.get("author").toString();  //use firebase UID, 저자
                        String UID = dataMap.get("UID").toString(); //게시글 UID
                        int price = (int)dataMap.get("price");  //가격
                        long timeStamp = (long)dataMap.get("timeStamp");  //올린시간

                        //책 상태
                        String underbarTrace = dataMap.get("underbarTrace").toString();   //NONE, PENCIL, PEN
                        String writeTrace = dataMap.get("writeTrace").toString();  //NONE, PENCIL, PEN
                        String bookCover = dataMap.get("bookCover").toString(); //CLEAN, DIRTY
                        boolean naming = (boolean)dataMap.get("naming"); //true:이름있음, false:없음
                        boolean discolor = (boolean)dataMap.get("discolor");   //변색, true:있음, false:없음
                        itemlist=new BookItem(title,author,UID,price,timeStamp,underbarTrace,writeTrace,bookCover,naming,discolor);
                    }
                }
                else
                {
                    //작업 실패 시
                }
            }
        });

    }

}