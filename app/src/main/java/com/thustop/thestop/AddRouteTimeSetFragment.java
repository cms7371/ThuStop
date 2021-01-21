package com.thustop.thestop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.thustop.databinding.FragmentAddRouteTimeSetBinding;
import com.thustop.thestop.model.Address;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddRouteTimeSetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddRouteTimeSetFragment extends FragmentBase implements MainActivity.onBackPressedListener {
    FragmentAddRouteTimeSetBinding binding;

    int h, min;
    String noon;
    final Calendar cal = Calendar.getInstance();
    Address startLocation, endLocation;

    public AddRouteTimeSetFragment() {
        // Required empty public constructor
    }
    /*
    public static AddRouteTimeSetFragment newInstance(String param1, String param2) {
        AddRouteTimeSetFragment fragment = new AddRouteTimeSetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    public static AddRouteTimeSetFragment newInstance(Address startLocation, Address endLocation) {
        AddRouteTimeSetFragment fragment = new AddRouteTimeSetFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.startLocation = startLocation;
        fragment.endLocation = endLocation;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddRouteTimeSetBinding.inflate(inflater);
        binding.setAddRouteTimefrag(this);
        //binding.tvTimePicker.setText((+String.valueOf(h)+"시 "+String.valueOf(min)+"분");)
        setCurTime();
        binding.tvStart.setText(startLocation.getAddress());
        binding.tvEnd.setText(endLocation.getAddress());

        _listener.setToolbarStyle(_listener.WHITE_BACK, "");
        _listener.setOnBackPressedListener(this);

        // Inflate the layout for this fragment
        return binding.getRoot();
    }


    public void onTimeSetClick(View view) {
        CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(getActivity());
        timePickerDialog.setDialogListener(new CustomTimePickerDialog.CustomTimePickerDialogListener() {
            //TODO : 00 찍히는 거는 해결. 피커 특성상 언어 설정으로 인해 AM PM이 한글로 오전 오후로 나오는데 통일이 낫지 않을까?
            @Override
            public void onOkClick(int hour, int minute, String n) {
                h = hour;
                min = minute;
                noon = n;
                binding.tvTimePicker.setText(noon+"              "+String.valueOf(h)+"              "+String.valueOf(min));
            }


        });
        timePickerDialog.show();

    }

    private void setCurTime(){
        String n;
        if(cal.get(Calendar.AM_PM) == 0) {
            n = "AM";
        } else {
            n = "PM";
        }
        int h = cal.get(Calendar.HOUR);
        int m = (cal.get(Calendar.MINUTE)/10)*10;
        binding.tvTimePicker.setText(n+"              "+String.valueOf(h)+"              "+String.valueOf(m));

    }

    @Override
    public void onBack() {
        _listener.addFragmentNotBackStack(AddRouteMapFragment.newInstance(startLocation, endLocation));
    }


}