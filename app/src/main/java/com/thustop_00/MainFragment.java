package com.thustop_00;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.thustop_00.databinding.FragmentMainBinding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends FragmentBase implements MainRecyclerAdapter.OnListItemSelectedInterface, MainActivity.onBackPressedListener {
    FragmentMainBinding binding;
    boolean toggle;
    boolean[] tog_local = {false, false, false};

    private GpsTracker gpsTracker;
    double latitude;
    double longitude;
    String address;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    long timeBackPressed = 0;


    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
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

        binding = FragmentMainBinding.inflate(inflater);
        binding.setMainfrag(this);

        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        } else {
            checkRunTimePermission();
        }

        binding.vPause.setVisibility(View.GONE);
        binding.layoutLocal.setVisibility(View.GONE);
        toggle = false;
        _listener.setToolbar(false, false, true);
        _listener.showActionBar(true);
        _listener.setOnBackPressedListener(this);

        RecyclerView mainRecycler = binding.rvRoutes;
        MainRecyclerAdapter mainAdapter = new MainRecyclerAdapter(null, this);
        mainRecycler.setAdapter(mainAdapter);

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    //Listener for RecyclerView item selection.
    @Override
    public void onItemSelected(View v, int position) {
        if (position == 0) {
            _listener.addFragment(AddRouteMapFragment.newInstance(null, null));
        }
    }


    public void onSelLocalClick(View view) {
        if (!toggle) {
            binding.vPause.setVisibility(View.VISIBLE);
            binding.layoutLocal.setVisibility(View.VISIBLE);
            toggle = true;
        } else {
            binding.vPause.setVisibility(View.GONE);
            binding.layoutLocal.setVisibility(View.GONE);
            toggle = false;
        }
    }


    // when select local, change selected button's background resource
    public void onLocal1Click(View view) {
        if (tog_local[1] || tog_local[2]) {
            if (tog_local[1]) {
                tog_local[1] = false;
                binding.btLocal2.setBackgroundResource(R.drawable.button_local);
            } else {
                tog_local[2] = false;
                binding.btLocal3.setBackgroundResource(R.drawable.button_local);
            }
        }

        tog_local[0] = !tog_local[0];
        if (tog_local[0]) {
            binding.btLocal1.setBackgroundResource(R.drawable.button_local_sel);
            binding.tvSelLocal.setText(R.string.bt_local1);
        } else {
            binding.btLocal1.setBackgroundResource(R.drawable.button_local);
            binding.tvSelLocal.setText(R.string.tvSelLocal);
        }
    }

    public void onLocal2Click(View view) {
        if (tog_local[0] || tog_local[2]) {
            if (tog_local[0]) {
                tog_local[0] = false;
                binding.btLocal1.setBackgroundResource(R.drawable.button_local);
            } else {
                tog_local[2] = false;
                binding.btLocal3.setBackgroundResource(R.drawable.button_local);
            }
        }

        tog_local[1] = !tog_local[1];
        if (tog_local[1]) {
            binding.btLocal2.setBackgroundResource(R.drawable.button_local_sel);
            binding.tvSelLocal.setText(R.string.bt_local2);
        } else {
            binding.btLocal2.setBackgroundResource(R.drawable.button_local);
            binding.tvSelLocal.setText(R.string.tvSelLocal);
        }
    }

    public void onLocal3Click(View view) {
        if (tog_local[0] || tog_local[1]) {
            if (tog_local[0]) {
                tog_local[0] = false;
                binding.btLocal1.setBackgroundResource(R.drawable.button_local);
            } else {
                tog_local[1] = false;
                binding.btLocal2.setBackgroundResource(R.drawable.button_local);
            }
        }

        tog_local[2] = !tog_local[2];
        if (tog_local[2]) {
            binding.btLocal3.setBackgroundResource(R.drawable.button_local_sel);
            binding.tvSelLocal.setText(R.string.bt_local3);
        } else {
            binding.btLocal3.setBackgroundResource(R.drawable.button_local);
            binding.tvSelLocal.setText(R.string.tvSelLocal);
        }

    }

    public void onGPSClick(View view) {
        gpsTracker = new GpsTracker(getActivity());
        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();
        address = getCurrentAddress(latitude, longitude);
        binding.tvSelLocal.setText(address);
        //Toast.makeText(getActivity(), "현재위치 \n위도 " + latitude + "\n경도 " + longitude, Toast.LENGTH_LONG).show();
    }

    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grandResults) {
        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if (check_result) {

                //위치 값을 가져올 수 있음
                ;
            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[1])) {

                    Toast.makeText(getActivity(), "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    getActivity().finish();


                } else {

                    Toast.makeText(getActivity(), "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }

    // method to convert location to address
    public String getCurrentAddress(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> addresses;
        try {
            // Last value is maxResults
            addresses = geocoder.getFromLocation(latitude, longitude, 100);
        } catch (IOException ioException) {
            // Network problem
            Toast.makeText(getActivity(), "지오코더 서비스 사용불가", Toast.LENGTH_SHORT).show();
            showDialogForLocationServiceSetting();
            return "지오코더 서비스 사용불가";

        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(getActivity(), "잘못된 GPS 좌표", Toast.LENGTH_SHORT).show();
            showDialogForLocationServiceSetting();
            return "잘못된 GPS 좌표표";
        }
        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(getActivity(), "주소 미발견", Toast.LENGTH_LONG).show();
            showDialogForLocationServiceSetting();
            return "주소 미발견";
        }

        Address address = addresses.get(0);
        return address.getAdminArea() + " " + address.getLocality() + " " + address.getSubLocality();

    }

    void checkRunTimePermission() {

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음


        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(getActivity(), "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }

    // Activate GPS
    private void showDialogForLocationServiceSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n" + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
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
            case GPS_ENABLE_REQUEST_CODE: //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }
                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    @Override
    public void onBack() {
        if(timeBackPressed == 0){
            Toast.makeText(getContext(), "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            timeBackPressed = System.currentTimeMillis();
        } else {
            int ms = (int) (System.currentTimeMillis() - timeBackPressed);
            if (ms > 2000) {
                timeBackPressed = 0;
            } else {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }
    }
}