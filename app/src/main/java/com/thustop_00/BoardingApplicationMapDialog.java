package com.thustop_00;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.thustop_00.model.Address;
import com.thustop_00.model.Via;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

public class BoardingApplicationMapDialog extends Dialog implements MapView.POIItemEventListener {
    private static final String TAG = "BoardingMapDialog";
    private MapView mapView;
    private Activity activity;
    private StopMapListener dialogListener;
    protected OnFragmentInteractionListener _listener;

    private ArrayList<Via> boarding_stops;
    private ArrayList<Via> alighting_stops;

    private Via via;
    private boolean isDestination;


    private boolean op_mod;
    private final static boolean SINGLE = false;
    private final static boolean MULTIPLE = true;

    public interface StopMapListener {
        void onConfirm();
    }

    public BoardingApplicationMapDialog(@NonNull Context context, Activity activity, ArrayList<Via> boarding_stops, ArrayList<Via> alighting_stops) {
        super(context, R.style.CustomFullDialog);//화면을 꽉 채울 수 있도록 스타일 지정
        this.activity = activity;
        this.boarding_stops = boarding_stops;
        this.alighting_stops = alighting_stops;
        this.op_mod = MULTIPLE;
    }

    public BoardingApplicationMapDialog(Context context, Activity activity, Via via, boolean isDestination) {
        super(context, R.style.CustomFullDialog);
        this.activity = activity;
        this.via = via;
        this.isDestination = isDestination;
        this.op_mod = SINGLE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (activity instanceof OnFragmentInteractionListener) {
            _listener = (OnFragmentInteractionListener) activity;
            _listener.setOnBackPressedListener(null);
        } else {
            throw new RuntimeException(activity.toString() + " must implement OnFragmentInteractionListener");
        }

        setContentView(R.layout.dialog_stop_map);
        //처음 설정된 match_parent가 무시되기 때문에 다시 설정해줘야함.
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        mapView = new MapView(activity);
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.mv_dsm_stop_map);
        mapViewContainer.addView(mapView);
        findViewById(R.id.bt_dsm_back).setOnClickListener(view -> dismiss());
        findViewById(R.id.bt_dsm_gps).setOnClickListener(view -> onGPSClick());

        //동작 모드에 따른 정의
        MapPOIItem marker;
        if (op_mod == MULTIPLE) {
            mapView.setPOIItemEventListener(this);
            findViewById(R.id.bt_dsm_confirm).setVisibility(View.GONE);
            setInformationBox(boarding_stops.get(0), false);
            //출발 정류장 마커 만들어 줍니다.(1, 2, 3 등 양수 태그 가짐)
            for (int i = 1; i <= boarding_stops.size(); i++) {
                marker = new MapPOIItem();
                marker.setTag(i);
                marker.setItemName(boarding_stops.get(i - 1).stop.name);
                marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                marker.setCustomImageResourceId(R.drawable.icon_pin_green);
                marker.setCustomImageAutoscale(true);
                marker.setCustomImageAnchor(0.5f, 1.0f);
                marker.setMapPoint(getMapPointWithVia(boarding_stops.get(i - 1)));
                mapView.addPOIItem(marker);
            }
            //도착 정류장 마커 만들기(-1, -2, -3 음수 태그 가짐)
            for (int i = 1; i <= alighting_stops.size(); i++) {
                marker = new MapPOIItem();
                marker.setTag(-i);
                marker.setItemName(alighting_stops.get(i - 1).stop.name);
                marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                marker.setCustomImageResourceId(R.drawable.icon_pin_red);
                marker.setCustomImageAutoscale(true);
                marker.setCustomImageAnchor(0.5f, 1.0f);
                marker.setMapPoint(getMapPointWithVia(alighting_stops.get(i - 1)));
                mapView.addPOIItem(marker);
            }
            mapView.fitMapViewAreaToShowAllPOIItems();
            mapView.zoomOut(true);
            //mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(centerLatitude,centerLongitude), 7, true);
        } else if (op_mod == SINGLE) { //정류장이 하나인 경우(노선 신청에서 넘어오는 경우)
            findViewById(R.id.bt_dsm_confirm).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogListener.onConfirm();
                    dismiss();
                }
            });
            setInformationBox(via, isDestination);
            marker = new MapPOIItem();
            marker.setTag(0);
            marker.setItemName(via.stop.name);
            marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
            if (isDestination) {
                marker.setCustomImageResourceId(R.drawable.icon_pin_red);
            } else {
                marker.setCustomImageResourceId(R.drawable.icon_pin_green);
            }
            marker.setCustomImageAutoscale(true);
            marker.setCustomImageAnchor(0.5f, 1.0f);
            marker.setMapPoint(getMapPointWithVia(via));
            mapView.addPOIItem(marker);
            mapView.fitMapViewAreaToShowAllPOIItems();
            mapView.zoomOut(true);
            mapView.zoomOut(true);
            //mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(centerLatitude,centerLongitude), 5, true);
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        _listener = null;
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
        mapView.setShowCurrentLocationMarker(false);
    }

    public void setDialogListener(StopMapListener listener) {
        this.dialogListener = listener;
    }

    private MapPoint getMapPointWithVia(Via v) {
        return MapPoint.mapPointWithGeoCoord(v.stop.latitude, v.stop.longitude);
    }

    private void setInformationBox(Via v, boolean isDestination) {
        ((TextView) findViewById(R.id.tv_dsm_address)).setText(v.stop.address);
        ((TextView) findViewById(R.id.tv_dsm_stop_name)).setText(v.stop.name);
        if (isDestination) {
            ((ImageView) findViewById(R.id.iv_dsm_address)).setImageDrawable(getContext().getDrawable(R.drawable.icon_destination));
        } else {
            ((ImageView) findViewById(R.id.iv_dsm_address)).setImageDrawable(getContext().getDrawable(R.drawable.icon_departure));
        }
    }

    private void onGPSClick() {
        if (_listener.getGPSServiceStatus()) {
            if (mapView.getCurrentLocationTrackingMode() == MapView.CurrentLocationTrackingMode.TrackingModeOff) {
                mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);
                mapView.setCustomCurrentLocationMarkerTrackingImage(R.drawable.icon_gps_marker, new MapPOIItem.ImageOffset(_listener.covertDPtoPX(18), _listener.covertDPtoPX(18)));
                Toast.makeText(getContext(), "위치를 탐색 중입니다(10초~1분 소요)\n한 번 더 누르면 현재 위치로 핀이 이동합니다.", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onGPSClick: 현위지 마커 활성화 됨");
            } else {
                GpsTracker gpsTracker = new GpsTracker(activity);
                mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(gpsTracker.getLatitude(), gpsTracker.getLongitude()), true);
            }
        } else {
            Toast.makeText(getContext(), "GPS 또는 위치 서비스가 비활성화 되어있습니다.\n설정에서 권한을 활성화해주세요", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
        int index = mapPOIItem.getTag();
        findViewById(R.id.cl_dsm_info).animate().translationY(-100).setDuration(150).withEndAction(() -> findViewById(R.id.cl_dsm_info).animate().translationY(0).setDuration(150).start()).start();
        if (index > 0) {
            index = index - 1;
            setInformationBox(boarding_stops.get(index), false);
        } else {
            index = (-index) - 1;
            setInformationBox(alighting_stops.get(index), true);
        }
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }
}
