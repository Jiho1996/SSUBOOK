package org.techtown.ssubook;

import java.util.ArrayList;
import java.util.Date;

public class BookItem
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
    private ArrayList<String> imageURLs = new ArrayList<>();    //사진 URL이 담긴 ArrayList

}
