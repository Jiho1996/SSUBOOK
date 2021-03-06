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

public class Post2 extends AppCompatActivity {
    private RecyclerView feedRecyclerView;
    private RecyclerView.Adapter feedAdapter;
    private RecyclerView.LayoutManager feedManager;
    private ActionBar feedActionbar;
    private SwipeRefreshLayout swipeLayout;
    private FloatingActionButton floatingBtn;
    ArrayList<BookItem> bookItemBundle = new ArrayList<>();
    String interested2;
    List<String> interested;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        setTitle("Interested post");

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
                            interested = (ArrayList<String>) document.getData().get("interested_post");
                        }
                        else
                        {
                           // if(currentUser.getDisplayName()!=null)
                        }
                    }
                    else
                    {

                    }
                }
            });
        }
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
                        for(int i=0; i<interested.size(); i++)
                            if(UID.equals(interested.get(i)))//currentUser.getUid()
                                bookItemBundle.add(new BookItem(title,author,UID,price,timeStamp,underbarTrace,writeTrace,bookCover,naming,discolor,imageURL));
                        Log.i("Feed","Data Added, title : "+title);
                    }
                    Collections.sort(bookItemBundle);   //TimeStamp를 사용해 최신순 정렬
                    feedAdapter.notifyDataSetChanged();
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
                                Map<String,Object> dataMap = document.getData();
                                String title = dataMap.get("title").toString();   //제목
                                String author = dataMap.get("author").toString();  //use firebase UID, 저자
                                String UID = dataMap.get("UID").toString(); //게시글 UID
                                int price = Integer.parseInt(dataMap.get("price").toString());  //가격
                                long timeStamp = Long.parseLong(dataMap.get("timeStamp").toString());


                                //책 상태
                                String underbarTrace = dataMap.get("underbarTrace").toString();
                                String writeTrace = dataMap.get("writeTrace").toString();
                                String bookCover = dataMap.get("bookCover").toString();
                                boolean naming = (boolean)dataMap.get("naming");
                                boolean discolor = (boolean)dataMap.get("discolor");
                                String imageURL = dataMap.get("imageURL").toString();
                                bookItemBundle.add(new BookItem(title,author,UID,price,timeStamp,underbarTrace,writeTrace,bookCover,naming,discolor,imageURL));
                                Log.i("Feed","Data Added, title : "+title);
                            }
                            Collections.sort(bookItemBundle);
                            feedAdapter.notifyDataSetChanged();
                        }
                        else
                        {

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
                Intent intent = new Intent(Post2.this, WritePost.class);
                startActivity(intent);
            }
        });
        floatingBtn.hide();

    }

}