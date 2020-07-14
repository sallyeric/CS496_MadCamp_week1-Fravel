package com.example.firstproject;


import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Dictionary;

public class Fragment1 extends Fragment implements SimpleTextAdapter.OnListItemLongSelectedInterface, SimpleTextAdapter.OnListItemSelectedInterface{

    private RecyclerView recyclerView;                                                              // RV

    /////////////////////////////////////////////////////////
    private GoogleMap mMap;
    private Geocoder geocoder;
    private Button button;
    private EditText editText;
    /////////////////////////////////////////////////////////

    // 옮겨온 변수들
    private DatabaseReference mPostReference;
    String name="";
    String number="";
    String address="";
    ListView listView;

    //ArrayList<Item> list; // 아래에 수정
    private ArrayList<Item> list = new ArrayList<>();
    public ArrayList<ImageUrl> imageUrlList = new ArrayList<ImageUrl>();
    SimpleTextAdapter dataAdapter;
    SimpleTextAdapter adapter;
    private DatabaseReference mDatabaseRef;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment1() {
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
    public static Fragment1 newInstance(String param1, String param2) {
        Fragment1 fragment = new Fragment1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
    public void onItemSelected(View v, int position) {
        SimpleTextAdapter.Holder viewHolder = (SimpleTextAdapter.Holder)recyclerView.findViewHolderForAdapterPosition(position);
        Toast.makeText(this.getContext(),  " long clicked", Toast.LENGTH_SHORT).show();
        Log.d("test","long clicked");
    }

    @Override
    public void onItemLongSelected(View v, int position) {
        Log.d("tab1test","clicked");
        Toast.makeText(getActivity().getApplicationContext(), position+" long pressed!" , Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_1, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView1);
        mPostReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("name_list");
        final Query query = ref.orderByChild("name");
        /*
        final ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.progress);


        Context context1 = v.getContext();                                                            // Context 수정
        list = Item.createContactsList(0);
        //adapter.addMoreContacts(Item.createContactsList(20));                          //RV2
        recyclerView.setHasFixedSize(true);
        adapter = new SimpleTextAdapter(getActivity().getApplicationContext(), list, this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(context1));                           // RV
        recyclerView.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(getActivity().getApplicationContext(),
                        new LinearLayoutManager(getActivity().getApplicationContext()).getOrientation());
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        recyclerView.addItemDecoration(dividerItemDecoration);

        Log.d("Tab1","recyclerView complete");
        progressBar.setVisibility(View.GONE);
        */

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    getFirebaseDatabase();
                } else {
                    //user not found
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Toast.makeText(MainActivity.this,"Error", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setHasFixedSize(true);
        adapter = new SimpleTextAdapter(getActivity().getApplicationContext(), list, this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        recyclerView.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(getActivity().getApplicationContext(),
                        new LinearLayoutManager(getActivity().getApplicationContext()).getOrientation());
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        recyclerView.addItemDecoration(dividerItemDecoration);

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
    }

    public void getFirebaseDatabase(){
        final ValueEventListener postListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("onDataChange","Data is Updated");
                list.clear();
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    String key=postSnapshot.getKey();
                    FirebasePost get=postSnapshot.getValue(FirebasePost.class);
                    String[] info={get.name,get.number,get.img};
                    Item result= new Item(info[0],info[1]); //수정 !!!

                    list.add(result);
                    Log.d("getFirebaseDatabase","key: "+key);
                    Log.d("getFirebaseDatabase","info: "+info[0]+" "+info[1]+" "+info[2]);
                    Log.d("ListSize",String.valueOf(list.size()));
                    ImageUrl imageUrl = new ImageUrl();
                    imageUrl.setImageUrl(info[2]);
                    imageUrl.setImageTitle(info[0]);

                    imageUrlList.add(imageUrl);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mPostReference.child("name_list").addValueEventListener(postListener);
    }

    //return v;

}
