package org.techtown.ssubook;

import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChatItem implements Comparable<ChatItem>
{
    private String sender;  //보낸사람
    private String receiver;    //받는사람
    private long timeStamp; //시간
    private String contents;    //콘텐츠
    private String chatUID;
    private String currentUID;
    private Date date;
    private int ViewType;


    private String opponent_Nickname;
    private String opponent_photo;

    public ChatItem(Date date)
    {
        this.date=date;
        ViewType=1004;
    }

    public ChatItem(String sender, String receiver, long timeStamp, String contents,String chatUID,String currentUID)
    {
        this.sender=sender;
        this.receiver=receiver;
        this.timeStamp=timeStamp;
        this.contents=contents;
        this.chatUID=chatUID;
        this.currentUID=currentUID;
        ViewType=this.getViewType();
        findOpponentprofile();
    }

    public String getChatUID()
    {
        return chatUID;
    }

    public void setChatUID(String chatUID)
    {
        this.chatUID = chatUID;
    }

    private boolean isValid(String url)
    {
        try
        {
            new URL(url).toURI();
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public boolean isPicture(String url)
    {
        if(isValid(url)&&url.contains("https://firebasestorage.googleapis.com/v0/b/ssu-usedbooktrade.appspot.com/"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public ChatItem(String chatUID)
    {
       FirebaseFirestore firebaseDB = FirebaseFirestore.getInstance();
       firebaseDB.collection("ChatRoom").document(chatUID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
       {
           @Override
           public void onComplete(@NonNull Task<DocumentSnapshot> task)
           {
               if(task.isSuccessful())
               {
                   DocumentSnapshot document = task.getResult();
                   if(document.exists())
                   {
                       sender=document.getData().get("sender").toString();
                       receiver=document.getData().get("sender").toString();
                       timeStamp=Long.parseLong(document.getData().get("timeStamp").toString());
                       contents=document.getData().get("sender").toString();
                   }
               }
           }
       });
    }

    private void findOpponentprofile()
    {
        FirebaseFirestore firebaseDB = FirebaseFirestore.getInstance();
        firebaseDB.collection("User").document(this.receiver).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if(task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists())
                    {
                        String nickname = document.getData().get("nickname").toString();
                        if(nickname.length()>0 && (nickname!=null))
                            opponent_Nickname = nickname;
                        String photo = document.getData().get("photo").toString();
                        if((photo.length()>0) && ((photo!=null)))
                            opponent_photo = photo;
                    }
                }
            }
        });
    }

    public String getOpponentPhoto()
    {
        if(opponent_photo==null)
            return "";
        else if(opponent_photo.length()>0)
            return opponent_photo;
        else
            return "";
    }

    public String getOpponentNickname()
    {
        if(opponent_Nickname==null)
            return "";
        else if(opponent_Nickname.length()>0)
            return opponent_Nickname;
        else
            return "상대방";
    }

    public String getTimeStringDay()
    {
        Date date = new Date(timeStamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        return dateFormat.format(date);
    }

    public long getTimeStamp()
    {
        return timeStamp;
    }

    public String getReceiver()
    {
        return receiver;
    }


    public String getContents() //채팅방 내부용
    {
        //사진 or 동영상일 경우 (사진) (동영상) 이라는 String return하도록 작업 필요함
        return contents;
    }

    public String getContentsOutside()  //외부용
    {
        if(isPicture(contents))
        {
            return "(사진)";
        }
        else
        {
            return contents;
        }
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

    public int getViewType()
    {
        if(date!=null)
            return 1004;
        else if(currentUID.equals(sender))
        {
            //SEND_TEXT : 1000, SEND_IMAGE : 1002
            if(isPicture(contents))
            {
                return 1002;
            }
            else
            {
                return 1000;
            }
        }
        else
        {
            //SEND_TEXT : 1000, SEND_IMAGE : 1002
            if(isPicture(contents))
            {
                return 1003;
            }
            else
            {
                return 1001;
            }
        }
    }
}
