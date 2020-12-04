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

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder>
{
    ArrayList<FeedItem> feedItemBundle = new ArrayList<>();
    Context mContext;
    public FeedAdapter(ArrayList<FeedItem> bundle)
    {
        this.feedItemBundle = bundle;
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        Context mContext = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.recycler_feed_item,parent,false);
        FeedViewHolder holder = new FeedViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position)
    {

    }

    @Override
    public int getItemCount()
    {
        return feedItemBundle.size();
    }

    public class FeedViewHolder extends RecyclerView.ViewHolder
    {
        TextView titleView;
        TextView dateView;
        TextView priceView;
        ImageView mainImageView;

        public FeedViewHolder(@NonNull View v)
        {
            super(v);
            titleView = v.findViewById(R.id.recyclerViewFeed_Title);
            dateView = v.findViewById(R.id.recyclerViewFeed_Date);
            priceView = v.findViewById(R.id.recyclerViewFeed_Price);
            //ImageView = v.findViewById(R.id.recyclerViewFeed_Image);
        }
    }
}
