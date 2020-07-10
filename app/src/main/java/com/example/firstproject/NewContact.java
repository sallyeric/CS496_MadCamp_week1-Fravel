package com.example.firstproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewContact extends AppCompatActivity {
    private DatabaseReference mPostReference;
    String name="", number="";
    String sort="name";
    EditText nameET,numberET;
    Button btn; //??

    ArrayList<String> data;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);

        data=new ArrayList<String>();
        nameET=(EditText) findViewById(R.id.addName);
        numberET=(EditText) findViewById(R.id.addNumber);

        mPostReference= FirebaseDatabase.getInstance().getReference();

        Button login = (Button)findViewById(R.id.signupButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Firebase button parse
                name=nameET.getText().toString();
                number=numberET.getText().toString();

                // 버튼 클릭시 존재하는 아이디인지 ??
                if((name.length()*number.length())==0){
                    Toast.makeText(NewContact.this,"Please fill all blanks", Toast.LENGTH_SHORT).show();
                }
                else {
                    DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("name_list");
                    Query query=ref.orderByChild("name").equalTo(name);

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getChildrenCount()>0) {
                                //username found
                                Toast.makeText(NewContact.this,"Please use another username", Toast.LENGTH_SHORT).show();

                            }else{
                                // username not found
                                Toast.makeText(NewContact.this,"add success", Toast.LENGTH_SHORT).show();

                                postFirebaseDatabase(true);
                                //Intent
                                Intent signupIntent = new Intent(NewContact.this, MainActivity.class);
                                startActivity(signupIntent);
                            }

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(NewContact.this,"Error", Toast.LENGTH_SHORT).show();

                        }
                    });
                }


            }
        });

    }

    public void postFirebaseDatabase(boolean add){
        Map<String,Object> childUpdates=new HashMap<>();
        Map<String,Object> postValues=null;
        if(add){
            FirebasePost post=new FirebasePost(name,number);
            postValues=post.toMap();
        }
        childUpdates.put("/name_list/"+name,postValues);
        mPostReference.updateChildren(childUpdates);
        clearET();
    }

    public void clearET(){
        nameET.setText("");
        numberET.setText("");
        name="";
        number="";
    }
}