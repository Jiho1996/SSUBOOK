package org.techtown.ssubook;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class FeedItem
{
    private String title;   //제목
    private String author;  //use firebase UID, 저자
    private String UID; //게시글 UID
    private int price;  //가격
    private long timeStamp;  //올린시간


    //게시글 내용
    private ArrayList<String> imageURLs = new ArrayList<>();    //사진 URL이 담긴 ArrayList, FeedItem에서는 0번 인덱스를 대표 이미지로 사용

    public FeedItem(String title, String author, String UID, int price, long timeStamp)
    {
        this.title=title;
        this.author=author;
        this.UID=UID;
        this.price=price;
        this.timeStamp=timeStamp;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title=title;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author=author;
    }

    public String getUID()
    {
        return UID;
    }

    public void setUID(String UID)
    {
        this.UID=UID;
    }

    public int getPrice()
    {
        return price;
    }

    public void setPrice(int price)
    {
        this.price=price;
    }

    public long getTimeStamp()
    {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp)
    {
        this.timeStamp = timeStamp;
    }

    public String getTimeString() //String으로 TimeStamp 변환
    {
        Date date = new Date(timeStamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm", Locale.getDefault());
        return dateFormat.format(date);
    }
}
