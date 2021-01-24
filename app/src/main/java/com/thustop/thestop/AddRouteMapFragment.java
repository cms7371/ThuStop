package com.thustop.thestop;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.thustop.R;
import com.thustop.databinding.FragmentAddRouteMapBinding;
import com.thustop.thestop.model.Address;

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
    private boolean isDragged = false;
    private GpsTracker gpsTracker = null;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddRouteMapBinding.inflate(inflater);
        binding.setMapSearchfrag(this);
        _listener.setToolbarStyle(_listener.INVISIBLE, "");
        isStart = true;
        binding.map.setMapViewEventListener(this);
        //입력이 null로 fragment가 선언되면(메인에서 넘어올 때) 현재 위치로 초기화 해줍니다.
        if (startLocation == null) {
            if (_listener.getGPSServiceStatus()) {
                startLocation = new Address();
                endLocation = new Address();
                getCurrentLocation(startLocation);
                getCurrentLocation(endLocation);
            } else {
                startLocation = Address.newInstance(null, 37.56667, 126.97847);
                endLocation = Address.newInstance(null, 37.56667, 126.97847);
            }

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
        if (gpsTracker == null)
            gpsTracker = new GpsTracker(getContext());

        return binding.getRoot();
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.map.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
        binding.map.setShowCurrentLocationMarker(false);
        gpsTracker.stopUsingGPS();
    }

    public void onStartClick(View view) {
        if (!isStart) {
            binding.marker.setImageResource(R.drawable.icon_pin_head_green);
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
            binding.marker.setImageResource(R.drawable.icon_pin_head_red);
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

    //TODO:gps 벡터 잘되나 확인!(안되면 마커는 png로 변경)
    public void onGPSClick(View view) {
        if (_listener.getGPSServiceStatus()) {
            if (binding.map.getCurrentLocationTrackingMode() == MapView.CurrentLocationTrackingMode.TrackingModeOff) {
                binding.map.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);
                binding.map.setCustomCurrentLocationMarkerTrackingImage(R.drawable.ic_gps_marker, new MapPOIItem.ImageOffset(_listener.covertDPtoPX(18), _listener.covertDPtoPX(18)));
                Toast.makeText(getContext(), "위치를 탐색 중입니다(10초~1분 소요)\n한 번 더 누르면 현재 위치로 핀이 이동합니다.", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onGPSClick: 현위지 마커 활성화 됨");
            } else {
                Address address = new Address();
                getCurrentLocation(address);
                binding.map.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(address.getLatitude(), address.getLongitude()), true);
            }
        } else {
            Toast.makeText(getContext(), "GPS 또는 위치 서비스가 비활성화 되어있습니다.\n설정에서 권한을 활성화해주세요", Toast.LENGTH_SHORT).show();
        }
    }

    public void onBackClick(View view) {
        _listener.setFragment(MainFragment.newInstance());
    }

    public void getCurrentLocation(Address address) {
        address.setLatitude(gpsTracker.getLatitude());
        address.setLongitude(gpsTracker.getLongitude());
    }

    public void getAddr() {
        MapReverseGeoCoder reverseGeoCoder = new MapReverseGeoCoder(Constant.KAKAO_API_KEY, centerPoint, new MapReverseGeoCoder.ReverseGeoCodingResultListener() {
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

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
        centerPoint = binding.map.getMapCenterPoint();
        getAddr();
    }
}