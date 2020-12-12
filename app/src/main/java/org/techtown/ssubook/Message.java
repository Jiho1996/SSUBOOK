package org.techtown.ssubook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class Message extends AppCompatActivity
{
    private ActionBar msgActionbar;
    ArrayList<ChatRoomItem> chatRoomItemBundle = new ArrayList<ChatRoomItem>(); //채팅방 아이템 번들
    private RecyclerView chatRoomRecyclerView;
    private RecyclerView.Adapter chatRoomAdapter;
    private RecyclerView.LayoutManager chatRoomManager;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        //Firestore 데이터 읽어오기
        FirebaseFirestore firebaseDB = FirebaseFirestore.getInstance();
        //Uid 가져오기
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null)
        {
            String userUID = currentUser.getUid();
            //채팅방 쿼리 작업
            CollectionReference chatRef = firebaseDB.collection("ChatRooms");
            Query chatRoomQuery = chatRef.whereArrayContains("UserList",userUID);
            chatRoomQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task)
                {
                    if (task.isSuccessful())
                    {
                        for (QueryDocumentSnapshot document : task.getResult()) //Task 종료 시 getResult는 QuerySnapShot을 return, QuerySnapShot은 Iterable이므로 for-each 문으로 QueryDocumentSnapshot으로 사용가능.
                        {
                            //각각의 document는 서로 다른 ChatRoom임

                            //QueryDocumentSnapshot은 모두 document형, getData()로 Map<String,Object>를 return
                            Map<String,Object> dataMap = document.getData();    //dataMap.get("ChatList")하면 ["UID","UID",..]된 ArrayList들이 나옴

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
}