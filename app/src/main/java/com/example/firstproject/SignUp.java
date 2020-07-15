package com.example.firstproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class SignUp extends AppCompatActivity {

    private DatabaseReference mPostReference;
    String signupUsername="", signupPassword="", signupFullname="",
            signupBirthday="", signupEmail="";
    String sort="id";
    EditText usernameET,passwordET,fullnameET,birthdayET,emailET;
    Button btn; //??

    ArrayList<String> data;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        data=new ArrayList<String>();
        usernameET=(EditText) findViewById(R.id.signupUsername);
        passwordET=(EditText) findViewById(R.id.signupPassword);
        fullnameET=(EditText) findViewById(R.id.signupFullname);
        birthdayET=(EditText) findViewById(R.id.signupBirthday);
        emailET=(EditText) findViewById(R.id.signupEmail);

        mPostReference= FirebaseDatabase.getInstance().getReference();

        TextView login = (TextView) findViewById(R.id.signupButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Firebase button parse
                signupUsername=usernameET.getText().toString();
                signupPassword=passwordET.getText().toString();
                signupFullname=fullnameET.getText().toString();
                signupBirthday=birthdayET.getText().toString();
                signupEmail=emailET.getText().toString();

                // 버튼 클릭시 존재하는 아이디인지 ??
                if((signupUsername.length()*signupPassword.length()*signupFullname.length()*
                        signupBirthday.length()*signupEmail.length())==0){
                    Toast.makeText(SignUp.this,"Please fill all blanks", Toast.LENGTH_SHORT).show();
                }
                else {
                    DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("id_list");
                    Query query=ref.orderByChild("signupUsername").equalTo(signupUsername);

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getChildrenCount()>0) {
                                //username found
                                Toast.makeText(SignUp.this,"Please use another username", Toast.LENGTH_SHORT).show();

                            }else{
                                // username not found
                                Toast.makeText(SignUp.this,"You can use this username", Toast.LENGTH_SHORT).show();

                                postFirebaseDatabase(true);
                                //Intent
                                EditText username = (EditText)findViewById(R.id.signupUsername);
                                Intent signupIntent = new Intent(SignUp.this, LoginActivity.class);
                                signupIntent.putExtra("Username", username.getText().toString());
                                startActivity(signupIntent);
                            }

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(SignUp.this,"Error", Toast.LENGTH_SHORT).show();

                        }
                    });

                    /*
                    if(query==null){
                        postFirebaseDatabase(true);
                        //Intent
                        EditText username = (EditText)findViewById(R.id.signupUsername);
                        Intent signupIntent = new Intent(SignUp.this, MainActivity.class);
                        signupIntent.putExtra("Username", username.getText().toString());
                        startActivity(signupIntent);
                    }else{
                        Toast.makeText(SignUp.this,"Please use another username", Toast.LENGTH_SHORT).show();
                    }
 */
                }


            }
        });

    }

    public void postFirebaseDatabase(boolean add){
        Map<String,Object> childUpdates=new HashMap<>();
        Map<String,Object> postValues=null;
        if(add){
            FirebaseUserInfo post=new FirebaseUserInfo(signupUsername,signupPassword,signupFullname,
                    signupBirthday,signupEmail);
            postValues=post.toMap();
        }
        childUpdates.put("/id_list/"+signupUsername,postValues);
        mPostReference.updateChildren(childUpdates);
        clearET();
    }

    public void clearET(){
        usernameET.setText("");
        passwordET.setText("");
        fullnameET.setText("");
        birthdayET.setText("");
        emailET.setText("");
        signupUsername="";
        signupPassword="";
        signupFullname="";
        signupBirthday="";
        signupEmail="";
    }
}