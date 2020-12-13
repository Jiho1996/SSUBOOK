package org.techtown.ssubook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class searched_post extends AppCompatActivity
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
        setTitle("검색 결과");

        Intent intent = getIntent();
        final String searched_title = intent.getStringExtra("title");
        Toast.makeText(this, searched_title, Toast.LENGTH_LONG).show();

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
        //FirebaseFirestore firebaseDB = FirebaseFirestore.getInstance();
        firebaseDB.collection("Post").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                if (task.isSuccessful())
                {
                    for (QueryDocumentSnapshot document : task.getResult())
                    {
                        Map<String,Object> dataMap = document.getData();
                        String title = dataMap.get("title").toString();
                        String author = dataMap.get("author").toString();
                        String UID = dataMap.get("UID").toString();
                        int price = Integer.parseInt(dataMap.get("price").toString());
                        long timeStamp = Long.parseLong(dataMap.get("timeStamp").toString());

                        String underbarTrace = dataMap.get("underbarTrace").toString();
                        String writeTrace = dataMap.get("writeTrace").toString();
                        String bookCover = dataMap.get("bookCover").toString();
                        boolean naming = (boolean)dataMap.get("naming");
                        boolean discolor = (boolean)dataMap.get("discolor");
                        String imageURL = dataMap.get("imageURL").toString();
                        if(find_by_title(title, searched_title))//searched_title.equals(title)
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


                                //책 상태
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
                Intent intent = new Intent(searched_post.this, WritePost.class);
                startActivity(intent);
            }
        });


    }
    private Boolean find_by_title(String title, String searched_title) {
        for (int i = 0; i <= title.length() - searched_title.length(); i++)
            if (searched_title.equals(title.substring(i, i + searched_title.length())))
                return true;
        return false;
}
}



/*
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class searched_post extends AppCompatActivity {
    private ArrayList<BookItem> booklist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched_post);
        setTitle("검색 결과");
        String title;

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        Toast.makeText(this, title, Toast.LENGTH_LONG).show();

        ListView listview;
        ListViewAdapter adapter;
        adapter = new ListViewAdapter();
        listview = (ListView) findViewById(R.id.searched_post);
        listview.setAdapter(adapter);

        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.ic_baseline_assignment_24),
                "1",
                "검색 글1"); // 첫 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.ic_baseline_perm_identity_24),
                "2",
                "검색 글2"); // 두 번째 아이템 추가.

    }


        private int[] search(User[] users, String title){
            int[] post= new int[100];
            int position=0;
            for(int i=0; i<users.length; i++)
                if(match_string(title,users[i].title)==true)
                    post[position]=i;
            return post;
        }


    private boolean match_string(String a, String b) {
        for (int i = 0; i <= a.length() - b.length(); i++)
            if (b.equals(a.substring(i, i + b.length())))
                return true;

        return false;
    }

    private ArrayList<Integer> find_by_title(ArrayList<BookItem> booklist, String title) {
        ArrayList<Integer> finds = new ArrayList();
        for (int i = 0; i < booklist.size(); i++) {
            Object obj = booklist.get(i);
            BookItem book = (BookItem) obj;
            if (match_string(book.getTitle(), title) == true)
                finds.add(i);
        }
        return finds;
    }
}



//정보 불러오기. (안 쓰는 거. cloud에 저장해서 이거 안씀.... )

        FirebaseDatabase database = FirebaseDatabase.getInstance(); //database는 firebase의 root라 생각.
        DatabaseReference myRef = database.getReference("Post/0");

        final User[] users= new User[100];
        final int[] position = {0};

        myRef.addListenerForSingleValueEvent( //한 번만 데이터를 읽어오는 함수로 알고 썼습니다.
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {




                        //firebase를 만들지 않았어서 시행착오를 확인할 방법이 없어서 여러 개 만들어 놨습니다.

                        User user = dataSnapshot.getValue(User.class);
                        users[position[0]++]=user;




                        User[] user = new User[100];
                        int pos=0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            user[pos++]=(User)dataSnapshot.getValue(); //없으면 null 리턴.
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }


                }
        );
        */