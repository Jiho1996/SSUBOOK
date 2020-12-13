package org.techtown.ssubook;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ChatRoomViewHolder>
{
    ArrayList<ChatRoomItem> chatRoomItemBundle = new ArrayList<>();
    Context mContext;
    public ChatRoomAdapter(ArrayList<ChatRoomItem> bundle)
    {
        this.chatRoomItemBundle = bundle;
    }

    @NonNull
    @Override
    public ChatRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        mContext = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.recycler_chat_room,parent,false);
        ChatRoomViewHolder holder = new ChatRoomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatRoomViewHolder holder, int position)
    {
        //이미지 삽입 하는곳 (주석지우고 넣으삼)
        final ChatRoomItem curItm = chatRoomItemBundle.get(position);

        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore firebaseDB = FirebaseFirestore.getInstance();
        firebaseDB.collection("Chat").document(curItm.getLastChat()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                DocumentSnapshot document = task.getResult();
                if(document.exists())
                {
                    String sender=document.getData().get("sender").toString();
                    final String receiver=document.getData().get("reciever").toString();
                    long timeStamp=Long.parseLong(document.getData().get("timeStamp").toString());
                    String contents = document.getData().get("contents").toString();
                    if(isPicture(contents))
                    {
                        holder.maintextView.setText("(사진)");
                    }
                    else
                    {
                        holder.maintextView.setText(contents);
                    }
                    final String opponent;

                    Date date = new Date(timeStamp);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm", Locale.getDefault());
                    holder.dateView.setText(dateFormat.format(date));
                    if(currentUser.getUid().equals(sender))
                    {
                        opponent=receiver;
                    }
                    else
                    {
                        opponent=sender;
                    }
                    firebaseDB.collection("User").document(opponent).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task)
                        {
                            DocumentSnapshot document = task.getResult();
                            if(document.exists())
                            {
                                String nickname = document.getData().get("nickname").toString();
                                String photo = document.getData().get("photo").toString();
                                if((nickname!=null)&&(nickname.equals("")&&(nickname.length()>0)))
                                {
                                    holder.titleView.setText(nickname);
                                }
                                else
                                {
                                    holder.titleView.setText("상대방");
                                }

                                if((photo!=null)&&(photo.equals("")&&(photo.length()>0)))
                                {
                                    Glide.with(holder.opponentImageView.getContext()).load(photo).into(holder.opponentImageView);
                                }
                            }
                        }
                    });
                }
            }
        });



    }
    private boolean isValid(String url)
    {
        try
        {
            new URL(url).toURI();
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public boolean isPicture(String url)
    {
        if(isValid(url)&&url.contains("https://firebasestorage.googleapis.com/v0/b/ssu-usedbooktrade.appspot.com/"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public int getItemCount()
    {
        return chatRoomItemBundle.size();
    }

    public class ChatRoomViewHolder extends RecyclerView.ViewHolder
    {
        TextView titleView;
        TextView dateView;
        TextView maintextView;
        ImageView opponentImageView;

        public ChatRoomViewHolder(@NonNull View v)
        {
            super(v);
            v.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    int pos = getAdapterPosition();
                    if(pos!=RecyclerView.NO_POSITION)
                    {
                        ChatRoomItem chatroomItem = chatRoomItemBundle.get(pos);
                        String reciever = chatroomItem.getAnother();
                        Intent intent = new Intent(view.getContext(),ChattingRoom.class);
                        intent.putExtra("reciever",reciever);
                        mContext.startActivity(intent);
                    }
                }
            });
            titleView = v.findViewById(R.id.recycler_chatRoom_title);
            dateView = v.findViewById(R.id.recycler_chatRoom_Date);
            maintextView = v.findViewById(R.id.recycler_chatRoom_mainText);
            opponentImageView = v.findViewById(R.id.recycler_chatRoom_image);


        }
    }
}
