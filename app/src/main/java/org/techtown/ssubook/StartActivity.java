package org.techtown.ssubook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    private Button Login;
    private TextView SignUpText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin);
        Login = (Button) findViewById(R.id.button);
        SignUpText = (TextView)findViewById(R.id.textView3);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SignUpActivity 연결
                Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        SignUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(StartActivity.this, SignUpActivity.class);
                startActivity(Intent);
            }
        });
    }
}