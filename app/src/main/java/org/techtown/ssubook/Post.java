package org.techtown.ssubook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
public class Post extends AppCompatActivity
{
    private RecyclerView feedRecyclerView;
    private RecyclerView.Adapter feedAdapter;
    private RecyclerView.LayoutManager feedManager;
    private ActionBar feedActionbar;
    private SwipeRefreshLayout swipeLayout;
    private FloatingActionButton floatingBtn;
    ArrayList<BookItem> bookItemBundle = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        setTitle("My post");

        swipeLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_layout);
        feedRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_feed);
        floatingBtn = (FloatingActionButton) findViewById(R.id.floating_feed_btn);
        feedActionbar = getSupportActionBar();

        FirebaseFirestore firebaseDB = FirebaseFirestore.getInstance();
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null)
        {
            //DB에서 별명 가져오기
            firebaseDB.collection("User").document(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task)
                {
                    if(task.isSuccessful())
                    {
                        DocumentSnapshot document = task.getResult();
                        if(document.exists())
                        {
                            String nickname = document.getData().get("nickname").toString();
                            feedActionbar.setTitle( (nickname) + "회원님");
                        }
                        else
                        {
                            if(currentUser.getDisplayName()!=null)
                                feedActionbar.setTitle( (currentUser.getDisplayName()) + "회원님");
                            else
                                feedActionbar.setTitle("회원님");
                        }
                    }
                    else
                    {
                        if(currentUser.getDisplayName()!=null)
                            feedActionbar.setTitle( (currentUser.getDisplayName()) + "회원님");
                        else
                            feedActionbar.setTitle("회원님");
                    }
                }
            });
        }
        //FireStore 데이터 읽어오기
        //FirebaseFirestore firebaseDB = FirebaseFirestore.getInstance();
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
                        int price = Integer.parseInt(dataMap.get("price").toString());  //가격
                        long timeStamp = Long.parseLong(dataMap.get("timeStamp").toString());


                        //책 상태
                        String underbarTrace = dataMap.get("underbarTrace").toString();   //NONE, PENCIL, PEN
                        String writeTrace = dataMap.get("writeTrace").toString();  //NONE, PENCIL, PEN
                        String bookCover = dataMap.get("bookCover").toString(); //CLEAN, DIRTY
                        boolean naming = (boolean)dataMap.get("naming"); //true:이름있음, false:없음
                        boolean discolor = (boolean)dataMap.get("discolor");   //변색, true:있음, false:없음
                        String imageURL = dataMap.get("imageURL").toString();
                        if(author.equals(currentUser.getUid()))//currentUser.getUid()
                            bookItemBundle.add(new BookItem(title,author,UID,price,timeStamp,underbarTrace,writeTrace,bookCover,naming,discolor,imageURL));
                        Log.i("Feed","Data Added, title : "+title);
                    }
                    Collections.sort(bookItemBundle);   //TimeStamp를 사용해 최신순 정렬
                    feedAdapter.notifyDataSetChanged();
                }
                else
                {
                    //작업 실패 시
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


        feedRecyclerView.setHasFixedSize(true);
        feedManager = new LinearLayoutManager(this);

        feedRecyclerView.setLayoutManager(feedManager);
        feedAdapter = new FeedAdapter(bookItemBundle);
        feedRecyclerView.setAdapter(feedAdapter);


        feedRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( getApplicationContext(), "Log Out 성공", Toast.LENGTH_LONG ).show();
            }
        });
        /*
        feedAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( getApplicationContext(), "Log Out 성공", Toast.LENGTH_LONG ).show();
            }
        });
*/


        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                //데이터 읽어오기
                FirebaseFirestore firebaseDB = FirebaseFirestore.getInstance();
                firebaseDB.collection("Post").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if (task.isSuccessful())
                        {
                            bookItemBundle.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) //Task 종료 시 getResult는 QuerySnapShot을 return, QuerySnapShot은 Iterable이므로 for-each 문으로 QueryDocumentSnapshot으로 사용가능.
                            {
                                //QueryDocumentSnapshot은 모두 document형, getData()로 Map<String,Object>를 return
                                Map<String,Object> dataMap = document.getData();
                                String title = dataMap.get("title").toString();   //제목
                                String author = dataMap.get("author").toString();  //use firebase UID, 저자
                                String UID = dataMap.get("UID").toString(); //게시글 UID
                                int price = Integer.parseInt(dataMap.get("price").toString());  //가격
                                long timeStamp = Long.parseLong(dataMap.get("timeStamp").toString());


                                String underbarTrace = dataMap.get("underbarTrace").toString();   //NONE, PENCIL, PEN
                                String writeTrace = dataMap.get("writeTrace").toString();  //NONE, PENCIL, PEN
                                String bookCover = dataMap.get("bookCover").toString(); //CLEAN, DIRTY
                                boolean naming = (boolean)dataMap.get("naming"); //true:이름있음, false:없음
                                boolean discolor = (boolean)dataMap.get("discolor");   //변색, true:있음, false:없음
                                String imageURL = dataMap.get("imageURL").toString();
                                bookItemBundle.add(new BookItem(title,author,UID,price,timeStamp,underbarTrace,writeTrace,bookCover,naming,discolor,imageURL));
                                Log.i("Feed","Data Added, title : "+title);
                            }
                            Collections.sort(bookItemBundle);   //TimeStamp를 사용해 최신순 정렬
                            feedAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            //작업 실패 시
                        }
                    }
                });
                swipeLayout.setRefreshing(false);
            }
        });

        floatingBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Post.this, WritePost.class);
                startActivity(intent);
            }
        });

        floatingBtn.hide();

    }
}


/*
public class Post extends AppCompatActivity {
    private RecyclerView feedRecyclerView;
    private RecyclerView.Adapter feedAdapter;
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
                    feedAdapter.notifyDataSetChanged();
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
*/
/*
//userID를 찾는 과정....
//ArrayList<String> interested_post = new ArrayList<>();
//ArrayList<Integer> finds= find_interested_post(booklist, interested_post);



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
 */
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