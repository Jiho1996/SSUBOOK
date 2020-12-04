package org.techtown.ssubook;

import java.util.ArrayList;
import java.util.Date;

public class FeedItem
{
    private String title;   //제목
    private String author;  //use firebase UID, 저자
    private String UID; //게시글 UID
    private int price;  //가격
    private long timeStamp;  //올린시간


    //게시글 내용
    private ArrayList<String> imageURLs = new ArrayList<>();    //사진 URL이 담긴 ArrayList, FeedItem에서는 0번 인덱스를 대표 이미지로 사용
}
