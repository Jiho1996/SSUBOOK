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

import java.util.ArrayList;

public class ChatBalloonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    final static int SEND_TEXT = 1000;
    final static int RECIEVE_TEXT = 1001;
    final static int SEND_IMAGE = 1002;
    final static int RECIEVE_IMAGE = 1003;
    final static int DATE = 1004;
    ArrayList<ChatItem> ChatItemBundle = new ArrayList<>();
    Context mContext;

    public ChatBalloonAdapter(ArrayList<ChatItem> bundle)
    {
        this.ChatItemBundle = bundle;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        Context mContext = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = null;
        if(viewType==SEND_TEXT)
        {
            view = inflater.inflate(R.layout.send_message_item, parent, false);
            return new SendTextViewHolder(view);
        }
        else if(viewType==RECIEVE_TEXT)
        {
            view = inflater.inflate(R.layout.receive_message_item, parent, false);
            return new RecieveTextViewHolder(view);
        }
        else if(viewType==SEND_IMAGE)
        {
            view = inflater.inflate(R.layout.send_image_item, parent, false);
            return new SendImageViewHolder(view);
        }
        else if(viewType==RECIEVE_IMAGE)
        {
            view = inflater.inflate(R.layout.receive_image_item, parent, false);
            return new RecieveImageViewHolder(view);
        }
        else
        {
            view = inflater.inflate(R.layout.middle_message_item, parent, false);
            return new DateViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        ChatItem curItm = ChatItemBundle.get(position);
        if(holder instanceof SendTextViewHolder)
        {
            ((SendTextViewHolder) holder).dateView.setText(curItm.getTimeString());
            ((SendTextViewHolder) holder).chatView.setText(curItm.getContents());
        }
        else if(holder instanceof SendImageViewHolder)
        {
            ((SendImageViewHolder) holder).dateView.setText(curItm.getTimeString());
            Glide.with(holder.itemView.getContext()).load(curItm.getContents()).into(((SendImageViewHolder) holder).chatImageView);
        }
        else if(holder instanceof RecieveTextViewHolder)
        {
            ((RecieveTextViewHolder) holder).dateView.setText(curItm.getTimeString());
            ((RecieveTextViewHolder) holder).chatView.setText(curItm.getContents());
        }
        else if(holder instanceof RecieveImageViewHolder)
        {
            ((RecieveImageViewHolder) holder).dateView.setText(curItm.getTimeString());
            Glide.with(holder.itemView.getContext()).load(curItm.getContents()).into(((RecieveImageViewHolder) holder).chatImageView);
        }
        else if(holder instanceof DateViewHolder)
        {
            ((DateViewHolder) holder).dateView.setText(curItm.getTimeStringDay());
        }
    }

    @Override
    public int getItemCount()
    {
        return ChatItemBundle.size();
    }

    public class SendTextViewHolder extends RecyclerView.ViewHolder
    {
        TextView dateView;
        TextView chatView;

        public SendTextViewHolder(@NonNull View v)
        {
            super(v);
            dateView = v.findViewById(R.id.send_message_timetext);
            chatView = v.findViewById(R.id.send_message_text);
        }
    }

    public class SendImageViewHolder extends RecyclerView.ViewHolder
    {
        TextView dateView;
        ImageView chatImageView;

        public SendImageViewHolder(@NonNull View v)
        {
            super(v);
            dateView = v.findViewById(R.id.send_message_time_image);
            chatImageView = v.findViewById(R.id.send_message_image);
        }
    }

    public class RecieveTextViewHolder extends RecyclerView.ViewHolder
    {
        TextView dateView;
        TextView chatView;

        public RecieveTextViewHolder(@NonNull View v)
        {
            super(v);
            dateView = v.findViewById(R.id.recieve_message_timetext);
            chatView = v.findViewById(R.id.recieve_message_text);
        }
    }

    public class RecieveImageViewHolder extends RecyclerView.ViewHolder
    {
        TextView dateView;
        ImageView chatImageView;

        public RecieveImageViewHolder(@NonNull View v)
        {
            super(v);
            dateView = v.findViewById(R.id.recieve_message_time_image);
            chatImageView = v.findViewById(R.id.recieve_message_image);
        }
    }

    public class DateViewHolder extends RecyclerView.ViewHolder
    {
        TextView dateView;

        public DateViewHolder(@NonNull View v)
        {
            super(v);
            dateView = v.findViewById(R.id.middle_message_text);
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        return ChatItemBundle.get(position).getViewType();
    }
}
