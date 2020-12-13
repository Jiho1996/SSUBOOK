package org.techtown.ssubook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Mypage extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private ImageButton feedButton;
    private ImageButton searchButton;
    ImageView profileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        setTitle("마이페이지");
        feedButton = (ImageButton) findViewById(R.id.btn_feed);
        searchButton = (ImageButton) findViewById(R.id.btn_search);
        profileImage=(ImageView)findViewById(R.id.myprofile);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        //Log.d("abc", currentUser.getUid());


        final TextView textView=(TextView)findViewById(R.id.name_text);
        FirebaseFirestore firebaseDB = FirebaseFirestore.getInstance();
        if(currentUser != null)
        {
            //DB에서 별명 가져오기
            firebaseDB.collection("User").document(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
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
                            textView.setText(nickname);

                            String profilePhoto = document.getData().get("photo").toString();
                            if((!profilePhoto.equals(""))&&(profilePhoto.length()>0)&&(profilePhoto!=null))
                                Glide.with(getApplicationContext()).load(profilePhoto).into(profileImage); //이게 없으면 기본적으로 제공한 아이콘이 보인다.
                        }
                        else
                        {
                            textView.setText("회원님");

                        }
                    }
                    else
                    {
                        textView.setText("회원님");
                    }
                }
            });
        }

        //Log.d("abc", currentUser.getEmail());
        //textView.setText(currentUser.getUid());

        searchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Mypage.this, search.class);
                startActivity(intent);
            }
        });
        feedButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Mypage.this, Feed.class);
                startActivity(intent);
            }
        });

    }

    public void onLogOutButtonClick( View v )
    {

        FirebaseAuth.getInstance().signOut();
        Toast.makeText( this, "Log Out 성공", Toast.LENGTH_LONG ).show();
        Intent intent = new Intent(Mypage.this, LoginActivity.class);
        startActivity(intent);
    }



    public void onMyPostButtonClick( View v )
    {

        Intent intent = new Intent();
        ComponentName componentName = new ComponentName (
                "org.techtown.ssubook",
                "org.techtown.ssubook.Post");
        intent.setComponent(componentName);
        intent.putExtra("activty", 0);
        startActivity(intent);
    }


    public void onInterestedPostButtonClick( View v )
    {
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName (
                "org.techtown.ssubook",
                "org.techtown.ssubook.Post2");
        intent.setComponent(componentName);
        intent.putExtra("activity", 1);
        startActivity(intent);
    }



    public void onTermsButtonClick( View v )
    {
        Intent intent = new Intent(Mypage.this, ServiceActivity.class);
        startActivity(intent);
    }

    public void onMessage( View v )
    {
        Intent intent = new Intent(Mypage.this, Message.class);
        startActivity(intent);
    }

}