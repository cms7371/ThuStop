package com.thustop_00;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thustop_00.databinding.FragmentLocationMapSearchBinding;
import com.thustop_00.model.Addr;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LocationMapSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationMapSearchFragment extends FragmentBase implements MapView.MapViewEventListener {
    private FragmentLocationMapSearchBinding binding;
    private MapView mapView;
    private MapPoint sel_point;
    private Addr addr = new Addr();
    private GpsTracker gpsTracker;
    private MapReverseGeoCoder mapReverseGeoCoder;
    double latitude;    //위도
    double longitude;   //경도
    String address;     //주소
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    MapReverseGeoCoder.ReverseGeoCodingResultListener resultListener;

    public LocationMapSearchFragment() {
        // Required empty public constructor
    }


    public static LocationMapSearchFragment newInstance() {
        LocationMapSearchFragment fragment = new LocationMapSearchFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLocationMapSearchBinding.inflate(inflater);
        View v = binding.getRoot();
        _listener.showActionBar(false);


        binding.map.setMapViewEventListener(this);

        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        currentPosition();
        binding.map.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude,longitude),true);

        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("출발지");
        marker.setTag(1);


        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(latitude,longitude));
        marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
        marker.setCustomImageResourceId(R.drawable.icon_place_start);
        marker.setCustomImageAutoscale(false);
        marker.setCustomImageAnchor(0.5f,1.0f);
        binding.map.addPOIItem(marker);

        sel_point = binding.map.getMapCenterPoint();


        addr.x = sel_point.getMapPointGeoCoord().latitude;
        addr.y = sel_point.getMapPointGeoCoord().longitude;



        return binding.getRoot();
    }

    public void currentPosition() {
        gpsTracker = new GpsTracker(getActivity());
        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();
        //address = getCurrentAddress(latitude, longitude);
        //Toast.makeText(getActivity(), "현재위치 \n위도 " + latitude + "\n경도 " + longitude, Toast.LENGTH_LONG).show();
    }

    public void getAddr() {
        MapReverseGeoCoder reverseGeoCoder = new MapReverseGeoCoder(Constant.APP_KEY, sel_point, new MapReverseGeoCoder.ReverseGeoCodingResultListener() {
            @Override
            public void onReverseGeoCoderFoundAddress(MapReverseGeoCoder mapReverseGeoCoder, String addressString) {
                // 주소를 찾은 경우.
                addr.address = addressString;
                binding.tvStart.setText(addressString);
            }

            @Override
            public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder mapReverseGeoCoder) {
                binding.tvStart.setText("실패^^");// 호출에 실패한 경우.
                Log.d("여기", "여기");
            }
        }, getActivity());

        reverseGeoCoder.startFindingAddress();
    }


    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
        sel_point = binding.map.getMapCenterPoint();
        getAddr();
    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }
}