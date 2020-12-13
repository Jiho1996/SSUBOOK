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
    private ChatItem Last_Chat;
    private String me;

    public ChatRoomItem(ArrayList<String> UserList, String LastChat,String me)
    {
        this.UserList = UserList;
        this.LastChat = LastChat;
        this.me=me;
    }


    public ChatRoomItem(ArrayList<String> UserList, ArrayList<ChatItem> ChatList)
    {
        this.UserList = UserList;
        this.ChatList = ChatList;
    }

    public ChatRoomItem(ArrayList<String> UserList, ArrayList<String> ChatList,boolean LastChat)
    {
        this.UserList = UserList;
        for(String chat : ChatList)
        {
            this.ChatList.add(new ChatItem(chat));
        }
    }

    public String getMe()
    {
        return me;
    }

    public void setMe(String me)
    {
        this.me = me;
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

    public String getAnother()
    {
        int idx=0;
        if(getUserList().contains(me))
        {
            idx = getUserList().indexOf(me);
        }

        if(idx==0)
        {
            return getUserList().get(1);
        }
        else
        {
            return getUserList().get(0);
        }
    }
}
