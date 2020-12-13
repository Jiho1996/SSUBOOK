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
                        Log.w("Message", "Listen Failed",error);
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
                                    chatRoomItemBundle.add(new ChatRoomItem(UserList, lastChat,userUID));
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


}