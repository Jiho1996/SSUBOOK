package org.techtown.ssubook;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class BookItem implements Comparable<BookItem>
{
    private String title;   //제목
    private String author;  //use firebase UID, 저자
    private String UID; //게시글 UID
    private int price;  //가격
    private long timeStamp;  //올린시간

    //책 상태
    private String underbarTrace;   //NONE, PENCIL, PEN
    private String writeTrace;  //NONE, PENCIL, PEN
    private String bookCover; //CLEAN, DIRTY
    private boolean naming; //true:이름있음, false:없음
    private boolean discolor;   //변색, true:있음, false:없음

    //게시글 내용
    private String contents;    //내용
    private String imageURL;
    private ArrayList<String> imageURLs = new ArrayList<>();    //사진 URL이 담긴 ArrayList

    public BookItem(String title, String author, String UID, int price, long timeStamp)
    {
        this.title=title;
        this.author=author;
        this.UID=UID;
        this.price=price;
        this.timeStamp=timeStamp;
        underbarTrace="NONE";
        writeTrace="NONE";
        bookCover="CLEAN";
        naming=false;
        discolor=false;
    }

    public BookItem(String title, String author, String UID, int price, long timeStamp, String underbarTrace, String writeTrace, String bookCover, boolean naming, boolean discolor,String imageURL)
    {
        this.title=title;
        this.author=author;
        this.UID=UID;
        this.price=price;
        this.timeStamp=timeStamp;
        this.underbarTrace=underbarTrace;
        this.writeTrace=writeTrace;
        this.bookCover=bookCover;
        this.naming=naming;
        this.discolor=discolor;
        this.imageURL=imageURL;
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

    public void setContents(String contents)
    {
        this.contents = contents;
    }

    public String getContents()
    {
        return contents;
    }

    public String getImageURL()
    {
        return imageURL;
    }

    public void setImageURL(String imageURL)
    {
        this.imageURL = imageURL;
    }

    public String getTimeString() //String으로 TimeStamp 변환
    {
        Date date = new Date(timeStamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm", Locale.getDefault());
        return dateFormat.format(date);
    }

    @Override
    public int compareTo(BookItem bk)
    {
        if(bk.getTimeStamp()<getTimeStamp())
            return -1;
        else if(bk.getTimeStamp()>getTimeStamp())
            return 1;
        else
            return 0;
    }
}
