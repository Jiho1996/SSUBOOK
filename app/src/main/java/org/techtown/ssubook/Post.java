package org.techtown.ssubook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.content.ComponentName;
import android.content.Intent;
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
import java.util.List;
import java.util.Map;


public class Post extends AppCompatActivity {
    List<BookItem> booklist = new ArrayList<>();
    BookItem book;
    String userID = "123";
    List<String> titles = new ArrayList<>();
    private ActionBar feedActionbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        setTitle("Post");

        ListView listview;
        ListView listview2;
        final ListViewAdapter adapter;
        ListViewAdapter adapter2;
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
        firebaseDB.collection("Post").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> dataMap = document.getData();
                        String title = dataMap.get("title").toString();
                        String author = dataMap.get("author").toString();
                        String UID = dataMap.get("UID").toString();
                        int price = Integer.parseInt(String.valueOf(dataMap.get("price")));

                        Object timeStamp_o = dataMap.get("timeStamp");
                        long timeStamp = ((Timestamp) timeStamp_o).getSeconds();
                        BookItem book = new BookItem(title, author, UID, price, timeStamp);
                        booklist.add(book);
                        Log.d("add", UID + title + author);
                    }
                   // adapter.notifyDataSetChanged();
                } else {

                }
            }
        });


        /*
        ArrayList<Integer> finds= find_my_post(booklist, userID);

        for(int i=0; i<finds.size(); i++){
            Object obj = booklist.get(i);
            BookItem book= (BookItem)obj;

            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.ic_baseline_assignment_24),
                    book.getTitle(),
                    book.getContents()) ;
        }
*/
        //userID를 찾는 과정....
        //ArrayList<String> interested_post = new ArrayList<>();
        //ArrayList<Integer> finds= find_interested_post(booklist, interested_post);


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


    private ArrayList<Integer> find_my_post(ArrayList<BookItem> booklist, String UID) {
        ArrayList<Integer> finds = new ArrayList();
        for (int i = 0; i < booklist.size(); i++) {
            Object obj = booklist.get(i);
            BookItem book = (BookItem) obj;
            if (book.getUID().equals(UID))
                finds.add(i);
        }
        return finds;
    }
/*
    private ArrayList<Integer> find_interested_post(ArrayList<BookItem> booklist, ArrayList<String> interested_post){ //게시글에 고유 번호 있다는 전제.
        ArrayList<Integer> finds= new ArrayList();
        for(int i=0; i<interested_post.size(); i++) {
            Object obj = interested_post.get(i);
            String BookId=(String)obj;
            for (int j = 0; j < booklist.size(); j++) {
                Object obj2 = booklist.get(j);
                BookItem book= (BookItem)obj2;
                if(book.getBookID().equals(BookId))
                    finds.add(j);
            }
            obj=null;
            BookId=null;
        }
        return finds;
    }

    private String find_by_title(ArrayList<BookItem> booklist, String title){
        for(int i=0; i<booklist.size(); i++){
            Object obj = booklist.get(i);
            BookItem book= (BookItem)obj;
            if(book.getTitle().equals(title))
                return book.getTitle();
        }
        return null;
    }
*/

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("add", Integer.toString(booklist.size()));
        Log.d("add", booklist.get(0).getAuthor());
    }
}
