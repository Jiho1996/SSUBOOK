package org.techtown.ssubook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ChattingRoom extends AppCompatActivity
{
    final int GET_GALLARY_IMAGE = 200;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<ChatItem> chatItemBundle = new ArrayList<ChatItem>();
    private ActionBar msgActionbar;
    ImageButton sendMsgBtn;
    ImageButton sendImageBtn;
    EditText textInput;
    String userUID;
    String chatRoomID;
    boolean haschatRoom;
    Uri selectedImageUri;
    FirebaseFirestore firebaseDB = FirebaseFirestore.getInstance();
    StorageReference imageRef;
    String reciever;
    String sender;
    String intent_receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_room);
        Intent chatDataIntent = getIntent();
        intent_receiver = chatDataIntent.getStringExtra("reciever");
        //Firestore 데이터 읽어오기
        //Uid 가져오기
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null)
        {
            userUID = currentUser.getUid();
            //채팅방 쿼리 작업
            CollectionReference chatRef = firebaseDB.collection("Chat");
           chatRef.whereEqualTo("sender",userUID).whereEqualTo("reciever",intent_receiver).addSnapshotListener(new EventListener<QuerySnapshot>()
            {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error)
                {
                    if (error != null)
                    {
                        Log.w("ChattingRoom", "Listen Failed", error);
                    }
                    else
                    {
                        String sender;
                        String reciever;
                        long timeStamp;
                        String contents;
                        String chatUID;
                        Map<String, Object> dataMap;
                        for (DocumentChange doc : value.getDocumentChanges())
                        {
                            switch (doc.getType())
                            {
                                case MODIFIED:
                                    dataMap = doc.getDocument().getData();    //dataMap.get("ChatList")하면 ["UID","UID",..]된 ArrayList들이 나옴
                                    sender = dataMap.get("sender").toString();
                                    reciever = dataMap.get("reciever").toString();
                                    timeStamp = Long.parseLong(dataMap.get("timeStamp").toString());
                                    contents = dataMap.get("contents").toString();

                                    Log.w("Message", "Chat Modified");
                                    break;
                                case REMOVED:
                                    break;
                                case ADDED:
                                default:
                                    dataMap = doc.getDocument().getData();    //dataMap.get("ChatList")하면 ["UID","UID",..]된 ArrayList들이 나옴
                                    sender = dataMap.get("sender").toString();
                                    reciever = dataMap.get("reciever").toString();
                                    timeStamp = Long.parseLong(dataMap.get("timeStamp").toString());
                                    contents = dataMap.get("contents").toString();
                                    chatUID = doc.getDocument().getId();
                                    chatItemBundle.add(new ChatItem(sender, reciever, timeStamp, contents, chatUID,currentUser.getUid()));
                                    Log.w("Message", "Chat Load_A / Chat Count:"+chatItemBundle.size());
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            });

            chatRef.whereEqualTo("sender",intent_receiver).whereEqualTo("reciever",userUID).addSnapshotListener(new EventListener<QuerySnapshot>()
            {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error)
                {
                    if (error != null)
                    {
                        Log.w("ChattingRoom", "Listen Failed", error);
                    }
                    else
                    {
                        String sender;
                        String reciever;
                        long timeStamp;
                        String contents;
                        String chatUID;
                        Map<String, Object> dataMap;
                        for (DocumentChange doc : value.getDocumentChanges())
                        {
                            switch (doc.getType())
                            {
                                case MODIFIED:
                                    dataMap = doc.getDocument().getData();    //dataMap.get("ChatList")하면 ["UID","UID",..]된 ArrayList들이 나옴
                                    sender = dataMap.get("sender").toString();
                                    reciever = dataMap.get("reciever").toString();
                                    timeStamp = Long.parseLong(dataMap.get("timeStamp").toString());
                                    contents = dataMap.get("contents").toString();

                                    Log.w("Message", "ChatRoom Modified");
                                    break;
                                case REMOVED:
                                    break;
                                case ADDED:
                                default:
                                    dataMap = doc.getDocument().getData();    //dataMap.get("ChatList")하면 ["UID","UID",..]된 ArrayList들이 나옴
                                    sender = dataMap.get("sender").toString();
                                    reciever = dataMap.get("reciever").toString();
                                    timeStamp = Long.parseLong(dataMap.get("timeStamp").toString());
                                    contents = dataMap.get("contents").toString();
                                    chatUID = doc.getDocument().getId();
                                    chatItemBundle.add(new ChatItem(sender, reciever, timeStamp, contents, chatUID,currentUser.getUid()));
                                    Log.w("Message", "Chat Load_B / Chat Count: "+chatItemBundle.size());
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            });


        }





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

        sendImageBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(intent,GET_GALLARY_IMAGE);
            }
        });

        sendMsgBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(textInput.getText().toString().length()==0)
                return;
                //메세지 전송
                sender = userUID;
                reciever  = intent_receiver;
                String contents = textInput.getText().toString();

                Map<String,Object> chatData = new HashMap<>();
                chatData.put("sender",sender);
                chatData.put("reciever",reciever);
                chatData.put("contents",contents);
                chatData.put("timeStamp",new Date().getTime());

                firebaseDB.collection("Chat").add(chatData).addOnSuccessListener(new OnSuccessListener<DocumentReference>()
                {
                    @Override
                    public void onSuccess(final DocumentReference documentReference)
                    {
                        Log.w("ChatMessage","Success");


                        firebaseDB.collection("ChatRoom").whereArrayContains("UserList", sender).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task)
                            {
                                if (task.isSuccessful())
                                {
                                    haschatRoom=false;
                                    for(QueryDocumentSnapshot document : task.getResult())
                                    {
                                        Map<String,Object> docData = document.getData();
                                        List<String> UserList = (List<String>)docData.get("UserList");
                                        if(UserList.contains(reciever))
                                        {
                                            chatRoomID = document.getId();
                                            document.getReference().update("LastChat", documentReference.getId()).addOnSuccessListener(new OnSuccessListener<Void>()
                                            {
                                                @Override
                                                public void onSuccess(Void aVoid)
                                                {
                                                    Log.i("ChattingRoom","Change LastChat");
                                                }
                                            });
                                            haschatRoom=true;
                                            break;
                                        }
                                    }
                                    if(!haschatRoom)
                                    {
                                        HashMap<String,Object> newChatRoom = new HashMap<>();
                                        ArrayList<String> newSnR = new ArrayList<>();
                                        newSnR.add(sender);
                                        newSnR.add(reciever);
                                        newChatRoom.put("LastChat",documentReference.getId());
                                        newChatRoom.put("UserList",newSnR);
                                        firebaseDB.collection("ChatRoom").add(newChatRoom).addOnSuccessListener(new OnSuccessListener<DocumentReference>()
                                        {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference)
                                            {

                                                chatRoomID = documentReference.getId();
                                                haschatRoom=true;
                                                Log.i("ChattingRoom","Make LastChat");
                                            }

                                        }).addOnFailureListener(new OnFailureListener()
                                        {
                                            @Override
                                            public void onFailure(@NonNull Exception e)
                                            {
                                                Log.i("ChattingRoom","LastChat Save Failed",e);
                                            }
                                        });

                                    }
                                }
                                else
                                {
                                    Log.i("ChattingRoom","Task Failed!");
                                }
                            }
                        });

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



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLARY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            selectedImageUri = data.getData();
            //pictureView.setImageURI(selectedImageUri);
            //Glide.with(getApplicationContext()).load(selectedImageUri).into(pictureView);
            UUID imageUUID = UUID.randomUUID();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            imageRef = storage.getReference().child("Message/"+imageUUID+"-"+selectedImageUri.getLastPathSegment());
            imageRef.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
            {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                    {
                        @Override
                        public void onSuccess(Uri uri)
                        {
                            //메세지 전송
                            String contents = uri.toString();
                            sender = userUID;
                            reciever  = intent_receiver;

                            Map<String,Object> chatData = new HashMap<>();
                            chatData.put("sender",sender);
                            chatData.put("reciever",reciever);
                            chatData.put("contents",contents);
                            chatData.put("timeStamp",new Date().getTime());

                            firebaseDB.collection("Chat").add(chatData).addOnSuccessListener(new OnSuccessListener<DocumentReference>()
                            {
                                @Override
                                public void onSuccess(final DocumentReference documentReference)
                                {
                                    Log.w("ChatMessage","Success");


                                    firebaseDB.collection("ChatRoom").whereArrayContains("UserList", sender).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                                    {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task)
                                        {
                                            if (task.isSuccessful())
                                            {
                                                haschatRoom=false;
                                                for(QueryDocumentSnapshot document : task.getResult())
                                                {
                                                    Map<String,Object> docData = document.getData();
                                                    List<String> UserList = (List<String>)docData.get("UserList");
                                                    if(UserList.contains(reciever))
                                                    {
                                                        chatRoomID = document.getId();
                                                        document.getReference().update("LastChat", documentReference.getId()).addOnSuccessListener(new OnSuccessListener<Void>()
                                                        {
                                                            @Override
                                                            public void onSuccess(Void aVoid)
                                                            {
                                                                Log.i("ChattingRoom","Change LastChat");
                                                            }
                                                        });
                                                        haschatRoom=true;
                                                        break;
                                                    }
                                                }
                                                if(!haschatRoom)
                                                {
                                                    HashMap<String,Object> newChatRoom = new HashMap<>();
                                                    ArrayList<String> newSnR = new ArrayList<>();
                                                    newSnR.add(sender);
                                                    newSnR.add(reciever);
                                                    newChatRoom.put("LastChat",documentReference.getId());
                                                    newChatRoom.put("UserList",newSnR);
                                                    firebaseDB.collection("ChatRoom").add(newChatRoom).addOnSuccessListener(new OnSuccessListener<DocumentReference>()
                                                    {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference)
                                                        {

                                                            chatRoomID = documentReference.getId();
                                                            haschatRoom=true;
                                                            Log.i("ChattingRoom","Make LastChat");
                                                        }

                                                    }).addOnFailureListener(new OnFailureListener()
                                                    {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e)
                                                        {
                                                            Log.i("ChattingRoom","LastChat Save Failed",e);
                                                        }
                                                    });

                                                }
                                            }
                                            else
                                            {
                                                Log.i("ChattingRoom","Task Failed!");
                                            }
                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener()
                            {
                                @Override
                                public void onFailure(@NonNull Exception e)
                                {
                                    Log.w("ChatMessage","Message Send Failed");
                                }
                            });

                        }
                    });
                }
            });
        }
    }

}