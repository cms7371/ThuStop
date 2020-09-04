package com.thustop_00;

import android.Manifest;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.thustop_00.databinding.FragmentLocationMapSearchBinding;
import com.thustop_00.model.Address;

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
    private MapPoint centerPoint;
    private Address startLocation = new Address();
    private Address endLocation = new Address();
    private static final String TAG = "LocationMapSearchFrag";

    private boolean isStart;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    MapReverseGeoCoder.ReverseGeoCodingResultListener resultListener;

    public LocationMapSearchFragment() {
        // Required empty public constructor
    }


    public static LocationMapSearchFragment newInstance(Address startLocation, Address endLocation) {
        LocationMapSearchFragment fragment = new LocationMapSearchFragment();
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
        binding = FragmentLocationMapSearchBinding.inflate(inflater);
        binding.setMapSearchfrag(this);
        _listener.showActionBar(false);
        isStart = true;

        binding.map.setMapViewEventListener(this);

        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (startLocation == null) {
            startLocation = new Address();
            endLocation = new Address();
            getCurrentLocation(startLocation);
            getCurrentLocation(endLocation);
        } else {
            if (startLocation.address != "") {binding.tvStart.setText(startLocation.address);}
            if (endLocation.address != "") {binding.tvEnd.setText(endLocation.address);}
        }


        binding.map.setMapCenterPoint(
                MapPoint.mapPointWithGeoCoord(startLocation.latitude, startLocation.longitude),
                true);

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
                    MapPoint.mapPointWithGeoCoord(startLocation.latitude, startLocation.longitude),
                    true);
            isStart = true;
        } else {
            _listener.addFragmentNotBackStack(LocationSearchFragment.newInstance(startLocation, endLocation));
        }
    }

    public void onEndClick(View view) {
        if (!isStart) {
            _listener.addFragmentNotBackStack(LocationSearchFragment.newInstance(startLocation, endLocation));
        } else {
            isStart = false;
            binding.marker.setImageResource(R.drawable.icon_pin_end);
            binding.tvMarker.setText(R.string.tv_mark_end);
            binding.map.setMapCenterPoint(
                    MapPoint.mapPointWithGeoCoord(endLocation.latitude, endLocation.longitude),
                    true);
        }
    }

    public void getCurrentLocation(Address address) {
        GpsTracker gpsTracker = new GpsTracker(getActivity());
        address.latitude = gpsTracker.getLatitude();
        address.longitude = gpsTracker.getLongitude();
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
                    startLocation.address = addressString;
                    startLocation.latitude = centerPoint.getMapPointGeoCoord().latitude;
                    startLocation.longitude = centerPoint.getMapPointGeoCoord().longitude;
                    Log.d(TAG, "출발장소 변경됨");
                } else {
                    binding.tvEnd.setText(addressString);
                    endLocation.address = addressString;
                    endLocation.latitude = centerPoint.getMapPointGeoCoord().latitude;
                    endLocation.longitude = centerPoint.getMapPointGeoCoord().longitude;
                    Log.d(TAG, "도착 장소 변경됨");
                }
            }
            @Override
            public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder mapReverseGeoCoder) {
                binding.tvStart.setText("실패^^");// 호출에 실패한 경우.
                Log.d(TAG, "여기");
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