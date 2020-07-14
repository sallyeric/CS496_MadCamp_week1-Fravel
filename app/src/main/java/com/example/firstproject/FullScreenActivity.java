package com.example.firstproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class FullScreenActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_screen_image);
        Log.d("FullScreenActivity","onCreate:started");
        getIncomingIntent();

        ImageButton close = (ImageButton) findViewById(R.id.closeWindow);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
    private void getIncomingIntent(){
        Log.d("FULL", "getIncomingIntent: check");
        String imageUrl = getIntent().getStringExtra("image_url");
        String imagetitle = getIntent().getStringExtra("image_title");
        setImage(imageUrl, imagetitle);
    }
    private void setImage(String imageUrl, String title){
        Log.d("FULL","full screen image to " + imageUrl);
        ImageView img = findViewById(R.id.fullScreenImageView);
        TextView img_title = findViewById(R.id.imgtitle);

        Log.d("FULL","img view success");

        Glide.with(this)
                //.load(imageUrls.get(i).getImageUrl()) // 웹 이미지 로드
                .load(imageUrl) // imageUrl
                .thumbnail(0.1f)
                .error(R.drawable.imagenotfound)
                .into(img);
        img_title.setText(title);
        Log.d("FULL","image tile="+title);
    }

}
