package org.techtown.ssubook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

public class ChatRoomItem implements Comparable<ChatRoomItem>
{
    private ArrayList<String> UserList = new ArrayList<>(); //유저 목록
    private ArrayList<ChatItem> ChatList = new ArrayList<>(); //채팅 목록
    private String LastChat;


    public ChatRoomItem(ArrayList<String> UserList, ArrayList<ChatItem> ChatList)
    {
        this.UserList = UserList;
        this.ChatList = ChatList;
    }

    public ChatRoomItem(ArrayList<String> UserList, ArrayList<String> ChatList,boolean isChatUID)
    {
        this.UserList = UserList;
        for(int i=0;i<ChatList.size();i++)
        {
            this.ChatList.add(new ChatItem( ChatList.get(i)));
        }
    }

    public String getLastChat()
    {
        return LastChat;
    }

    public void setLastChat(String lastChat)
    {
        LastChat = lastChat;
    }

    public ArrayList<ChatItem> getChatList()
    {
        return ChatList;
    }

    public ArrayList<String> getUserList()
    {
        return UserList;
    }

    public void setChatList(ArrayList<ChatItem> chatList)
    {
        ChatList = chatList;
    }

    public void setUserList(ArrayList<String> userList)
    {
        UserList = userList;
    }

    public ChatItem getRecentChat()
    {
        Collections.sort(ChatList);
        return ChatList.get(0);
    }


    @Override
    public int compareTo(ChatRoomItem chatRoomItem)
    {
        if(chatRoomItem.getRecentChat().getTimeStamp() < getRecentChat().getTimeStamp())
            return -1;
        else if(chatRoomItem.getRecentChat().getTimeStamp() > getRecentChat().getTimeStamp())
            return 1;
        else
            return 0;
    }
}
