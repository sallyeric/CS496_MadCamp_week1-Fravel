package com.example.firstproject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        imageView = (ImageView) v.findViewById(R.id.imageView);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView2);
        gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        ArrayList imageUrlList = prepareData();
        mGalleryManager = new GalleryManager(getActivity().getApplicationContext());
        localPhotoList = mGalleryManager.getAllPhotoPathList();
        ImageAdapter dataAdapter = new ImageAdapter(getActivity().getApplicationContext(), imageUrlList, localPhotoList, this, this);
        recyclerView.setAdapter(dataAdapter);

        // Inflate the layout for this fragment
        return v;
    }

    private ArrayList prepareData() {
// here you should give your image URLs and that can be a link from the Internet
        String imageUrls[] = {
                "https://image.dongascience.com/Photo/2018/03/c4a9b9c58a79029437f7563bcc9d92e3.jpg",
                "https://img5.yna.co.kr/etc/inner/KR/2019/03/15/AKR20190315128100063_01_i_P2.jpg",
                "https://library.kaist.ac.kr/common/images/library_img_libraries.jpg",
                "https://lh3.googleusercontent.com/proxy/Xbk1e0S2qGfsNaMTDSQML51kF3U3YG629cjWy_rYn91BCWWaxnNjQa3nsOivnTeA13UcUZRYwx4oHMLc8WOxljjybpRGdSUKbysvOwTSUPMmttynu_2zYKHE3x9bpjkKCszmAk52",
                "https://post-phinf.pstatic.net/MjAxOTA5MTlfMjY1/MDAxNTY4ODg2OTAzNTY4.BVJLp9ehZy8ycWqj4BEmjD8vLFkbPtyK2NTpfARFWywg.iGzrVlGSrdIroOSw1punWIccxFVGah_a5S6-BSZEI70g.JPEG/1241.jpg?type=w1200",
                "https://cphoto.asiae.co.kr/listimglink/6/2020032314051136502_1584939911.jpg",
                "https://www.polytechnique.edu/sites/all/institutionnel/kaist_1.jpg",
                "https://besuccess.com/wp-content/uploads/2015/06/team-kaist.jpg",
                "https://www10.aeccafe.com/blogs/arch-showcase/files/2013/11/kaist-01.jpg",
                "https://i1.wp.com/blockinpress.com/wp-content/uploads/2018/09/DSC08202.jpg?fit=640%2C428&ssl=1",
                "https://sites.google.com/site/wqmbs2019/_/rsrc/1554518562382/direction/KI-map.png"};
        ArrayList imageUrlList = new ArrayList<>();
        for (int i = 0; i < imageUrls.length; i++) {
            ImageUrl imageUrl = new ImageUrl();
            imageUrl.setImageUrl(imageUrls[i]);
            imageUrlList.add(imageUrl);
        }
        Log.d("Fragment2", "List count: " + imageUrlList.size());
        return imageUrlList; // ArrayList : ImageUrl이 저장됨
    }

}
