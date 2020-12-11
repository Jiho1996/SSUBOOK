package org.techtown.ssubook;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class Message extends AppCompatActivity
{
    private ActionBar msgActionbar;
    ArrayList<ChatRoomItem> chatRoomItemBundle = new ArrayList<ChatRoomItem>(); //채팅방 아이템 번들
    private RecyclerView chatRoomRecyclerView;
    private RecyclerView.Adapter chatRoomAdapter;
    private RecyclerView.LayoutManager chatRoomManager;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        msgActionbar = getSupportActionBar();
        msgActionbar.setTitle("쪽지");    //상단바 타이틀 변경
        msgActionbar.setDisplayHomeAsUpEnabled(false);   //상단바에 뒤로가기버튼 삭제

        //Firestore 데이터 읽어오기
        

    }
}