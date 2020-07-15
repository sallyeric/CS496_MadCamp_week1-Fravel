package com.example.firstproject;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

//import noman.googleplaces.NRPlaces;
//import noman.googleplaces.Place;
//import noman.googleplaces.PlaceType;
//import noman.googleplaces.PlacesException;
//import noman.googleplaces.PlacesListener;

public class Fragment3  extends Fragment
        implements OnMapReadyCallback ,
        ActivityCompat.OnRequestPermissionsResultCallback{

    ////////////////////////////geocoding////////////////////////////
    ImageButton likeButton;
    ImageButton lookforButton;
    TextView addressTV;
    TextView latLongTV;
    EditText editText;

    /////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////
    private DatabaseReference mPostReference;
    //    String userInfo="";
//    //double userLong=0.0F;
//    //double userLat=0.0F;
//    String sort="userInfo";
//    //ArrayList<String> data;
//
    String name="";
    String score="";
    double lat=0.0f;
    double lng=0.0f;

    String signupUsername="";
//    ArrayList<Item2> list = new ArrayList<>();
//    ArrayAdapter<String> adapter;
//
//    Button btn;
    ////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////
    private GoogleMap mMap;
    ArrayList<LatLng>arrayList=new ArrayList<LatLng>();
    //LatLng suwon=new LatLng(36.3711705,127.36234309999999);
    //LatLng busan=new LatLng(36.3732444,127.36070219999999);
    private Marker currentMarker = null;

    private static final String TAG = "googlemap_example";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int UPDATE_INTERVAL_MS = 1000;  // 1초
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500; // 0.5초

    // onRequestPermissionsResult에서 수신된 결과에서 ActivityCompat.requestPermissions를 사용한 퍼미션 요청을 구별하기 위해 사용됩니다.
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    boolean needRequest = false;

    // 앱을 실행하기 위해 필요한 퍼미션을 정의합니다.
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    Location mCurrentLocatiion;
    LatLng currentPosition;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private Location location;

    private View mLayout;  // Snackbar 사용하기 위해서는 View가 필요합니다.
    // (참고로 Toast에서는 Context가 필요했습니다.)
    ////////////////////////////////////////////////////////////////

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment3() {
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
    public static Fragment3 newInstance(String param1, String param2) {
        Fragment3 fragment = new Fragment3();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    // 장소찾기
    List<Marker> previous_marker = null;

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
        // Inflate the layout for this fragment

        final View v = inflater.inflate(R.layout.fragment_3, null, false); // 원래 null 아니고 container

        Intent postPageIntent = getActivity().getIntent();
        String username = postPageIntent.getStringExtra("Username");
        //Log.d("FRAGMENT3 USERNAME", username);
        signupUsername=username;

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //////////////////////////////지오코딩///////////////////////////
        addressTV = (TextView) v.findViewById(R.id.addressTV);
        latLongTV = (TextView) v.findViewById(R.id.latLongTV);

        final EditText editText = (EditText) v.findViewById(R.id.addressET);
        editText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        editText.setOnEditorActionListener(editorListener);
        /////////////////////////////////////////////////////////////////

        ///////////////////////////사용자 정보////////////////////////////

        //btn=(Button)v.findViewById(R.id.button1);

        Context context4 = v.getContext();
//        AccountManager am = AccountManager.get(context4); // "this" references the current Context
//        Account[] accounts = am.getAccountsByType("com.google");
//        //Log.d("UserInformation",accounts.toString());
//        userInfo=accounts.toString();

//        data=new ArrayList<String>();
//
        mPostReference= FirebaseDatabase.getInstance().getReference();

        mLayout = v.findViewById(R.id.layout_main);

        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL_MS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);

        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder();

        builder.addLocationRequest(locationRequest);


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context4);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);

        //arrayList.add(suwon);
        //arrayList.add(busan);

        //장소찾기 버튼
        previous_marker = new ArrayList<Marker>();

        ///////////////////////////Firebase////////////////////////////////////
        final DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("place_list");
        final Query query=ref.orderByChild("name");

        likeButton = (ImageButton)v.findViewById(R.id.likeButton);
        lookforButton = (ImageButton)v.findViewById(R.id.lookforButton);

        lookforButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showPlaceInformation(currentPosition);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            // TODO: handle the post
                            String key=postSnapshot.getKey();
                            FirebasePlace get=postSnapshot.getValue(FirebasePlace.class);
                            String[] info={get.name,get.lat,get.lng};
                            //Item result= new Item(info[0],info[1],); //수정 !!!

                            Log.d("getFirebaseDatabase","key: "+key);
                            Log.d("getFirebaseDatabase","info: "+info[0]+info[1]+info[2]);
                            name=info[0];
                            lat=Double.valueOf(info[1]);
                            lng=Double.valueOf(info[2]);
                            Log.d("name: ", name);
                            Log.d("lat: ", String.valueOf(lat));
                            Log.d("lng: ", String.valueOf(lng));
                            moveCamera(new LatLng(lat, lng), DEFAULT_ZOOM, name);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getContext(),"Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        //////////////////////////////////////////////////////////////////////

        //////////////////////////Set Score////////////////////////////////////////

        final DatabaseReference ref2= FirebaseDatabase.getInstance().getReference().child("score_list");
        final Query query2=ref2.orderByChild("name");

        likeButton = (ImageButton)v.findViewById(R.id.likeButton);
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String nowText = editText.getText().toString();
                        if(nowText.length()==0){
                            return;
                        }
                        Boolean found=false;
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            // TODO: handle the post
                            String key=postSnapshot.getKey();
                            FirebaseScore get=postSnapshot.getValue(FirebaseScore .class);
                            String[] info={get.name,get.score};
                            //Log.d("POST[0]",postSnapshot.getKey());
                            //Item result= new Item(info[0],info[1],); //수정 !!!
                            // info[0] -> place list안에 들어가있는 이름들
                            if(info[0].equals(nowText)){
                                found=true;
                                Log.d("POST[0]if",postSnapshot.getKey());
                                name=editText.getText().toString();
                                int currentScore=Integer.parseInt(info[1]);
                                currentScore+=1;
                                Log.d("userName",info[0]);
                                Log.d("currentScore",String.valueOf(currentScore));
                                score=Integer.toString(currentScore);

                                Map<String,Object> childUpdates=new HashMap<>();
                                Map<String,Object> postValues=null;
                                Log.d("scoreUpdate->", String.valueOf(score));
                                FirebaseScore post=new FirebaseScore(info[0],score);
                                postValues=post.toMap();
                                Log.d("scoreUpdate", String.valueOf(postValues));
                                childUpdates.put("/score_list/"+info[0], postValues);
                                mPostReference.updateChildren(childUpdates);
                                //Query query3=ref2.orderByChild("name").equalTo(name).
                                //ref2.child(name).child("score").updateChildren(name,score);
                            }
                        }
                        if(found==false){

                            name=nowText;
                            int currentScore=1;
                            score=Integer.toString(currentScore);
                            Log.d("scoreUpdate->else", String.valueOf(score));

                            Map<String,Object> childUpdates=new HashMap<>();
                            Map<String,Object> postValues=null;
                            Log.d("scoreUpdate->", String.valueOf(score));
                            FirebaseScore post=new FirebaseScore(name, score);
                            postValues=post.toMap();
                            Log.d("scoreUpdate", String.valueOf(postValues));
                            childUpdates.put("/score_list/"+nowText, postValues);
                            mPostReference.updateChildren(childUpdates);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getContext(),"Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        ///////////////////////////////////////////////////////////////////////////

        return v;
    }

    public void postFirebaseDatabase2(boolean add){
        Map<String,Object> childUpdates=new HashMap<>();
        Map<String,Object> postValues=null;
        if(add){
            FirebaseScore post=new FirebaseScore(name,score);
            postValues=post.toMap();
        }
        Log.d("scoreUpdatepp", String.valueOf(postValues));
        childUpdates.put("/score_list/"+name,postValues);
        mPostReference.updateChildren(childUpdates);
        //clearET();
    }


    ///////////////////////////////200714/////////////////////////////////////////////////
    private static final float DEFAULT_ZOOM = 15f;

    private void geoLocate(){
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = editText.getText().toString();

        Geocoder geocoder = new Geocoder(getContext());
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage() );
        }
        if(list.size() > 0){
            Address address = list.get(0);
            Log.d(TAG, "geoLocate: found a location: " + address.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();

            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,
                    address.getAddressLine(0));
        }
    }

    private TextView.OnEditorActionListener editorListener = new TextView.OnEditorActionListener() { // 키보드 타입 keyboard 엔터 enter
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            Log.d("keyboard","in the functions");
            switch (actionId) {
                case EditorInfo.IME_ACTION_NEXT:
                    Log.d("keyboard","action_next");
                    //Toast.makeText(this, "Next", Toast.LENGTH_SHORT).show();
                    break;
                case EditorInfo.IME_ACTION_SEND:
                    Log.d("keyboard","action_send");
                    break;
                case EditorInfo.IME_ACTION_SEARCH: // SEARCH 버튼 클릭 =================================== 검색 버튼 클릭 시 이벤트
                    final DatabaseReference ref2= FirebaseDatabase.getInstance().getReference().child("score_list");
                    final Query query2=ref2.orderByChild("name");

                    Log.d("keyboard","action_search");
                    final EditText editText = (EditText) v.findViewById(R.id.addressET);
                    String address = editText.getText().toString();

                    GeocodingLocation locationAddress = new GeocodingLocation();
                    locationAddress.getAddressFromLocation(address,
                            getActivity().getApplicationContext(), new GeocoderHandler());



                    LatLng SEARCHED_PLACE = new LatLng(lat, lng);

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(SEARCHED_PLACE);
                    markerOptions.title(address);

                    BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.marker);
                    Bitmap b=bitmapdraw.getBitmap();
                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

                    mMap.addMarker(markerOptions);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(SEARCHED_PLACE));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                    break;
            }
            return false;
        }
    };

    private void moveCamera(LatLng latLng, float zoom, String title){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if(!title.equals("My Location")){
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mMap.addMarker(options);

            //마커 클릭 리스너
            //this.mMap.setOnMarkerClickListener(markerClickListener);

        }

        hideSoftKeyboard();
    }
    private void hideSoftKeyboard(){
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
    ///////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////Geocoding////////////////////////////////////////////
    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            String lat="", lng="";
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    lat = bundle.getString("lat");
                    lng = bundle.getString("lon");
                    Log.d("location", "lat: "+lat+"  lng:"+lng);
                    break;
                default:
                    locationAddress = null;
            }
            Log.d("location", "locationAddress: "+locationAddress);
            //latLongTV.setText(locationAddress);

            Log.d("location", "latlng"+ lat+" / "+lng);
            if(lat==null || lng==null){
                Log.d("location","null error!");
                return;
            }
            LatLng SEARCHED_PLACE = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
            /*
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(SEARCHED_PLACE);

            BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.marker);
            Bitmap b=bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));*/
            mMap.moveCamera(CameraUpdateFactory.newLatLng(SEARCHED_PLACE));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(SEARCHED_PLACE);
            markerOptions.title(locationAddress);
        }
    }
    //////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        Log.d(TAG, "onMapReady :");

        mMap = googleMap;

        //수원과 부산
        for(int i=0;i<arrayList.size();i++){
            mMap.addMarker(new MarkerOptions().position(arrayList.get(i)).title("Marker"));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(arrayList.get(i)));
        }

        //런타임 퍼미션 요청 대화상자나 GPS 활성 요청 대화상자 보이기전에
        //지도의 초기위치를 서울로 이동
        setDefaultLocation();

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this.getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED   ) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            startLocationUpdates(); // 3. 위치 업데이트 시작


        }else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Snackbar.make(mLayout, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.",
                        Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                        ActivityCompat.requestPermissions( getActivity(), REQUIRED_PERMISSIONS,
                                PERMISSIONS_REQUEST_CODE);
                    }
                }).show();


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions( getActivity(), REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

        // 초기
        LatLng SEOUL = new LatLng(37.56, 126.97);
        LatLng KAIST = new LatLng(36.372238, 127.360422);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(KAIST);
        markerOptions.title("KAIST");
        markerOptions.snippet("korea institude of science and technology");

        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.marker);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);

            List<Location> locationList = locationResult.getLocations();

            if (locationList.size() > 0) {
                location = locationList.get(locationList.size() - 1);
                //location = locationList.get(0);

                currentPosition
                        = new LatLng(location.getLatitude(), location.getLongitude());

                //////////////////////////////////to Firebase//////////////////////////////
                //userLat=location.getLatitude();
                //userLong=location.getLongitude();

                //Log.d("UserLatitude",String.valueOf(userLat));
                //Log.d("UserLongitude",String.valueOf(userLong));
                /////////////////////////////////////////////////////////////////////////////


                String markerTitle = getCurrentAddress(currentPosition);
                String markerSnippet = "위도:" + String.valueOf(location.getLatitude())
                        + " 경도:" + String.valueOf(location.getLongitude());

                Log.d(TAG, "onLocationResult : " + markerSnippet);

                //현재 위치에 마커 생성하고 이동
                setCurrentLocation(location, markerTitle, markerSnippet);

                mCurrentLocatiion = location;
            }


        }

    };

    private void startLocationUpdates() {
        if (!checkLocationServicesStatus()) {
            Log.d(TAG, "startLocationUpdates : call showDialogForLocationServiceSetting");
            showDialogForLocationServiceSetting();
        }else {
            int hasFineLocationPermission = ContextCompat.checkSelfPermission(this.getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION);
            int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this.getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION);
            if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED ||
                    hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED   ) {
                Log.d(TAG, "startLocationUpdates : 퍼미션 안가지고 있음");
                return;
            }
            Log.d(TAG, "startLocationUpdates : call mFusedLocationClient.requestLocationUpdates");
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
            if (checkPermission())
                mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        if (checkPermission()) {
            Log.d(TAG, "onStart : call mFusedLocationClient.requestLocationUpdates");
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
            if (mMap!=null)
                mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mFusedLocationClient != null) {
            Log.d(TAG, "onStop : call stopLocationUpdates");
            mFusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    public String getCurrentAddress(LatLng latlng) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this.getContext(), Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latlng.latitude,
                    latlng.longitude,
                    1);
        } catch (IOException ioException) {
            //네트워크 문제
            //Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            //Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }


        if (addresses == null || addresses.size() == 0) {
            //Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString();
        }

    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(this.getContext().LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {


        if (currentMarker != null) currentMarker.remove();


        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLatLng);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);


        currentMarker = mMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
        mMap.moveCamera(cameraUpdate);

    }

    public void setDefaultLocation() {


        //디폴트 위치, Seoul
        LatLng DEFAULT_LOCATION = new LatLng(36.372292, 127.360371);
        String markerTitle = "위치정보 가져올 수 없음";
        String markerSnippet = "위치 퍼미션과 GPS 활성 요부 확인하세요";


        if (currentMarker != null) currentMarker.remove();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(DEFAULT_LOCATION);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currentMarker = mMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15);
        mMap.moveCamera(cameraUpdate);

    }

    //여기부터는 런타임 퍼미션 처리을 위한 메소드들
    private boolean checkPermission() {

        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this.getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION);



        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED   ) {
            return true;
        }

        return false;

    }

    /*
     * ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if ( check_result ) {

                // 퍼미션을 허용했다면 위치 업데이트를 시작합니다.
                startLocationUpdates();
            }
            else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[1])) {


                    // 사용자가 거부만 선택한 경우에는 앱을 다시 실행하여 허용을 선택하면 앱을 사용할 수 있습니다.
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            getActivity().finish();
                        }
                    }).show();

                }else {


                    // "다시 묻지 않음"을 사용자가 체크하고 거부를 선택한 경우에는 설정(앱 정보)에서 퍼미션을 허용해야 앱을 사용할 수 있습니다.
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            getActivity().finish();
                        }
                    }).show();
                }
            }

        }
    }

    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d(TAG, "onActivityResult : GPS 활성화 되있음");


                        needRequest = true;

                        return;
                    }
                }

                break;
        }
    }

//    @Override
//    public void onPlacesFailure(PlacesException e) {
//
//    }

//    @Override
//    public void onPlacesStart() {
//
//    }

//    @Override
//    public void onPlacesSuccess(final List<Place> places) {
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                for (noman.googleplaces.Place place : places) {
//
//                    LatLng latLng
//                            = new LatLng(place.getLatitude()
//                            , place.getLongitude());
//
//                    String markerSnippet = getCurrentAddress(latLng);
//
//                    MarkerOptions markerOptions = new MarkerOptions();
//                    markerOptions.position(latLng);
//                    markerOptions.title(place.getName());
//                    markerOptions.snippet(markerSnippet);
//                    Marker item = mMap.addMarker(markerOptions);
//                    previous_marker.add(item);
//
//                }
//
//                //중복 마커 제거
//                HashSet<Marker> hashSet = new HashSet<Marker>();
//                hashSet.addAll(previous_marker);
//                previous_marker.clear();
//                previous_marker.addAll(hashSet);
//
//            }
//        });
//    }

//    @Override
//    public void onPlacesFinished() {
//
//    }

//    public void showPlaceInformation(LatLng location)
//    {
//        mMap.clear();//지도 클리어
//
//        if (previous_marker != null)
//            previous_marker.clear();//지역정보 마커 클리어
//
//        new NRPlaces.Builder()
//                .listener(this)
//                .key("AIzaSyCVX6n_K-jNboEmw5wjOm-Q_mlfcp51Re8")
//                .latlng(location.latitude, location.longitude)//현재 위치
//                .radius(500) //500 미터 내에서 검색
//                .type(PlaceType.RESTAURANT) //음식점
//                .build()
//                .execute();
//    }

    /*
    private void geoLocate(){
        Log.d(TAG, "geoLocate: geolocating");
        String searchString = mSearchText.getText().toString();
        EditText editText = (EditText) v.findViewById(R.id.addressET);
        String address = editText.getText().toString();
        Geocoder geocoder = new Geocoder(MapActivity.this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage() );
        }
        if(list.size() > 0){
            Address address = list.get(0);
            Log.d(TAG, "geoLocate: found a location: " + address.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,
                    address.getAddressLine(0));
        }
    }
    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            if(mLocationPermissionsGranted){
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM,
                                    "My Location");
                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }
    private void moveCamera(LatLng latLng, float zoom, String title){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        if(!title.equals("My Location")){
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mMap.addMarker(options);
        }
        hideSoftKeyboard();
    }
*/

}