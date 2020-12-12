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
    ArrayList<BookItem> feedItemBundle = new ArrayList<>();
    Context mContext;
    public FeedAdapter(ArrayList<BookItem> bundle)
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
        //이미지 삽입 하는곳 (주석지우고 넣으삼)
        BookItem curItm = feedItemBundle.get(position);

        holder.titleView.setText(curItm.getTitle());
        holder.dateView.setText(curItm.getTimeString());
        holder.priceView.setText(curItm.getPrice()+"원");
        //이미지뷰는 Firebase 작업 후 구현
        //holder.mainImageView.
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
            mainImageView = v.findViewById(R.id.recyclerViewFeed_Image);
            //Todo : 클릭 이벤트 처리하기
            /*
            v.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {

                }
            });
            */

        }
    }
}
