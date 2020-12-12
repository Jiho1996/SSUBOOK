package org.techtown.ssubook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

public class Feed extends AppCompatActivity
{
    private RecyclerView feedRecyclerView;
    private RecyclerView.Adapter feedAdapter;
    private RecyclerView.LayoutManager feedManager;
    private ActionBar feedActionbar;
    private SwipeRefreshLayout swipeLayout;
    ArrayList<BookItem> bookItemBundle = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        swipeLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_layout);
        feedRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_feed);
        feedActionbar = getSupportActionBar();

        //FireStore 데이터 읽어오기
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
                        Object timeStamp_o =  dataMap.get("timeStamp");
                        long timeStamp = ((Timestamp) timeStamp_o).getSeconds();


                        //책 상태
                        String underbarTrace = dataMap.get("underbarTrace").toString();   //NONE, PENCIL, PEN
                        String writeTrace = dataMap.get("writeTrace").toString();  //NONE, PENCIL, PEN
                        String bookCover = dataMap.get("bookCover").toString(); //CLEAN, DIRTY
                        boolean naming = (boolean)dataMap.get("naming"); //true:이름있음, false:없음
                        boolean discolor = (boolean)dataMap.get("discolor");   //변색, true:있음, false:없음
                        bookItemBundle.add(new BookItem(title,author,UID,price,timeStamp,underbarTrace,writeTrace,bookCover,naming,discolor));
                    }
                }
                else
                {
                    //작업 실패 시
                }
            }
        });
        Collections.sort(bookItemBundle);   //TimeStamp를 사용해 최신순 정렬

        feedRecyclerView.setHasFixedSize(true);
        feedManager = new LinearLayoutManager(this);

        feedRecyclerView.setLayoutManager(feedManager);
        feedAdapter = new FeedAdapter(bookItemBundle);
        feedRecyclerView.setAdapter(feedAdapter);

        //RecyclerView 업데이트 시
        /*
        feedAdapter.notifyDataSetChanged();
         */

        //ActionBar
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
                            feedActionbar.setTitle( (currentUser.getDisplayName()) + "회원님");
                        }
                    }
                    else
                    {
                        feedActionbar.setTitle( (currentUser.getDisplayName()) + "회원님");
                    }
                }
            });
        }

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
                            for (QueryDocumentSnapshot document : task.getResult()) //Task 종료 시 getResult는 QuerySnapShot을 return, QuerySnapShot은 Iterable이므로 for-each 문으로 QueryDocumentSnapshot으로 사용가능.
                            {
                                //QueryDocumentSnapshot은 모두 document형, getData()로 Map<String,Object>를 return
                                Map<String,Object> dataMap = document.getData();
                                String title = dataMap.get("title").toString();   //제목
                                String author = dataMap.get("author").toString();  //use firebase UID, 저자
                                String UID = dataMap.get("UID").toString(); //게시글 UID
                                int price = (int)dataMap.get("price");  //가격
                                Object timeStamp_o =  dataMap.get("timeStamp");
                                long timeStamp = ((Timestamp) timeStamp_o).getSeconds();


                                //책 상태
                                String underbarTrace = dataMap.get("underbarTrace").toString();   //NONE, PENCIL, PEN
                                String writeTrace = dataMap.get("writeTrace").toString();  //NONE, PENCIL, PEN
                                String bookCover = dataMap.get("bookCover").toString(); //CLEAN, DIRTY
                                boolean naming = (boolean)dataMap.get("naming"); //true:이름있음, false:없음
                                boolean discolor = (boolean)dataMap.get("discolor");   //변색, true:있음, false:없음
                                bookItemBundle.add(new BookItem(title,author,UID,price,timeStamp,underbarTrace,writeTrace,bookCover,naming,discolor));
                            }
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



    }
}

