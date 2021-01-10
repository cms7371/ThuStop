package com.thustop_00;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pixplicity.easyprefs.library.Prefs;
import com.thustop_00.databinding.FragmentFreeTicketIntroBinding;
import com.thustop_00.databinding.FragmentFreeTicketRouteSelectionBinding;
import com.thustop_00.model.PageResponse;
import com.thustop_00.model.Route;

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
        _listener.setToolbarStyle(_listener.WHITE_BACK, "무료 탑승권");

        MainRecyclerAdapter mainAdapter = new MainRecyclerAdapter(getContext(), true, null, this);
        binding.rvFreeTicketRoutes.setAdapter(mainAdapter);
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

        return binding.getRoot();
    }

    @Override
    public void onItemSelected(View v, int position, int ticket_position) {

    }
}
