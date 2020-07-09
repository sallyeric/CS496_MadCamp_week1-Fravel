package com.example.firstproject;
import android.content.Context;
import android.media.Image;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter{
    private Context mContext;
    public int[] imageArray={
        R.drawable.img1, R.drawable.img2, R.drawable.img3,
            R.drawable.img4, R.drawable.img5, R.drawable.img6,
            R.drawable.img7, R.drawable.img8, R.drawable.img9,
            R.drawable.img10,  R.drawable.img11, R.drawable.img12,
            R.drawable.img13, R.drawable.img14, R.drawable.img15,
            R.drawable.img16, R.drawable.img17
    };
    private int itemWidth;


    public ImageAdapter(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public int getCount(){
        return imageArray.length;
    }

    @Override
    public Object getItem(int position){
        return imageArray[position];
    }

    @Override
    public long getItemId(int position){
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        //DisplayMetrics metrics = this.getResources().getDisplayMertrics();
       //int screenWidth = metrics.widthPixels;
        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(imageArray[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(400, 400));
        imageView.setPadding(5,5,5,5);

        return imageView;
    }
}
