package org.techtown.ssubook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Page extends AppCompatActivity {
    TextView title_text;
    TextView author_text;
    TextView low1cos1;
    TextView low1cos2;
    TextView low1cos3;

    TextView low2cos1;
    TextView low2cos2;
    TextView low2cos3;

    TextView low3cos1;
    TextView low3cos2;

    TextView low4cos1;
    TextView low4cos2;

    TextView low5cos1;
    TextView low5cos2;
    TextView content_text;
    TextView price_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);

        title_text= (TextView)findViewById(R.id.titletext);
        author_text= (TextView)findViewById(R.id.authortext);
        content_text = (TextView)findViewById(R.id.contents);
        price_text=(TextView)findViewById(R.id.price);

        low1cos1= (TextView)findViewById(R.id.row1col1);
        low1cos2= (TextView)findViewById(R.id.row1col2);
        low1cos3= (TextView)findViewById(R.id.row1col3);
        low2cos1= (TextView)findViewById(R.id.row2col1);
        low2cos2= (TextView)findViewById(R.id.row2col2);
        low2cos3= (TextView)findViewById(R.id.row2col3);
        low3cos1= (TextView)findViewById(R.id.row3col1);
        low3cos2= (TextView)findViewById(R.id.row3col2);
        low4cos1= (TextView)findViewById(R.id.row4col1);
        low4cos2= (TextView)findViewById(R.id.row4col2);
        low5cos1= (TextView)findViewById(R.id.row5col1);
        low5cos2= (TextView)findViewById(R.id.row5col2);

        Intent intent = getIntent();
        String UID = intent.getStringExtra("UID");

        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore firebaseDB = FirebaseFirestore.getInstance();
        firebaseDB.collection("Post").document(UID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if(task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists())
                    {
                         String title;   //제목
                         String author;  //use firebase UID, 저자
                         String UID; //게시글 UID
                         int price;  //가격
                         long timeStamp;  //올린시간

                        //책 상태
                         String underbarTrace;   //NONE, PENCIL, PEN
                         String writeTrace;  //NONE, PENCIL, PEN
                         String bookCover; //CLEAN, DIRTY
                         boolean naming; //true:이름있음, false:없음
                         boolean discolor;   //변색, true:있음, false:없음

                        //게시글 내용
                         String contents;    //내용
                         String imageURL;

                        title=document.getData().get("title").toString();
                        author=document.getData().get("author").toString();
                        UID=document.getData().get("UID").toString();
                        price=Integer.parseInt(document.getData().get("price").toString());
                        timeStamp=Long.parseLong(document.getData().get("timeStamp").toString());

                        underbarTrace=document.getData().get("underbarTrace").toString();
                        writeTrace=document.getData().get("writeTrace").toString();
                        bookCover=document.getData().get("bookCover").toString();
                        naming=(boolean)document.getData().get("naming");
                        discolor=(boolean)document.getData().get("discolor");

                        contents=document.getData().get("contents").toString();
                        imageURL=document.getData().get("imageURL").toString();

                        title_text.setText(title);
                        author_text.setText(author);

                        if(writeTrace.equals("PENCIL"))
                            low1cos3.setTypeface(null, Typeface.BOLD);
                        else if(writeTrace.equals("PEN"))
                            low1cos2.setTypeface(null, Typeface.BOLD);
                        else
                            low1cos1.setTypeface(null, Typeface.BOLD);

                        if(underbarTrace.equals("PENCIL"))
                            low2cos3.setTypeface(null, Typeface.BOLD);
                        else if(underbarTrace.equals("PEN"))
                            low2cos2.setTypeface(null, Typeface.BOLD);
                        else
                            low2cos1.setTypeface(null, Typeface.BOLD);
//ㅁㅁㄴ

                        if(bookCover.equals("CLEAN"))
                            low3cos1.setTypeface(null, Typeface.BOLD);
                        else
                            low3cos2.setTypeface(null, Typeface.BOLD);

                        if(naming)
                            low4cos2.setTypeface(null, Typeface.BOLD);
                        else
                            low4cos1.setTypeface(null, Typeface.BOLD);

                        if(discolor)
                            low5cos2.setTypeface(null, Typeface.BOLD);
                        else
                            low5cos1.setTypeface(null, Typeface.BOLD);

                        content_text.setText(contents);

                        price_text.setText(Integer.toString(price)+"원");
                    }
                    else
                    {
                        Log.w("page","NONE_EXIST");
                    }
                }
            }
        });


    }
}