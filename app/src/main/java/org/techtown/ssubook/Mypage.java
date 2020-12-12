package org.techtown.ssubook;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Mypage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        setTitle("마이페이지");
/*
        ActionBar ab = getSupportActionBar();
        ab.setTitle("마이페이지");
*/
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
                "org.techtown.ssubook.Post");
        intent.setComponent(componentName);
        intent.putExtra("activity", 1);
        startActivity(intent);
    }


    public void onTermsButtonClick( View v )
    {
        Toast.makeText( this, "Terms of Use 접근중", Toast.LENGTH_LONG ).show();

        Intent intent = new Intent();
        ComponentName componentName = new ComponentName (
                "org.techtown.ssubook",
                "org.techtown.ssubook.MainActivity");
        intent.setComponent(componentName);
        startActivity(intent);

    }

}