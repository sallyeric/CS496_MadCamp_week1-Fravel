package com.example.firstproject;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class GalleryManager {
    private Context mContext;

    public GalleryManager(Context context) {
        mContext = context;
    }

    public ArrayList getAllPhotoPathList() {
        ArrayList photoList = new ArrayList<>();

        Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.DATE_ADDED
        };
        Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null, null);
        int columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        while (cursor.moveToNext()){
            imgFormat temp = new imgFormat(cursor.getString(columnIndexData),false);
            photoList.add(temp);
            Log.d("GalleryManager", "path->" + temp.getImgPath());
        }
        cursor.close();
        Log.d("GalleryManager", "List count: " + photoList.size());
        return photoList;

        /*
        ArrayList imageUrlList = new ArrayList<>();
        for (int i = 0; i < imageUrls.length; i++) {
            ImageUrl imageUrl = new ImageUrl();
            imageUrl.setImageUrl(imageUrls[i]);
            imageUrlList.add(imageUrl);
        }
        Log.d("Fragment2", "List count: " + imageUrlList.size());
        return imageUrlList;
        */
    }
}
