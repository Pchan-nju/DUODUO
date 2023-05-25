package com.pchan.duoduo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

public class WelcomeActivity extends AppCompatActivity {
    private int[] arrayPicture = new int[]{R.drawable.image1, R.drawable.image2};
    private ImageSwitcher imageSwitcher;
    private int index = 0;
    //手指按下和抬起的x坐标
    private float touchDownX;
    private float touchUpX;

    private void updateButtonText(){
        AppCompatButton skipButton = findViewById(R.id.SkipButton);
        if(index == arrayPicture.length - 1){
            skipButton.setText("START");
        }else {
            skipButton.setText("SKIP");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Button button = findViewById(R.id.SkipButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, FirstSetActivity.class);
                startActivity(intent);
            }
        });

        //实现图片切换
        imageSwitcher = findViewById(R.id.imageSwitcher);
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(WelcomeActivity.this);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER); //设置ImageView缩放类型为FIT_CENTER
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setImageResource(arrayPicture[index]);
                return imageView;
            }
        });


        imageSwitcher.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    touchDownX = event.getX();
                    return true;
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    touchUpX = event.getX();
                    if(touchUpX - touchDownX > 100 && index > 0){
                        index--;
                        imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(WelcomeActivity.this, R.anim.slide_in_left));
                        imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(WelcomeActivity.this, R.anim.slide_out_right));
                        imageSwitcher.setImageResource(arrayPicture[index]);
                    }else if(touchDownX - touchUpX > 100 && index < arrayPicture.length - 1){
                        index++;
                        imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(WelcomeActivity.this, R.anim.slide_in_right));
                        imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(WelcomeActivity.this, R.anim.slide_out_left));
                        imageSwitcher.setImageResource(arrayPicture[index]);
                    }
                    updateButtonText();
                    return true;
                }
                return false;
            }
        });
    }
}