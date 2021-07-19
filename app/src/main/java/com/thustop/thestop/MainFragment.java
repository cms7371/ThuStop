package com.thustop.thestop;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.pixplicity.easyprefs.library.Prefs;
import com.thustop.R;
import com.thustop.thestop.adapter.MainRecyclerAdapter;
import com.thustop.databinding.FragmentMainBinding;
import com.thustop.thestop.model.PageResponse;
import com.thustop.thestop.model.Route;
import com.thustop.thestop.model.Ticket;
import com.thustop.thestop.widgets.NotoTextView;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

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
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    long timeBackPressed = 0;
    private int prePosition = -1;
    private String selectedRegion;
    private NotoTextView preSelectedItem, curSelectedItem;
    private MainRecyclerAdapter mainAdapter = null;
    private List<Route> routes;
    private List<Ticket> tickets;
    private String[] test_region_list;
    private GridView regionGrid;
    private RegionGridAdapter regionAdapter;
    private boolean isRefreshing = false;
    private GpsTracker gpsTracker = null;

    private Retrofit retrofit = null;
    private RestApi restApi = null;


    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //_listener.updateLoginState();
    }

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMainBinding.inflate(inflater);
        binding.setMainfrag(this);
        if (_listener.getGPSServiceStatus() == null) {
            _listener.setGPSServiceStatus(false);
            checkRunTimePermission();
        }
        colorText(binding.tvLocal1, R.string.tv_local1_color, ContextCompat.getColor(requireContext(), R.color.Primary));
        toggle = false;
        //Activity 기본 세팅
        _listener.setToolbarStyle(_listener.GREEN_HAMBURGER, null);

        _listener.setOnBackPressedListener(this);
        _listener.lockDrawer(false);


        test_region_list = new String[]{"호매실동", "하남", "동탄", "우리집", "남의집"};

        //Recycler view 호출 및 어댑터와 연결, 데이터 할당
        RecyclerView mainRecycler = binding.rvRoutes;
        if (mainAdapter == null) {
            mainAdapter = new MainRecyclerAdapter(getContext(), false, null, this);
            mainRecycler.setAdapter(mainAdapter);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                getRoutes();
                getTickets();
            }, 300);
        } else {
            mainRecycler.setAdapter(mainAdapter);
        }

        regionGrid = binding.gvLocal;
        regionAdapter = new RegionGridAdapter(getContext(), test_region_list, _listener.covertDPtoPX(77));

        regionGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (prePosition == position) {
                    preSelectedItem.setSelected(false);
                    preSelectedItem.setTextColor(ContextCompat.getColor(requireContext(), R.color.TextGray));
                    preSelectedItem.setBackgroundResource(R.drawable.bg_round25_grayf5);
                    prePosition = -1;
                    binding.tvSelLocal.setText(R.string.tvSelLocal);
                } else {
                    selectedRegion = adapterView.getItemAtPosition(position).toString();
                    Log.d(TAG, selectedRegion);
                    curSelectedItem = (NotoTextView) view;
                    curSelectedItem.setSelected(true);
                    curSelectedItem.setTextColor(ContextCompat.getColor(requireContext(), R.color.TextBlack));
                    curSelectedItem.setBackgroundResource(R.drawable.bg_outline25_green_f5);
                    binding.tvSelLocal.setText(selectedRegion);
                    if (prePosition != -1) {
                        preSelectedItem = (NotoTextView) regionGrid.getChildAt(prePosition);
                        preSelectedItem.setSelected(false);
                        preSelectedItem.setTextColor(ContextCompat.getColor(requireContext(), R.color.TextGray));
                        preSelectedItem.setBackgroundResource(R.drawable.bg_round25_grayf5);
                    }
                    prePosition = position;
                }
                onSelLocalClick(null);
            }
        });

        regionGrid.setAdapter(regionAdapter);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (gpsTracker != null) {
            gpsTracker.stopUsingGPS();
        }
    }

    /**
     * 노선 추가 버튼 눌렀을 때.
     */
    @Override
    public void onItemSelected(View v, int position, int ticket_position) {
        if (position == 0) {
            //TODO 수정하세요 티켓 위치
            if (ticket_position == -1) {
                _listener.addFragment(AddRouteMapFragment.newInstance(null, null));
            } else {
                Toast.makeText(getContext(), ticket_position + "번째 티켓 눌림", Toast.LENGTH_SHORT).show();
                _listener.addFragment(TicketQRFragment.newInstance(tickets.get(ticket_position)));
            }
        } else if (position >= 2) {
            _listener.addFragment(BoardingApplicationDetailFragment.newInstance(routes.get(position - 2)));
        }
    }


    public void onSelLocalClick(View view) {
        if (!toggle) {
            binding.vPause.setVisibility(View.VISIBLE);
            binding.layoutLocal.setVisibility(View.VISIBLE);
            binding.vPause.animate().alpha(1f).setDuration(300).start();
            binding.layoutLocal.animate().alpha(1f).setDuration(300).start();
            toggle = true;
        } else {
            binding.vPause.animate().alpha(0f).setDuration(300).withEndAction(() ->
                    binding.vPause.setVisibility(View.GONE)).start();
            binding.layoutLocal.animate().alpha(0f).setDuration(300).withEndAction(() ->
                    binding.layoutLocal.setVisibility(View.GONE)).start();
            toggle = false;
        }
    }

    public void onServiceRegionRequestClick(View view) {
        _listener.addFragment(RequestServiceRegionFragment.newInstance());
    }

    public void onRefreshClick(View view) {
        /*Ticket ticket11 = new Ticket(routes.get(1), 1, 1, "2021-03-22");
        ticket11.id = 2222;
        _listener.addFragment(BoardingApplicationDetailFragment.newInstance(ticket11));*/

        //TicketDetailMapDialog ticketDetailMapDialog = new TicketDetailMapDialog(requireContext(), ticket11, getActivity());
        //ticketDetailMapDialog.show();

        //TODO:다이얼로그 확인차 여기다 집어넣음. 나중에 주석 풀기 필수!
        if (!isRefreshing) {
            isRefreshing = true;
            view.animate().rotation(720f).setDuration(750).setInterpolator(new AccelerateDecelerateInterpolator()).withEndAction(() -> {
                        getRoutes();
                        getTickets();
                        view.animate().translationX(0).setDuration(500).withEndAction(() ->
                                view.animate().rotation(1440f).setDuration(750).setInterpolator(new AccelerateDecelerateInterpolator()).withEndAction(() -> {
                                            isRefreshing = false;
                                            view.setRotation(0f);
                                        }
                                ).start()
                        ).start();
                    }
            ).start();
        }
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

    private void initializeRestApi() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(Constant.SERVER_URL).addConverterFactory(GsonConverterFactory.create()).build();
            restApi = retrofit.create(RestApi.class);
        }
    }

    private void getRoutes() {
        initializeRestApi();
        Call<PageResponse<Route>> call = restApi.listRoutes(Prefs.getString(Constant.LOGIN_KEY, ""));
        call.enqueue(new Callback<PageResponse<Route>>() {
            @Override
            public void onResponse(@NotNull Call<PageResponse<Route>> call, @NotNull Response<PageResponse<Route>> response) {
                if (response.isSuccessful() && (response.body() != null)) {
                    routes = response.body().results;
                    if (_listener.getGPSServiceStatus() && gpsTracker == null)
                        gpsTracker = new GpsTracker(getContext());
                    for (Route r : routes) {
                        r.initialize();
                        if (_listener.getGPSServiceStatus())
                            r.updateMinimumDistance(gpsTracker.getLatitude(), gpsTracker.getLongitude());
                    }
                    Log.d(TAG, "onResponse: 노선 업데이트 됨");
                    routes.sort(Comparator.comparing(Route::getMinimumDistance));
                    mainAdapter.updateRoutes(routes);
                }
            }

            @Override
            public void onFailure(@NotNull Call<PageResponse<Route>> call, @NotNull Throwable t) {
                Log.e(TAG, "RestApi onFailure: 노선 정보 수신 실패", null);
            }
        });
    }

    private void getTickets() {
        initializeRestApi();
        Call<PageResponse<Ticket>> call = restApi.getTickets(Prefs.getString(Constant.LOGIN_KEY, ""));
        call.enqueue(new Callback<PageResponse<Ticket>>() {
            @Override
            public void onResponse(@NotNull Call<PageResponse<Ticket>> call, @NotNull Response<PageResponse<Ticket>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().results.size() != 0) {
                    Log.d(TAG, "onResponse: 티켓 로딩 성공" + response.body().results);
                    for (Ticket ticket : response.body().results)
                        ticket.route_obj.initialize();
                    mainAdapter.updateTickets(response.body().results);
                    _listener.putTickets(response.body().results);
                    tickets = response.body().results;
                }
            }

            @Override
            public void onFailure(@NotNull Call<PageResponse<Ticket>> call, @NotNull Throwable t) {

            }
        });
    }

    /*
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
*/


}