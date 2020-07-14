package com.example.firstproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;

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
        String imagereview = getIntent().getStringExtra("image_review");
        setImage(imageUrl, imagetitle, imagereview);
    }
    private void setImage(String imageUrl, String title, String review){
        Log.d("FULL","full screen image to " + imageUrl);
        //ImageView img = findViewById(R.id.fullScreenImageView);
        PhotoView img = findViewById(R.id.fullScreenImageView);
        TextView img_title = findViewById(R.id.imgtitle);
        TextView img_review = findViewById(R.id.imgreview);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress);

        Log.d("FULL","img view success");

        Glide.with(this)
                //.load(imageUrls.get(i).getImageUrl()) // 웹 이미지 로드
                .load(imageUrl) // imageUrl
                .listener( new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .thumbnail(0.1f)
                .error(R.drawable.imagenotfound)
                .into(img);
        img_title.setText(title);
        img_review.setText("\""+review+"\"");
        Log.d("FULL","image_tile="+title);
        Log.d("FULL","image_review="+review);
    }

}
