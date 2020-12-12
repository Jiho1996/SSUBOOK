package org.techtown.ssubook;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private EditText editTextPhone;
    private EditText editTextPassword;
    private EditText editTextName;
    private EditText editTextPasswordConfirm;
    private Button buttonJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();

        editTextPhone = (EditText) findViewById(R.id.phone_num);
        editTextPassword = (EditText) findViewById(R.id.password);
        editTextName = (EditText) findViewById(R.id.nick_name);
        editTextPasswordConfirm = (EditText) findViewById(R.id.password2);

        buttonJoin = (Button) findViewById(R.id.button2);
        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextName.getText().toString().equals("")&&!editTextPhone.getText().toString().equals("")
                        && !editTextPassword.getText().toString().equals("") && !editTextPasswordConfirm.getText().toString().equals((""))) {
                    // 번호와 비밀번호가 공백이 아닌 경우
                    createUser(editTextPhone.getText().toString(), editTextPassword.getText().toString(), editTextName.getText().toString());
                } else {
                    // 번호와 비밀번호가 공백인 경우
                    Toast.makeText(SignUpActivity.this, "정보를 전부 입력하세요.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void createUser(String phone, String password, String name) {
        firebaseAuth.createUserWithEmailAndPassword(phone, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 회원가입 성공시
                            Toast.makeText(SignUpActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            // 계정이 중복된 경우
                            Toast.makeText(SignUpActivity.this, "이미 존재하는 계정입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}