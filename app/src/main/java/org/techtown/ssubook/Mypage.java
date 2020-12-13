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
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        setTitle("마이페이지");

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        //Log.d("abc", currentUser.getUid());



        TextView textView=(TextView)findViewById(R.id.name_text);
        textView.setText("김택민");
        //Log.d("abc", currentUser.getEmail());
        //textView.setText(currentUser.getUid());

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

}