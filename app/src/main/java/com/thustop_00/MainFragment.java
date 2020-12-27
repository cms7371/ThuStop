package com.thustop_00;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.pixplicity.easyprefs.library.Prefs;
import com.thustop_00.databinding.FragmentMainBinding;
import com.thustop_00.model.PageResponse;
import com.thustop_00.model.Route;
import com.thustop_00.widgets.NotoTextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends FragmentBase implements MainRecyclerAdapter.OnListItemSelectedInterface, MainActivity.onBackPressedListener {
    private final static String TAG = "MainFragment";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    FragmentMainBinding binding;
    boolean toggle;
    double latitude;
    double longitude;
    String address;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    long timeBackPressed = 0;
    private int backPosition = -1;
    private String selectedRegion;
    private NotoTextView BackSelectedItem, CurSelectedItem;
    private GpsTracker gpsTracker;
    private MainRecyclerAdapter mainAdapter;
    private List<Route> routes;
    private String[] test_region_list;

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMainBinding.inflate(inflater);
        binding.setMainfrag(this);
        if (_listener.getGPSServiceStatus() == null) {
            checkRunTimePermission();
        }
        binding.vPause.setVisibility(View.GONE);
        binding.layoutLocal.setVisibility(View.GONE);
        colorText(binding.tvLocal1, R.string.tv_local1_color, getResources().getColor(R.color.Primary));
        toggle = false;
        //Activity 기본 세팅
        _listener.setToolbarStyle(_listener.GREEN_HAMBURGER, null);

        _listener.setOnBackPressedListener(this);
        _listener.lockDrawer(false);

        //Test Routes
/*        Route route1 = new Route("A15", "수원", 123, 52.34f, 35, 13, 45, "운행중", 99000);
        Stop stop1_0 = new Stop("수원역 1번 출구", 0, "경기도 수원시 팔달구 매산동 103", 37.266260f, 127.001412f);
        Via via1_0 = new Via(0, stop1_0, "07:30");
        Stop stop1_1 = new Stop("이춘택 병원", 1, "경기도 수원시 팔달구 교동 매산로 138", 37.272110f, 127.015525f);
        Via via1_1 = new Via(1, stop1_1, "07:45");
        Stop stop1_2 = new Stop("성빈센트 병원", 2, "경기도 수원시 팔달구 지동 중부대로 93", 37.277585f, 127.028323f);
        Via via1_2 = new Via(2, stop1_2, "08:00");
        Stop stop1_3 = new Stop("정자역 1번 출구", 3, "성남시 정자동", 37.366200f, 127.108386f);
        Via via1_3 = new Via(3, stop1_3, "08:45");
        Stop stop1_4 = new Stop("정자역 5번 출구", 4, "성남시 정자동", 37.368213f, 127.108262f);
        Via via1_4 = new Via(4, stop1_4, "08:50");
        Stop stop1_5 = new Stop("두산위브파빌리온 오피스텔", 5, "경기도 성남시 분당구 정자동 7", 37.371521f, 127.108274f);
        Via via1_5 = new Via(5, stop1_5, "09:00");
        route1.boarding_stops.add(via1_0);
        route1.boarding_stops.add(via1_1);
        route1.boarding_stops.add(via1_2);
        route1.alighting_stops.add(via1_3);
        route1.alighting_stops.add(via1_4);
        route1.alighting_stops.add(via1_5);
        test_route_list = new ArrayList<Route>();
        test_route_list.add(route1);
        route1.status = "모집중";
        test_route_list.add(route1);*/
        test_region_list = new String[]{"호매실동", "하남", "동탄", "우리집", "남의집"};

        //Recycler view 호출 및 어댑터와 연결, 데이터 할당
        RecyclerView mainRecycler = binding.rvRoutes;
        mainAdapter = new MainRecyclerAdapter(getContext(), null, this);
        mainRecycler.setAdapter(mainAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_URL).addConverterFactory(GsonConverterFactory.create()).build();
        RestApi api = retrofit.create(RestApi.class);
        Call<PageResponse<Route>> call = api.listRoutes(Prefs.getString(Constant.LOGIN_KEY, ""));
        call.enqueue(new Callback<PageResponse<Route>>() {
            @Override
            public void onResponse(Call<PageResponse<Route>> call, Response<PageResponse<Route>> response) {
                if (response.isSuccessful() && (response.body() != null)) {
                    routes = response.body().results;
                    for (Route r : routes) {
                        r.initialize();
                    }
                    mainAdapter.changeDataSet(routes);
                }
            }

            @Override
            public void onFailure(Call<PageResponse<Route>> call, Throwable t) {
                Log.e(TAG, "RestApi onFailure: 노선 정보 수신 실패", null);
            }
        });


        GridView regionGrid = binding.gvLocal;
        RegionGridAdapter RegionAdapter = new RegionGridAdapter(getContext(), test_region_list);

        regionGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (backPosition == position) {
                    BackSelectedItem.setSelected(false);
                    BackSelectedItem.setTextColor(getResources().getColor(R.color.TextGray));
                    BackSelectedItem.setBackgroundResource(R.drawable.button_local);
                    backPosition = -1;
                    binding.tvSelLocal.setText(R.string.tvSelLocal);
                } else {
                    selectedRegion = adapterView.getItemAtPosition(position).toString();
                    Log.d(TAG, selectedRegion);
                    CurSelectedItem = (NotoTextView) view;
                    CurSelectedItem.setSelected(true);
                    CurSelectedItem.setTextColor(getResources().getColor(R.color.TextBlack));
                    CurSelectedItem.setBackgroundResource(R.drawable.button_local_sel);
                    binding.tvSelLocal.setText(selectedRegion);
                    if (backPosition != -1) {
                        BackSelectedItem = (NotoTextView) regionGrid.getChildAt(backPosition);
                        BackSelectedItem.setSelected(false);
                        BackSelectedItem.setTextColor(getResources().getColor(R.color.TextGray));
                        BackSelectedItem.setBackgroundResource(R.drawable.button_local);
                    }
                    backPosition = position;
                }
                toggle = false;
                binding.vPause.setVisibility(View.GONE);
                binding.layoutLocal.setVisibility(View.GONE);
            }
        });
        regionGrid.setAdapter(RegionAdapter);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    /**
     * 노선 추가 버튼 눌렀을 때.
     */
    @Override
    public void onItemSelected(View v, int position, int ticket_position) {
        if (position == 0) {
            if (ticket_position == -1) {
                _listener.addFragment(AddRouteMapFragment.newInstance(null, null));
            } else {
                Toast.makeText(getContext(), ticket_position + "번째 티켓 눌림", Toast.LENGTH_SHORT).show();
            }
        } else if (position >= 2) {
            _listener.addFragment(BoardingApplicationDetailFragment.newInstance(routes.get(position - 2)));
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

    public void onServiceRegionRequestClick(View view) {
        _listener.addFragment(RequestServiceRegionFragment.newInstance());
    }

    public void onGPSClick(View view) {
        gpsTracker = new GpsTracker(getActivity());
        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();
        address = getCurrentAddress(latitude, longitude);
        binding.tvSelLocal.setText(address);
        Toast.makeText(getActivity(), "현재위치 \n위도 " + latitude + "\n경도 " + longitude, Toast.LENGTH_LONG).show();
    }

    // method to convert location to address TODO: 없어져야하는 부분
    public String getCurrentAddress(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> addresses;
        try {
            // Last value is maxResults
            addresses = geocoder.getFromLocation(latitude, longitude, 100);
        } catch (IOException ioException) {
            // Network problem
            Toast.makeText(getActivity(), "지오코더 서비스 사용불가", Toast.LENGTH_SHORT).show();
            _listener.showLocationServiceSettingDialog();
            return "지오코더 서비스 사용불가";

        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(getActivity(), "잘못된 GPS 좌표", Toast.LENGTH_SHORT).show();
            _listener.showLocationServiceSettingDialog();
            return "잘못된 GPS 좌표표";
        }
        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(getActivity(), "주소 미발견", Toast.LENGTH_LONG).show();
            _listener.showLocationServiceSettingDialog();
            return "주소 미발견";
        }
        Address address = addresses.get(0);
        return address.getAdminArea() + " " + address.getLocality() + " " + address.getSubLocality();
    }

    /**
     * 위치, GPS 권환 확인 플로우
     * checkRunTimePermission 에서 GPS 권한확인
     * if GPS 허용 -> 위치 권환 확인으로 -> (2)
     * else GPS 허용 X -> GPS 권한 팝업 뜨고 결과 MainActivity onRequestPermissionsResult 에서 수신 -> (1)
     * (1) GPS 팝업 결과
     * 허용 -> 위치 권환 확인으로 -> (2)
     * 거부 -> isGPSLocationServiceEnabled = false 로 하여 이후 위치 서비스 비활성화
     * (2)위치 권환 확인
     * 허용 -> isGPSLocationServiceEnabled = true 하여 위치 서비스 활성화
     * 거부 -> showLocationServiceSettingDialog 로 설정할 수 있도록 함 -> 결과는 MainActivity onActivityResult 에서 수신 -> (3)
     * (3) 위치 권한 설정 창 이후
     * 허용 -> isGPSLocationServiceEnabled = true 하여 위치 서비스 활성화
     * 거부 -> isGPSLocationServiceEnabled = false 로 하여 이후 위치 서비스 비활성화
     */
    void checkRunTimePermission() {
        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION);
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            // 2. 이미 퍼미션을 가지고 있다면 3.  위치 값을 가져올 수 있음
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)
            Log.d(TAG, "checkRunTimePermission: 위치 권환 허용 되어 있음");
            //위치 서비스 활성화 되어 있는지 체크
            if (!_listener.checkLocationServicesStatus()) {
                _listener.showLocationServiceSettingDialog();
            } else {
                _listener.setGPSServiceStatus(true);
            }
        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.
            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])) {
                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                //Toast.makeText(getActivity(), "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }
            //+ onRequestPermissionResult 에서 허용할 시 위치 설정이 켜져있는지 체크 후 다이얼로그를 띄움
        }
    }


    @Override
    public void onBack() {
        if (timeBackPressed == 0) {
            Toast.makeText(getContext(), "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            timeBackPressed = System.currentTimeMillis();
        } else {
            int ms = (int) (System.currentTimeMillis() - timeBackPressed);
            if (ms > 2000) {
                timeBackPressed = 0;
                onBack();
            } else {
                _listener.finishActivity();
            }
        }
    }

    /***
     * 지역 선택 그리드뷰 어댑터
     *
     * ***/
    //TODO : 별도 레이아웃 쓰면 자꾸 오류남. 뷰 하나라 상관없긴 한데, 레이아웃 사용하면 뷰 셋팅 매번 안해줘도 됨.
    public class RegionGridAdapter extends BaseAdapter {
        LayoutInflater inf;
        private Context context;
        private String[] regions;
        private int layout;

        RegionGridAdapter(Context context, String[] regionsIn) {
            this.context = context;
            this.regions = regionsIn;

        }

        @Override
        public int getCount() {
            return regions.length;
        }

        @Override
        public Object getItem(int position) {
            return regions[position];
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            NotoTextView btRegion = new NotoTextView(this.context);
            btRegion.setText(regions[position]);
            btRegion.setBackgroundResource(R.drawable.button_local);
            btRegion.setGravity(Gravity.CENTER);
            btRegion.setTextColor(getResources().getColor(R.color.TextGray));
            btRegion.setLayoutParams(new GridView.LayoutParams(_listener.covertDPtoPX(77), _listener.covertDPtoPX(77)));
            btRegion.setTextSize(13);
            return btRegion;
        }
    }


//메인 액티비티로 옮겨야 동작하는거 확인 추후 삭제 예정
/*    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grandResults) {
        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults);
        Log.d(TAG,"권한 결과 확인 함수 호출");
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
    }*/

//동작 안하는거 확인 삭제 예정
/*    @Override
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
            default:
                Log.d(TAG, "onActivityResult: 작동하기는 했음");
        }
    }*/

//이거도 동작하는 지 모르겠음
/*    // Activate GPS
    private void showLocationServiceSettingDialog() {
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
    }*/


}