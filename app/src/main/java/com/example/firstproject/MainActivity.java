package com.example.firstproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Movie;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mPostReference;
    String name = "";
    String number ="";

    TextView textView;

    ArrayList<String> data;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data = new ArrayList<String>();
        textView=(TextView) findViewById(R.id.textview);

        mPostReference = FirebaseDatabase.getInstance().getReference();
        Query query = mPostReference.equalTo("name");

        //arrayAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        //listView.setAdapter(arrayAdapter);

        //getFirebaseDatabase();

    }

    /*
    public void getFirebaseDatabase(){
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                data.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    String key = postSnapshot.getKey();
                    FirebasePost get = postSnapshot.getValue(FirebasePost.class);
                    String[] info = {String.valueOf(get.name),String.valueOf(get.number)};
                    String result = info[0] + " : "  + info[1];
                    data.add(result);
                    Log.d("getFirebaseDatabase","info: "+info[0]+ info[1]);

                }
                arrayAdapter.clear();
                arrayAdapter.addAll(data);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mPostReference.child("namelist").addValueEventListener(postListener);
    }*/
}