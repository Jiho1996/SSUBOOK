package org.techtown.ssubook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ChatRoomViewHolder>
{
    ArrayList<ChatRoomItem> ChatRoomItemBundle = new ArrayList<>();
    Context mContext;

    public ChatRoomAdapter(ArrayList<ChatRoomItem> bundle)
    {
        this.ChatRoomItemBundle = bundle;
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
        ChatRoomItem curItm = ChatRoomItemBundle.get(position);

        //ChatRoomItem으로 오므로 ChatRoom에서 최신 Chat 추출
        ChatItem recentChat = curItm.getRecentChat();

        holder.titleView.setText(recentChat.getSender());
        holder.dateView.setText(recentChat.getTimeString());
        holder.recentChatView.setText(recentChat.getContents());
        //이미지뷰는 Firebase 작업 후 구현
        //holder.mainImageView.
    }

    @Override
    public int getItemCount()
    {
        return ChatRoomItemBundle.size();
    }

    public class ChatRoomViewHolder extends RecyclerView.ViewHolder
    {
        TextView titleView;
        TextView dateView;
        TextView recentChatView;
        ImageView mainImageView;

        public ChatRoomViewHolder(@NonNull View v)
        {
            super(v);
            titleView = v.findViewById(R.id.recycler_chatRoom_title);
            dateView = v.findViewById(R.id.recycler_chatRoom_Date);
            recentChatView = v.findViewById(R.id.recycler_chatRoom_mainText);
            mainImageView = v.findViewById(R.id.recycler_chatRoom_image);
        }
    }
}
