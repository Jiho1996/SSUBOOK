package org.techtown.ssubook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);

        Intent intent = getIntent();
        String UID = intent.getStringExtra("UID");

        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore firebaseDB = FirebaseFirestore.getInstance();
        firebaseDB.collection("ChatRoom").document(UID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
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

                        
                    }
                }
            }
        });


    }
}