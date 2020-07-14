package com.example.firstproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewContact extends AppCompatActivity {
    private DatabaseReference mPostReference;
    String name="", number="", review="", img="";
    double lat=0.0F, lng=0.0F;
    String sort="name";
    EditText nameET,numberET, reviewET;
    Button btn; //??

    String templat="", templon="";

    Button searchButton;

    //image
    private static final int PICK_IMAGE=777;
    private StorageReference mStorageRef;
    Uri currentImageUri;
    boolean check;

    ArrayList<String> data;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);

        data=new ArrayList<String>();
        nameET=(EditText) findViewById(R.id.addName);
        numberET=(EditText) findViewById(R.id.addNumber);
        reviewET=(EditText) findViewById(R.id.addReview);

        mPostReference= FirebaseDatabase.getInstance().getReference();
        mStorageRef= FirebaseStorage.getInstance().getReference("Images");

        ////////////////////////LNGLAT///////////////////////////////////
        searchButton = (Button) findViewById(R.id.search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                EditText editText = (EditText) findViewById(R.id.addName);
                String address = editText.getText().toString();

                GeocodingLocation locationAddress = new GeocodingLocation();
                locationAddress.getAddressFromLocation(address, getApplicationContext(), new GeocoderHandler());

            }
        });
        //////////////////////////////////////////////////////////////////

        Button login = (Button)findViewById(R.id.signupButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Firebase button parse
                name=nameET.getText().toString();
                number=numberET.getText().toString();
                review=reviewET.getText().toString();
                Log.d("upload", templat+" "+templon);

                // 버튼 클릭시 존재하는 아이디인지 ??
                if((name.length()*number.length())==0){
                    Toast.makeText(NewContact.this,"Please fill in required values.", Toast.LENGTH_SHORT).show();
                }
                else {
                    DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("name_list");
                    Query query=ref.orderByChild("name").equalTo(name);

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getChildrenCount()>0) {
                                //username found
                                Toast.makeText(NewContact.this,"Restaurant already exists.", Toast.LENGTH_SHORT).show();

                            }else{
                                // username not found
                                Toast.makeText(NewContact.this,"add success", Toast.LENGTH_SHORT).show();

                                //Image upload
                                if(check){
                                    /*
                                    StorageReference riverseRef=mStorageRef.child(currentImageUri.getLastPathSegment());
                                    final UploadTask uploadTask=riverseRef.putFile(currentImageUri);
                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                            img = "https://sites.google.com/site/wqmbs2019/_/rsrc/1554518562382/direction/KI-map.png";
                                            Log.d("firebaseURL", String.valueOf(uploadTask.getResult()));
                                            Log.d("firebaseUrl", "snap: "+taskSnapshot.getStorage().getDownloadUrl());
                                            img = String.valueOf(uploadTask.getResult());
                                        }
                                    });*/

                                    final StorageReference riverseRef=mStorageRef.child(currentImageUri.getLastPathSegment());
                                    final UploadTask uploadTask=riverseRef.putFile(currentImageUri);
                                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                        @Override
                                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                            if(!task.isSuccessful()){
                                                //throw.task.getException();
                                            }
                                            return riverseRef.getDownloadUrl();
                                        }
                                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if(task.isSuccessful()){
                                                Uri downloadUri = task.getResult();
                                                Log.d("firebaseUrl1", String.valueOf(downloadUri));
                                                img = String.valueOf(downloadUri);
                                                setImgUrl(String.valueOf(downloadUri));
                                            }else{

                                            }
                                        }
                                    });
                                }
                                Log.d("inSetImgUrl22","this img "+img);
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

        ImageButton image=(ImageButton)findViewById(R.id.imagebtn);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery,PICK_IMAGE);
            }
        });
    }
    //////////////////////////////////////////////////////////////////////////
    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    Log.d("newcontact bundle", locationAddress);
                    String temp1 = bundle.getString("lat");
                    Log.d("bundle1",temp1);
                    String temp2 = bundle.getString("lon");
                    Log.d("bundle1",temp2);

                    templat=temp1;
                    templon=temp2;

                    break;
                default:
                    locationAddress = null;
            }
        }
    }

    public void setImgUrl(String url){
        this.img = url;
        Log.d("inSetImgUrl","this img"+this.img);
    }
    //////////////////////////////////////////////////////////////////////////

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE){
            ImageView img = (ImageView) findViewById(R.id.imagebtn);
            currentImageUri = data.getData();
            check=true;
            img.setImageURI(currentImageUri);
        }
    }

    public void postFirebaseDatabase(boolean add){
        Map<String,Object> childUpdates=new HashMap<>();
        Map<String,Object> postValues=null;
        if(add){
            FirebasePost post=new FirebasePost(name,number, review, img);
            Log.d("postFirebaseDatabase", "+++"+post.img);
            postValues=post.toMap();
        }
        childUpdates.put("/name_list/"+name,postValues);
        mPostReference.updateChildren(childUpdates);
        clearET();
    }

    public void postFirebaseDatabase2(boolean add){
        Map<String,Object> childUpdates=new HashMap<>();
        Map<String,Object> postValues=null;
        if(add){
            FirebasePlace post=new FirebasePlace(name,lat,lng);
            postValues=post.toMap();
        }
        childUpdates.put("/place_list/"+name,postValues);
        mPostReference.updateChildren(childUpdates);
        clearET();
    }

    public void clearET(){
        nameET.setText("");
        numberET.setText("");
        reviewET.setText("");
        name="";
        number="";
        review="";
    }
}