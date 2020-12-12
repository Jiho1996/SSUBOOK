package org.techtown.ssubook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
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
import java.util.Map;

public class ChattingRoom extends AppCompatActivity
{
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<ChatItem> chatItemBundle = new ArrayList<ChatItem>();
    private ActionBar msgActionbar;
    ImageButton sendMsgBtn;
    ImageButton sendImageBtn;
    EditText textInput;
    String userUID;
    FirebaseFirestore firebaseDB = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_room);
        Intent chatDataIntent = getIntent();
        final String intent_receiver = chatDataIntent.getStringExtra("reciever");
        //Firestore 데이터 읽어오기

        //Uid 가져오기
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null)
        {
            userUID = currentUser.getUid();
            //채팅방 쿼리 작업
            CollectionReference chatRef = firebaseDB.collection("Chat");
            Query chatQuery = chatRef.whereEqualTo("sender",userUID).whereEqualTo("reciever",intent_receiver);
            chatQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task)
                {
                    if (task.isSuccessful())
                    {
                        for (QueryDocumentSnapshot document : task.getResult()) //Task 종료 시 getResult는 QuerySnapShot을 return, QuerySnapShot은 Iterable이므로 for-each 문으로 QueryDocumentSnapshot으로 사용가능.
                        {
                            //각각의 document는 서로 다른 Chat임

                            //QueryDocumentSnapshot은 모두 document형, getData()로 Map<String,Object>를 return
                            Map<String,Object> dataMap = document.getData();
                            String sender = dataMap.get("sender").toString();
                            String reciever = dataMap.get("reciever").toString();
                            Object timeStamp_o = dataMap.get("timeStamp");
                            long timeStamp = ((Timestamp) timeStamp_o).getSeconds();
                            String contents = dataMap.get("contents").toString();

                            ChatItem chatItem = new ChatItem(sender,reciever,timeStamp,contents);
                            chatItemBundle.add(chatItem);
                        }
                    }
                    else
                    {
                        //작업 실패 시
                    }
                }
            });
        }

        Collections.sort(chatItemBundle);

        msgActionbar = getSupportActionBar();
        msgActionbar.setDisplayHomeAsUpEnabled(true);   //상단바에 뒤로가기버튼
        //상대방 닉네임 가져오기
        firebaseDB.collection("User").document(intent_receiver).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
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
                        msgActionbar.setTitle( (nickname));
                    }
                    else
                    {
                        msgActionbar.setTitle("회원님");
                    }
                }
                else
                {
                    msgActionbar.setTitle("회원님");
                }
            }
        });



        //msgActionbar.setTitle("쪽지");    //상단바 타이틀 변경

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_chatroom);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManager);
        adapter = new ChatBalloonAdapter(chatItemBundle);
        recyclerView.setAdapter(adapter);

        sendMsgBtn = (ImageButton) findViewById(R.id.btn_chatroom_send);
        sendImageBtn = (ImageButton)findViewById(R.id.btn_chatroom_additional);
        textInput = (EditText) findViewById(R.id.textinput_chat);

        sendMsgBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(textInput.getText().toString().length()==0)
                    return;
                //메세지 전송
                String sender = userUID;
                String reciever = intent_receiver;
                String contents = textInput.getText().toString();

                Map<String,Object> chatData = new HashMap<>();
                chatData.put("sender",sender);
                chatData.put("reciever",reciever);
                chatData.put("contents",contents);
                firebaseDB.collection("Chat").add(chatData).addOnSuccessListener(new OnSuccessListener<DocumentReference>()
                {
                    @Override
                    public void onSuccess(DocumentReference documentReference)
                    {
                        Log.w("ChatMessage","Success");
                    }
                }).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Log.w("ChatMessage","Message Send Failed");
                    }
                });

                textInput.setText("");
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
            }
        });

        firebaseDB.collection("Chat").whereEqualTo("sender",userUID).whereEqualTo("reciever",intent_receiver).addSnapshotListener(new EventListener<QuerySnapshot>()
        {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error)
            {
                if(error != null)
                {
                    Log.w("ChattingRoom", "Listen Failed",error);
                }
                else
                {
                    for (DocumentChange doc: value.getDocumentChanges())
                    {
                        if(doc.getType()==DocumentChange.Type.ADDED)
                        {
                            Map<String,Object> addedDate = doc.getDocument().getData();
                            String sender = addedDate.get("sender").toString();
                            String reciever = addedDate.get("reciever").toString();
                            Object timeStamp_o = addedDate.get("timeStamp");
                            long timeStamp = ((Timestamp) timeStamp_o).getSeconds();
                            String contents = addedDate.get("contents").toString();

                            ChatItem chatItem = new ChatItem(sender,reciever,timeStamp,contents);
                            chatItemBundle.add(chatItem);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });

    }
}