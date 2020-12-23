package com.thustop_00;

import android.Manifest;
import android.content.Context;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.thustop_00.databinding.FragmentAddRouteMapBinding;
import com.thustop_00.model.Address;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddRouteMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddRouteMapFragment extends FragmentBase implements MapView.MapViewEventListener {
    private FragmentAddRouteMapBinding binding;
    private MapPoint centerPoint;
    private Address startLocation = new Address();
    private Address endLocation = new Address();
    private static final String TAG = "LocationMapSearchFrag";

    private boolean isStart;
    private GpsTracker gpsTracker = null;
    private MapPOIItem gpsMarker = null;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    MapReverseGeoCoder.ReverseGeoCodingResultListener resultListener;

    public AddRouteMapFragment() {
        // Required empty public constructor
    }


    public static AddRouteMapFragment newInstance(Address startLocation, Address endLocation) {
        AddRouteMapFragment fragment = new AddRouteMapFragment();
        Bundle args = new Bundle();
        fragment.startLocation = startLocation;
        fragment.endLocation = endLocation;
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
        binding = FragmentAddRouteMapBinding.inflate(inflater);
        binding.setMapSearchfrag(this);
        _listener.setToolbarStyle(_listener.INVISIBLE, "");
        isStart = true;
        binding.map.setMapViewEventListener(this);
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        //입력이 null로 fragment가 선언되면(메인에서 넘어올 때) 현재 위치로 초기화 해줍니다.
        if (startLocation == null) {
            startLocation = new Address();
            endLocation = new Address();
            getCurrentLocation(startLocation);
            getCurrentLocation(endLocation);
        }
        if (!startLocation.getAddress().equals("")) {
            binding.tvStart.setText(startLocation.getAddress());
            binding.tvStart.setTextColor(Color.parseColor("#535353"));
        }
        if (!endLocation.getAddress().equals("")) {
            binding.tvEnd.setText(endLocation.getAddress());
            binding.tvEnd.setTextColor(Color.parseColor("#535353"));
            binding.map.setMapCenterPoint(
                    MapPoint.mapPointWithGeoCoord(endLocation.getLatitude(), endLocation.getLongitude()), true);
            onEndClick(binding.tvEnd);
        } else {
            binding.map.setMapCenterPoint(
                    MapPoint.mapPointWithGeoCoord(startLocation.getLatitude(), startLocation.getLongitude()), true);
        }

        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("출발지");
        marker.setTag(1);

        return binding.getRoot();
    }

    public void onStartClick(View view) {
        if (!isStart) {
            binding.marker.setImageResource(R.drawable.icon_pin_start);
            binding.tvMarker.setText(R.string.tv_mark_start);
            binding.map.setMapCenterPoint(
                    MapPoint.mapPointWithGeoCoord(startLocation.getLatitude(), startLocation.getLongitude()),
                    true);
            isStart = true;
        } else {
            _listener.addFragmentNotBackStack(AddRouteSearchFragment.newInstance(startLocation, endLocation));
        }
    }

    public void onEndClick(View view) {
        if (!isStart) {
            _listener.addFragmentNotBackStack(AddRouteSearchFragment.newInstance(startLocation, endLocation));
        } else {
            isStart = false;
            binding.marker.setImageResource(R.drawable.icon_pin_end);
            binding.tvMarker.setText(R.string.tv_mark_end);
            binding.map.setMapCenterPoint(
                    MapPoint.mapPointWithGeoCoord(endLocation.getLatitude(), endLocation.getLongitude()),
                    true);
        }
    }

    public void onConfirmClick(View view) {
        if (startLocation.getAddress().equals("")) {
            Toast.makeText(getContext(), "지도를 끌어 출발지를 선택해주세요.", Toast.LENGTH_SHORT).show();
        } else if (endLocation.getAddress().equals("")) {
            Toast.makeText(getContext(), "지도를 끌어 도착지를 선택해주세요.", Toast.LENGTH_SHORT).show();
        } else {
            _listener.addFragmentNotBackStack(AddRouteTimeSetFragment.newInstance(startLocation, endLocation));
        }
    }

    public void onGPSClick(View view){
        if(_listener.getGPSServiceStatus()){
            if(gpsTracker == null){
                gpsTracker = new GpsTracker(getContext());
                gpsMarker = new MapPOIItem();
                gpsMarker.setTag(0);
                gpsMarker.setItemName("현재 위치");
                gpsMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
            }

        }
    }

    public void onBackClick(View view) {
        _listener.setFragment(MainFragment.newInstance());
    }

    public void getCurrentLocation(Address address) {
        GpsTracker gpsTracker = new GpsTracker(getActivity());
        address.setLatitude(gpsTracker.getLatitude());
        address.setLongitude(gpsTracker.getLongitude());
        //address = getCurrentAddress(latitude, longitude);
        //Toast.makeText(getActivity(), "현재위치 \n위도 " + latitude + "\n경도 " + longitude, Toast.LENGTH_LONG).show();
    }

    public void getAddr() {
        MapReverseGeoCoder reverseGeoCoder = new MapReverseGeoCoder(Constant.APP_KEY, centerPoint, new MapReverseGeoCoder.ReverseGeoCodingResultListener() {
            @Override
            public void onReverseGeoCoderFoundAddress(MapReverseGeoCoder mapReverseGeoCoder, String addressString) {
                // 주소를 찾은 경우.

                if (isStart) {
                    binding.tvStart.setText(addressString);
                    binding.tvStart.setTextColor(Color.parseColor("#535353"));
                    startLocation.setAddress(addressString);
                    startLocation.setLatitude(centerPoint.getMapPointGeoCoord().latitude);
                    startLocation.setLongitude(centerPoint.getMapPointGeoCoord().longitude);
                    Log.d(TAG, "출발장소 변경됨");
                } else {
                    binding.tvEnd.setText(addressString);
                    binding.tvEnd.setTextColor(Color.parseColor("#535353"));
                    endLocation.setAddress(addressString);
                    endLocation.setLatitude(centerPoint.getMapPointGeoCoord().latitude);
                    endLocation.setLongitude(centerPoint.getMapPointGeoCoord().longitude);
                    Log.d(TAG, "도착 장소 변경됨");
                }
            }

            @Override
            public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder mapReverseGeoCoder) {
                //binding.tvStart.setText("실패^^");// 호출에 실패한 경우.
                Log.d(TAG, "onReverseGeoCoderFailedToFindAddress: ReverseGeoCoder 호출 실패");
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
        centerPoint = binding.map.getMapCenterPoint();
        getAddr();
    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }
}