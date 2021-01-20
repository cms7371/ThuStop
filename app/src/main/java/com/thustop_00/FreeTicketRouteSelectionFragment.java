package com.thustop_00;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.pixplicity.easyprefs.library.Prefs;
import com.thustop_00.databinding.FragmentFreeTicketIntroBinding;
import com.thustop_00.databinding.FragmentFreeTicketRouteSelectionBinding;
import com.thustop_00.model.PageResponse;
import com.thustop_00.model.Route;
import com.thustop_00.widgets.NotoTextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FreeTicketRouteSelectionFragment extends FragmentBase implements MainRecyclerAdapter.OnListItemSelectedInterface{
    private FragmentFreeTicketRouteSelectionBinding binding;
    private List<Route> routes;
    private final static String TAG = "FreeTicketRoutes";
    private String[] test_region_list;
    private GridView regionGrid;
    private RegionGridAdapter regionGridAdapter;
    private int prePosition = -1;
    private String selectedRegion;
    private NotoTextView preSelectedItem, curSelectedItem;
    boolean toggle = false;

    public static FreeTicketRouteSelectionFragment newInstance() {
        FreeTicketRouteSelectionFragment fragment = new FreeTicketRouteSelectionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFreeTicketRouteSelectionBinding.inflate(inflater);
        binding.setFreeTicketRouteSelectionFrag(this);
        _listener.setToolbarStyle(_listener.WHITE_BACK, "무료 탑승권");

        MainRecyclerAdapter mainAdapter = new MainRecyclerAdapter(getContext(), true, null, this);
        binding.rvFreeTicketRoutes.setAdapter(mainAdapter);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
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
        }, 300);

        test_region_list = new String[]{"호매실동", "하남", "동탄", "우리집", "남의집"};
        regionGridAdapter = new RegionGridAdapter(getContext(),test_region_list,_listener.covertDPtoPX(77));
        regionGrid = binding.gvFftLocal;
        regionGrid.setAdapter(regionGridAdapter);
        regionGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (prePosition == position) {
                    preSelectedItem.setSelected(false);
                    preSelectedItem.setTextColor(getResources().getColor(R.color.TextGray));
                    preSelectedItem.setBackgroundResource(R.drawable.bg_round25_grayf5);
                    prePosition = -1;
                    binding.tvFftSelectedRegion.setText(R.string.tvSelLocal);
                } else {
                    selectedRegion = adapterView.getItemAtPosition(position).toString();
                    Log.d(TAG, selectedRegion);
                    curSelectedItem = (NotoTextView) view;
                    curSelectedItem.setSelected(true);
                    curSelectedItem.setTextColor(getResources().getColor(R.color.TextBlack));
                    curSelectedItem.setBackgroundResource(R.drawable.bg_outline25_green_f5);
                    binding.tvFftSelectedRegion.setText(selectedRegion);
                    if (prePosition != -1) {
                        preSelectedItem = (NotoTextView) regionGrid.getChildAt(prePosition);
                        preSelectedItem.setSelected(false);
                        preSelectedItem.setTextColor(getResources().getColor(R.color.TextGray));
                        preSelectedItem.setBackgroundResource(R.drawable.bg_round25_grayf5);
                    }
                    prePosition = position;
                }
                onSelLocalClick(null);
            }
        });


        return binding.getRoot();
    }

    public void onSelLocalClick(View view) {
        if (!toggle) {
            binding.vFftPause.setVisibility(View.VISIBLE);
            binding.clFftLocal.setVisibility(View.VISIBLE);
            binding.vFftPause.animate().alpha(1f).setDuration(300).start();
            binding.clFftLocal.animate().alpha(1f).setDuration(300).start();
            toggle = true;
        } else {
            binding.vFftPause.animate().alpha(0f).setDuration(300).withEndAction(() ->
                    binding.vFftPause.setVisibility(View.GONE)).start();
            binding.clFftLocal.animate().alpha(0f).setDuration(300).withEndAction(() ->
                    binding.clFftLocal.setVisibility(View.GONE)).start();
            toggle = false;
        }
    }

    @Override
    public void onItemSelected(View v, int position, int ticket_position) {

    }


}
