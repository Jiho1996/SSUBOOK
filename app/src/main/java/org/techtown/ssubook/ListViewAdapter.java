package org.techtown.ssubook;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter { //실습을 기반으로 작성했습니다.
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>() ;

    public ListViewAdapter() {

    }
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        ListViewItem item = listViewItemList.get(position); //데이터를 저장한 배열에서 position에 해당하는 아이템을 가져온다.


        ImageView image = (ImageView) convertView.findViewById(R.id.imageView1) ;
        image.setImageDrawable(item.getIcon());

        TextView title = (TextView) convertView.findViewById(R.id.textView1) ;
        title.setText(item.getTitle());

        TextView main_text = (TextView) convertView.findViewById(R.id.textView2) ;
        main_text.setText(item.getDesc());

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    public void addItem(Drawable icon, String title, String desc) { //firebase에서 불러온 데이터 하나를 저장하는 법.
        ListViewItem item = new ListViewItem();

        item.setIcon(icon);
        item.setTitle(title);
        item.setDesc(desc);

        listViewItemList.add(item);
    }
}