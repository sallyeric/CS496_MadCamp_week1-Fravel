package com.example.firstproject;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

public class ImageFinder {
    private Context mContext;
    public ImageFinder(Context context) {
        mContext = context;
    }
    public List<imgFormat> getAllPhotoPathList() {
        ArrayList<imgFormat> photoList = new ArrayList<>();
        Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;// 안드로이스 시스템에서 제공하는 미디어 데이터 DB
        String[] projection = {
                MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.DATE_ADDED // 파일과 관련된 컬럼을 나타내는 상수값 가져옴
        };
        Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null, null); // 그 안에서의 이동 및 쿼리, 다 가져옴
        int columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        while (cursor.moveToNext()) {
            imgFormat now = new imgFormat(cursor.getString(columnIndexData),false);
            photoList.add(now); //채워줌
        }
        cursor.close();
        return photoList;
    }
}
