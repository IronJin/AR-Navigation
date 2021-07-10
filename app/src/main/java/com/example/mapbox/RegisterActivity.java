package com.example.mapbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth; //Firebase 인증처리
    private DatabaseReference mDatabaseRef; //실시간 데이터베이스 -> 서버에 연동시킬수있는 객체
    private EditText mEtId, mEtPwd; //회원가입 입력필드
    private Button mBtnRegister; //회원가입 버튼

    private ArrayAdapter adapter;
    private Spinner spinner;

<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super .onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth= FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("AR-Navi");

        mEtId = findViewById(R.id.idText);
        mEtPwd = findViewById(R.id.passwordText);


        mBtnRegister =findViewById(R.id.registerButton);
        //회원가입 버튼이 클릭될때의 옵션처리
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strId = mEtId.getText().toString();
                String strPwd = mEtPwd.getText().toString();

                if(strId.getBytes().length <=0 || strPwd.getBytes().length <=0){
                    Toast.makeText(RegisterActivity.this ,"가입정보를 입력하세요.", Toast.LENGTH_SHORT).show();
                }else{

                //FirebaseAuth 진행
                mFirebaseAuth.createUserWithEmailAndPassword(strId, strPwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NotNull Task<AuthResult> task) {
                        //회원가입이 이루어졌을때 처리
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                            UserAccount account = new UserAccount();
                            account.setIdToken(firebaseUser.getUid());
                            account.setEmailId(firebaseUser.getEmail());
                            account.setPassword(strPwd);

                            //setValue 는 database에 삽입하는 행위
                            mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);

                            Toast.makeText(RegisterActivity.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);

                        } else {
<<<<<<< Updated upstream
                            Toast.makeText(RegisterActivity.this, "회원가입에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
=======
                            Toast.makeText(RegisterActivity.this, R.string.sing_up_fail, Toast.LENGTH_SHORT).show();
>>>>>>> Stashed changes
                            Intent intent = new Intent(RegisterActivity.this, RegisterActivity.class);
                            startActivity(intent);
                        }
                    }
                });

                }
            }
        });
        spinner = (Spinner) findViewById(R.id.majorSpinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.major, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}