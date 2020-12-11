package org.techtown.ssubook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

public class ChatRoomItem
{
    private ArrayList<String> UserList = new ArrayList<>(); //유저 목록
    private ArrayList<ChatItem> ChatList = new ArrayList<>(); //채팅 목록


    public ChatRoomItem(ArrayList<String> UserList, ArrayList<ChatItem> ChatList)
    {
        this.UserList = UserList;
        this.ChatList = ChatList;
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
}
