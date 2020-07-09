package com.example.firstproject;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import static com.example.firstproject.Fragment2.screenWidth;

public class ImageAdapter extends BaseAdapter{
    private Context mContext;
    private List<imgFormat> mPhotoList;
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
        imageView.setLayoutParams(new GridView.LayoutParams(screenWidth/3-50, screenWidth/3-50));
        imageView.setPadding(5,5,5,5);
        Log.d("getView inside", String.valueOf(screenWidth));
        return imageView;
    }


    // create a new ImageView for each item referenced by the Adapter
    /*
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = vi.inflate(R.layout.galchild, null);
        try {

            ImageView imageView = (ImageView) v.findViewById(R.id.ImageView01);
            //imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            // imageView.setPadding(8, 8, 8, 8);
            Bitmap bmp = decodeURI(mUrls[position].getPath());
            //BitmapFactory.decodeFile(mUrls[position].getPath());
            imageView.setImageBitmap(bmp);
            //bmp.
            TextView txtName = (TextView) v.findViewById(R.id.TextView01);
            txtName.setText(mNames[position]);
        } catch (Exception e) {
        }
        return v;
    }
    */

    /**
     * This method is to scale down the image
     */
    public Bitmap decodeURI(String filePath){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        // Only scale if we need to
        // (16384 buffer for img processing)
        Boolean scaleByHeight = Math.abs(options.outHeight - 100) >= Math.abs(options.outWidth - 100);
        if(options.outHeight * options.outWidth * 2 >= 16384){
            // Load, scaling to smallest power of 2 that'll get it <= desired dimensions
            double sampleSize = scaleByHeight
                    ? options.outHeight / 100
                    : options.outWidth / 100;
            options.inSampleSize =
                    (int)Math.pow(2d, Math.floor(
                            Math.log(sampleSize)/Math.log(2d)));
        }
        // Do the actual decoding
        options.inJustDecodeBounds = false;
        options.inTempStorage = new byte[512];
        Bitmap output = BitmapFactory.decodeFile(filePath, options);
        return output;
    }
}
