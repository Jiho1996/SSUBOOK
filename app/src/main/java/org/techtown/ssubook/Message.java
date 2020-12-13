package org.techtown.ssubook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Message extends AppCompatActivity
{
    private ActionBar msgActionbar;
    ArrayList<ChatRoomItem> chatRoomItemBundle = new ArrayList<ChatRoomItem>(); //채팅방 아이템 번들
    private RecyclerView chatRoomRecyclerView;
    private RecyclerView.Adapter chatRoomAdapter;
    private RecyclerView.LayoutManager chatRoomManager;
    private ArrayList<ChatItem> chatItems = new ArrayList<>();
    HashMap<String,ChatRoomItem> chatMap = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        msgActionbar = getSupportActionBar();
        msgActionbar.setDisplayHomeAsUpEnabled(false);   //상단바에 뒤로가기버튼
        msgActionbar.setTitle("쪽지");
        chatRoomRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_message);
        //Firestore 데이터 읽어오기
        final FirebaseFirestore firebaseDB = FirebaseFirestore.getInstance();
        //Uid 가져오기
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null)
        {
            final String userUID = currentUser.getUid();
            //채팅방 쿼리 작업
            final CollectionReference chatRef = firebaseDB.collection("ChatRoom");

            final Query chatRoomQuery = chatRef.whereArrayContains("UserList",userUID);
            chatRoomQuery.addSnapshotListener(new EventListener<QuerySnapshot>()
            {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error)
                {
                    if(error != null)
                    {
                        Log.w("ChatRoom", "Listen Failed",error);
                    }
                    else
                    {
                        ArrayList<String> ChatList;
                        ArrayList<String> UserList;
                        String lastChat;
                        Map<String, Object> dataMap;
                        for (DocumentChange doc: value.getDocumentChanges())
                        {
                            switch (doc.getType())
                            {
                                case MODIFIED:
                                    dataMap = doc.getDocument().getData();    //dataMap.get("ChatList")하면 ["UID","UID",..]된 ArrayList들이 나옴
                                    ChatList = (ArrayList) dataMap.get("ChatList");
                                    UserList = (ArrayList) dataMap.get("UserList");
                                    lastChat = dataMap.get("LastChat").toString();
                                    for (ChatRoomItem chat : chatRoomItemBundle)
                                    {
                                        if(chat.getUserList().equals(UserList))
                                        {
                                            chat.setLastChat(lastChat);
                                        }
                                    }
                                    Log.w("Message", "ChatRoom Modified");
                                    break;
                                case REMOVED:
                                    break;
                                case ADDED:
                                default:
                                    dataMap = doc.getDocument().getData();    //dataMap.get("ChatList")하면 ["UID","UID",..]된 ArrayList들이 나옴
                                    ChatList = (ArrayList) dataMap.get("ChatList");
                                    UserList = (ArrayList) dataMap.get("UserList");
                                    lastChat = dataMap.get("LastChat").toString();
                                    chatRoomItemBundle.add(new ChatRoomItem(UserList, lastChat));
                                    Log.w("Message", "ChatRoom Load");
                            }
                        }
                        chatRoomAdapter.notifyDataSetChanged();
                    }
                }
            });

        }

        chatRoomRecyclerView.setHasFixedSize(true);
        chatRoomManager = new LinearLayoutManager(this);

        chatRoomRecyclerView.setLayoutManager(chatRoomManager);
        chatRoomAdapter = new ChatRoomAdapter(chatRoomItemBundle);
        chatRoomRecyclerView.setAdapter(chatRoomAdapter);


    }

    private void refresh()
    {
        FirebaseFirestore firebaseDB = FirebaseFirestore.getInstance();
        String userUID;
        CollectionReference chatRef = firebaseDB.collection("Chat");

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null)
        {
            userUID = currentUser.getUid();
            final Query chatRoomQuery = chatRef.whereArrayContains("UserList",userUID);
            chatRoomQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task)
                {
                    if (task.isSuccessful())
                    {
                        chatRoomItemBundle.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) //Task 종료 시 getResult는 QuerySnapShot을 return, QuerySnapShot은 Iterable이므로 for-each 문으로 QueryDocumentSnapshot으로 사용가능.
                        {
                            //각각의 document는 서로 다른 ChatRoom임

                            //QueryDocumentSnapshot은 모두 document형, getData()로 Map<String,Object>를 return
                            Map<String, Object> dataMap = document.getData();    //dataMap.get("ChatList")하면 ["UID","UID",..]된 ArrayList들이 나옴
                            ArrayList<String> ChatList = (ArrayList) dataMap.get("ChatList");
                            ArrayList<String> UserList = (ArrayList) dataMap.get("UserList");
                            String lastChat = dataMap.get("LastChat").toString();
                            chatRoomItemBundle.add(new ChatRoomItem(UserList, lastChat));
                            Log.w("Message", "ChatRoom Load");
                        }

                        chatRoomAdapter.notifyDataSetChanged();
                    }
                    else
                    {
                        //작업 실패 시
                        Log.w("Message", "Firestore Load Failed");
                    }
                }
            });
        }
    }
}