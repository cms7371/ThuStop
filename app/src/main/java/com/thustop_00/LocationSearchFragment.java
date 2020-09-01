package com.thustop_00;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.thustop_00.databinding.FragmentLocationMapSearchBinding;
import com.thustop_00.databinding.FragmentLocationSearchBinding;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LocationSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationSearchFragment extends FragmentBase {
    private FragmentLocationSearchBinding binding;
    private String start;

    public LocationSearchFragment() {
        // Required empty public constructor
    }


    public static LocationSearchFragment newInstance(String start) {
        LocationSearchFragment fragment = new LocationSearchFragment();
        Bundle args = new Bundle();
        fragment.start=start;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLocationSearchBinding.inflate(inflater);
        View v = binding.getRoot();
        _listener.setToolbar(true,true,false);
        _listener.setTitle("");
        _listener.showActionBar(true);
        //TODO 구글 주소 자동 완성 추가
        binding.tvStart.setText(start);
        return binding.getRoot();
    }

    /*
    private void search() {
        String s = binding.etEnd.getText().toString();
        //입력이 되었나
        if(!s.isEmpty()){
            if(getActivity() != null){
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                if(inputMethodManager != null)
                    inputMethodManager.hideSoftInputFromWindow(binding.etEnd.getWindowToken(), 0);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://dapi.kakao.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                RestApi api = retrofit.create(RestApi.class);


            }

        }

    }*/
}