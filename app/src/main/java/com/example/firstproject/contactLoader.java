package com.example.firstproject;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.util.ArrayList;

class PhoneBook{
    private String id;
    private String name;
    private String number;

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setId(String id) {
        this.id = id;
    }
    PhoneBook(){ }
}

public class contactLoader {
    public static ArrayList<PhoneBook> getData(Context context){
        ArrayList<PhoneBook> datas=new ArrayList<>();
        ContentResolver resolver=context.getContentResolver();
        Uri phoneUri= ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String proj[]={ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};
        String sortOrder="case"+
                " when substr(" + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+", 1,1) BETWEEN 'ㄱ' AND '힣' then 1 "+
                " when substr(" + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+", 1,1) BETWEEN 'A' AND 'Z' then 2 "+
                " when substr(" + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+", 1,1) BETWEEN 'a' AND 'z' then 3 "+
                " else 4 end, " + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" COLLATE LOCALIZED ASC";
        Cursor cursor=resolver.query(phoneUri,proj,null,null,sortOrder);
        if(cursor!=null){
            while(cursor.moveToNext()){
                int index=cursor.getColumnIndex(proj[0]);
                String id=cursor.getString(index);

                index=cursor.getColumnIndex(proj[1]);
                String name=cursor.getString(index);

                index=cursor.getColumnIndex(proj[2]);
                String number=cursor.getString(index);


                PhoneBook book=new PhoneBook();
                book.setId(id);
                book.setName(name);
                book.setNumber(number);

                datas.add(book);

            }
        }
        cursor.close();
        return datas;
    }
}