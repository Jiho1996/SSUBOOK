package org.techtown.ssubook;

import android.os.Bundle;

import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private EditText editTextPhone;
    private EditText editTextPassword;
    private EditText editTextName;
    private EditText editTextPasswordConfirm;
    private Button buttonJoin;
    private static final String TAG = "SignUpActivity";

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
                    if (editTextPassword.equals(editTextPasswordConfirm)) {
                        createUser(editTextPhone.getText().toString(), editTextPassword.getText().toString(), editTextName.getText().toString());
                        Toast.makeText(SignUpActivity.this, "회원가입 완료.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SignUpActivity.this, "비밀번호를 확인해주세요.", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    // 번호와 비밀번호가 공백인 경우
                    Toast.makeText(SignUpActivity.this, "정보를 빠짐없이 입력하세요.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

    }

    private void createUser(String phone, String password, String name) {
        firebaseAuth.createUserWithEmailAndPassword(phone, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 회원가입 성공시
                            Log.d(TAG, "createUserWithPhone:success");
                            Toast.makeText(SignUpActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {

                            if(task.getException() != null ) {
                                Log.d(TAG, "createUserWithPhone:failure", task.getException());
                                Toast.makeText(SignUpActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

}