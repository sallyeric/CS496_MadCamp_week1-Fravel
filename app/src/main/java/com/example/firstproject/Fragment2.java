package com.example.firstproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Fragment2  extends Fragment implements ImageAdapter.OnListItemSelectedInterface, ImageAdapter.OnListItemLongSelectedInterface{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    GridView gridView;
    static int screenWidth;
    private ImageView imageView;
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    private GalleryManager mGalleryManager;
    public ArrayList<imgFormat> localPhotoList;

    String signupUsername="";

    private DatabaseReference mDatabaseRef;
    ImageAdapter dataAdapter;


    private ArrayList<Item> list = new ArrayList<>();
    static ArrayList<ImageUrl> imageUrlList = new ArrayList<>();

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;

    FirebaseStorage storage;
    StorageReference storageReference;

    private StorageReference mStorageRef;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExploreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment2 newInstance(String param1, String param2) {
        Fragment2 fragment = new Fragment2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onItemSelected(View v, int position) {
        ImageAdapter.ViewHolder viewHolder = (ImageAdapter.ViewHolder)recyclerView.findViewHolderForAdapterPosition(position);

        //Toast.makeText(getActivity().getApplicationContext(), position+" pressed!" , Toast.LENGTH_SHORT).show(); //여기
        //Intent fullScreenIntent=new Intent(getActivity().getApplicationContext(), FullScreenActivity.class);
        //fullScreenIntent.putExtra("imgPath", position);
        //v.imageView.getContext().startActivity(fullScreenIntent);
    }

    @Override
    public void onItemLongSelected(View v, int position) {
        Toast.makeText(getActivity().getApplicationContext(), position + " long pressed!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Log.d("dispsize", "calculate the display size");
        //DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        //screenWidth = metrics.widthPixels;
        //Log.d("dispsize", String.valueOf(screenWidth));
        View v = inflater.inflate(R.layout.fragment_2, container, false);

        //Intent
        Intent postPageIntent = getActivity().getIntent();
        String username = postPageIntent.getStringExtra("Username");
        //Log.d("FRAGMENT2 USERNAME", username);
        signupUsername=username;

        imageView = (ImageView) v.findViewById(R.id.imageView);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView2);
        gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        /*
        mPostReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("name_list");
        final Query query = ref.orderByChild("name");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    getFirebaseDatabase();
                } else {// username not found

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Toast.makeText(MainActivity.this,"Error", Toast.LENGTH_SHORT).show();
            }
        });
        */

        mGalleryManager = new GalleryManager(getActivity().getApplicationContext());
        localPhotoList = mGalleryManager.getAllPhotoPathList();
        dataAdapter = new ImageAdapter(getActivity().getApplicationContext(), imageUrlList, localPhotoList, this, this);
        recyclerView.setAdapter(dataAdapter);


        mDatabaseRef = FirebaseDatabase.getInstance().getReference("name_list");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //list.clear();
                imageUrlList.clear();
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    String key=postSnapshot.getKey();
                    FirebasePost get=postSnapshot.getValue(FirebasePost.class);
                    String[] info={get.name,get.number,get.img, get.review};
                    Item result= new Item(info[0],info[1]);

                    //list.add(result);
                    if(info[2]!=null){
                        ImageUrl imageUrl = new ImageUrl();
                        imageUrl.setImageUrl(info[2]);
                        imageUrl.setImageTitle(info[0]);
                        imageUrl.setImageReview(info[3]);
                        imageUrlList.add(imageUrl);
                        Log.d("imgSaved", String.valueOf(imageUrlList.size()));
                    }
                }
                for (int i = 0; i < imageUrlList.size(); i++){
                    Log.d("images", ">> " + imageUrlList.get(i).getImageUrl());
                }
                Log.d("images", "List count: " + imageUrlList.size());
                dataAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Toast.makeText(fragement,"Error", Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton newContact = (ImageButton) v.findViewById(R.id.newContact);
        newContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent
                Context context2 = v.getContext();                                                    // Context 수정
                Intent newPostIntent = new Intent(context2, NewContact.class);
                startActivity(newPostIntent);
            }
        });
        return v;

        //ArrayList firebaseimglist = prepareData();
        /*
        mGalleryManager = new GalleryManager(getActivity().getApplicationContext());
        localPhotoList = mGalleryManager.getAllPhotoPathList();
        ImageAdapter dataAdapter = new ImageAdapter(getActivity().getApplicationContext(), imageUrlList, localPhotoList, this, this);
        recyclerView.setAdapter(dataAdapter);
         */

        // Inflate the layout for this fragment
    }
/*
    private ArrayList prepareData() {

// here you should give your image URLs and that can be a link from the Internet
        final String imageUrls[] = {
                "https://image.dongascience.com/Photo/2018/03/c4a9b9c58a79029437f7563bcc9d92e3.jpg",
                "https://www10.aeccafe.com/blogs/arch-showcase/files/2013/11/kaist-01.jpg",
                "https://i1.wp.com/blockinpress.com/wp-content/uploads/2018/09/DSC08202.jpg?fit=640%2C428&ssl=1",
                "https://sites.google.com/site/wqmbs2019/_/rsrc/1554518562382/direction/KI-map.png"};

        Log.d("FirebaseImage","loading start");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        StorageReference imageRef = storageReference
                .child("Images/11.jpeg");
        Log.d("FirebaseImage","loading complete");
        //ArrayList imageUrlList = new ArrayList<>();
        imageRef.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("FirebaseImage", "Download Url is "+uri.toString());
                        ImageUrl imageUrl = new ImageUrl();
                        imageUrl.setImageUrl(uri.toString());
                        imageUrlList.add(imageUrl);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("FirebaseImage", "loading fail");
            }
        });
        for (int i = 0; i < imageUrlList.size(); i++){
            Log.d("images", ">>" + imageUrlList.get(i).getImageUrl());
        }
        Log.d("images", "List count: " + imageUrlList.size());

        mPostReference = FirebaseDatabase.getInstance().getReference();

        for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
            String key=postSnapshot.getKey();
            FirebasePost get=postSnapshot.getValue(FirebasePost.class);
            String[] info={get.name,get.number};
            Item result= new Item(info[0],info[1]); //수정 !!!

            list.add(result);
            Log.d("getFirebaseDatabase","key: "+key);
            Log.d("getFirebaseDatabase","info: "+info[0]+" "+info[1]);
            Log.d("ListSize",String.valueOf(list.size()));
        }
        return imageUrlList; // ArrayList : ImageUrl이 저장됨
    }
*/
}
