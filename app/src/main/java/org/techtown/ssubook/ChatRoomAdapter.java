package org.techtown.ssubook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

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
        Context mContext = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.recycler_chat_room,parent,false);
        ChatRoomViewHolder holder = new ChatRoomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomViewHolder holder, int position)
    {
        //이미지 삽입 하는곳 (주석지우고 넣으삼)
        ChatRoomItem curItm = chatRoomItemBundle.get(position);

        holder.titleView.setText(curItm.getRecentChat().getOpponentNickname());
        holder.dateView.setText(curItm.getRecentChat().getTimeString());
        holder.maintextView.setText(curItm.getRecentChat().getContentsOutside());
        if((curItm.getRecentChat().getOpponentPhoto().length()>0)&&(curItm.getRecentChat().getOpponentPhoto()!="")&&(curItm.getRecentChat().getOpponentPhoto()!=null))
        {
            Glide.with(holder.opponentImageView.getContext()).load(curItm.getRecentChat().getOpponentPhoto()).into(holder.opponentImageView);
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
            titleView = v.findViewById(R.id.recycler_chatRoom_title);
            dateView = v.findViewById(R.id.recycler_chatRoom_Date);
            maintextView = v.findViewById(R.id.recycler_chatRoom_mainText);
            opponentImageView = v.findViewById(R.id.recycler_chatRoom_image);
        }
    }
}
