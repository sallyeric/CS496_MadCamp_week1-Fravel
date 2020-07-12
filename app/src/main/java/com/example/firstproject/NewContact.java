package com.example.firstproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    String name="", number="", address="";
    String sort="name";
    EditText nameET,numberET, addressET;
    Button btn; //??

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
        addressET=(EditText) findViewById(R.id.addAddress);

        mPostReference= FirebaseDatabase.getInstance().getReference();
        mStorageRef= FirebaseStorage.getInstance().getReference("Images");

        Button login = (Button)findViewById(R.id.signupButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Firebase button parse
                name=nameET.getText().toString();
                number=numberET.getText().toString();
                address=addressET.getText().toString();

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

                                postFirebaseDatabase(true);

                                //Image upload
                                if(check){
                                    StorageReference riverseRef=mStorageRef.child(currentImageUri.getLastPathSegment());
                                    UploadTask uploadTask=riverseRef.putFile(currentImageUri);
                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                        }
                                    });
                                }


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