package com.save.smartsave;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Explode;
import android.transition.Transition;
import android.view.WindowManager;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {
    ImageView img;
    PushNotification pushNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        pushNotification=PushNotification.getmInstance();

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        img=findViewById(R.id.splash_image_1);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent =new Intent(SplashScreen.this,LoginScreen.class);
                ActivityOptionsCompat options=ActivityOptionsCompat.makeSceneTransitionAnimation(SplashScreen.this,img,"splash_icon");
                startActivity(intent,options.toBundle());
                finish();
            }
        }, 1000);

    }
}
