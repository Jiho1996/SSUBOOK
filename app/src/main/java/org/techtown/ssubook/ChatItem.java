package org.techtown.ssubook;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChatItem implements Comparable<ChatItem>
{
    private String sender;  //보낸사람
    private String receiver;    //받는사람
    private long timeStamp; //시간
    private String contents;    //콘텐츠

    public ChatItem(String sender, String receiver, long timeStamp, String contents)
    {
        this.sender=sender;
        this.receiver=receiver;
        this.timeStamp=timeStamp;
        this.contents=contents;
    }

    public long getTimeStamp()
    {
        return timeStamp;
    }

    public String getReceiver()
    {
        return receiver;
    }

    public String getContents()
    {
        //사진 or 동영상일 경우 (사진) (동영상) 이라는 String return하도록 작업 필요함
        return contents;
    }

    public String getSender()
    {
        return sender;
    }

    public void setTimeStamp(long timeStamp)
    {
        this.timeStamp = timeStamp;
    }

    public void setContents(String contents)
    {
        this.contents = contents;
    }

    public void setReceiver(String receiver)
    {
        this.receiver = receiver;
    }

    public void setSender(String sender)
    {
        this.sender = sender;
    }
    public String getTimeString() //String으로 TimeStamp 변환
    {
        Date date = new Date(timeStamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm", Locale.getDefault());
        return dateFormat.format(date);
    }

    @Override
    public int compareTo(ChatItem c)
    {
        if(c.getTimeStamp()<getTimeStamp())
            return -1;
        else if(c.getTimeStamp()>getTimeStamp())
            return 1;
        else
            return 0;
    }
}
