package com.example.mapbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth; //파베 인증처리
    private DatabaseReference mDatabaseRef;//실시간
    private EditText mEtId, mEtPwd;

    EditText editText1, editText2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mFirebaseAuth = FirebaseAuth.getInstance(); //firebase 이용준비 끝
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("mapbox");

        mEtId = findViewById(R.id.idText);
        mEtPwd = findViewById(R.id.passwordText);

        Button btn_login = findViewById(R.id.btn_Login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //로그인 요청
                String strEmail = mEtId.getText().toString();
                String strPwd = mEtPwd.getText().toString();

                if(strEmail.getBytes().length <= 0 || strPwd.getBytes().length <= 0){
                    Toast.makeText(LoginActivity.this, "로그인 정보를 입력하세요", Toast.LENGTH_SHORT).show();
                }else {

                mFirebaseAuth.signInWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete( @NotNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //로그인 성공시 MainAcitivity로 연결
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            //finish(); //현재액티비티 파괴
                        } else {
                            Toast.makeText(LoginActivity.this , "로그인 실패", Toast.LENGTH_SHORT).show();
                           //바뀐 부분 로그인실패시 로그인 화면으로 다시 이동
                            Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                });}
            }
        });

<<<<<<< Updated upstream
        Button btn_register = findViewById(R.id.registerButton);
=======
        TextView btn_register = (TextView) findViewById(R.id.btn_register);
>>>>>>> Stashed changes
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //회원가입 버튼을 눌렀을 때의 처리 -> 회원가입 창으로 이동
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(intent);
            }
        });
    }
}