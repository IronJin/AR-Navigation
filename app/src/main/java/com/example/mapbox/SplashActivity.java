
package com.example.mapbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
//로딩화면을 위한 splash-activity

public class SplashActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        try{

            Thread.sleep(2000); //2초동안 화면을 표시하는 메서드


        }catch (InterruptedException e) {

            e.printStackTrace();

        }


        Intent intent=new Intent(this, LoginActivity.class);
        //이 액티비티 이후에 어떤 액티비티로 넘어갈지 설정해줌 (이번것은 이후에 메인으로 넘어감)

        startActivity(intent);

        finish();




    }

}