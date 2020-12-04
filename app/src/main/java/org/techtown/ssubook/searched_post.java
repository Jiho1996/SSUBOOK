package org.techtown.ssubook;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
/*
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
*/
public class searched_post extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched_post);
/*
        ActionBar ab = getSupportActionBar();
        ab.setTitle("검색 결과");
*/
        String title;

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        Toast.makeText( this, title, Toast.LENGTH_LONG ).show();

        ListView listview ;
        ListViewAdapter adapter;
        adapter = new ListViewAdapter() ;
        listview = (ListView) findViewById(R.id.searched_post);
        listview.setAdapter(adapter);

        //정보 불러오기.
        /*
        FirebaseDatabase database = FirebaseDatabase.getInstance(); //database는 firebase의 root라 생각.
        DatabaseReference myRef = database.getReference("Post/0");

        final User[] users= new User[100];
        final int[] position = {0};

        myRef.addListenerForSingleValueEvent( //한 번만 데이터를 읽어오는 함수로 알고 썼습니다.
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {




                        //firebase를 만들지 않았어서 시행착오를 확인할 방법이 없어서 여러 개 만들어 놨습니다.

                        User user = dataSnapshot.getValue(User.class);
                        users[position[0]++]=user;




                        User[] user = new User[100];
                        int pos=0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            user[pos++]=(User)dataSnapshot.getValue(); //없으면 null 리턴.
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }


                }
        );
        */

        //int[] posts=search(users,title);
        //users[posts[0]]; //첫 번째 해당하는 문자.
        //우선 목표는 반복문으로 전부 보여주기. 안되면 최신 10건.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.ic_baseline_assignment_24),
                "1",
                "검색 글1") ; // 첫 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.ic_baseline_perm_identity_24),
                "2",
                "검색 글2") ; // 두 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.ic_baseline_search_24),
                "3",
                "검색 글3"); // 두 번째 아이템 추가.

    }
/*
    private int[] search(User[] users, String title){
        int[] post= new int[100];
        int position=0;
        for(int i=0; i<users.length; i++)
            if(match_string(title,users[i].title)==true)
                post[position]=i;
        return post;
    }

    private boolean match_string(String a, String b){
        for(int i=0; i<=a.length()-b.length(); i++)
            if(b.equals(a.substring(i,i+b.length())))
                return true;

        return false;
    }
    */
}