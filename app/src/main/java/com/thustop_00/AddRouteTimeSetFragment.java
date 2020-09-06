package com.thustop_00;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thustop_00.databinding.FragmentAddRouteTimeSetBinding;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddRouteTimeSetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddRouteTimeSetFragment extends FragmentBase {
    FragmentAddRouteTimeSetBinding binding;

    int h, min;
    String noon;
    final Calendar cal = Calendar.getInstance();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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

    public static AddRouteTimeSetFragment newInstance() {
        AddRouteTimeSetFragment fragment = new AddRouteTimeSetFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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

        // Inflate the layout for this fragment
        return binding.getRoot();
    }


    public void onTimeSetClick(View view) {
        CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(getActivity());
        timePickerDialog.setDialogListener(new CustomDialogListener() {
            @Override
            public void onOkClick(int hour, int minute, String n) {
                h = hour;
                min = minute;
                noon = n;
                binding.tvTimePicker.setText(noon+"     "+String.valueOf(h)+"시     "+String.valueOf(min)+"분");
            }


        });
        timePickerDialog.show();


        //timePickerDialog.callFunction(binding.tvTimePicker);
        //showTime();
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
        binding.tvTimePicker.setText(n +"     "+String.valueOf(h)+"시     "+String.valueOf(m)+"분");

    }
    /*
    void showTime() {

        CustomTimePicker.OnTimeSetListener onTimeSetListener = new CustomTimePicker.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {

            }
        };

        CustomTimePicker timePickerDialog = new CustomTimePicker(getActivity(), R.style.SpinnerTimePickerDialogTheme, onTimeSetListener,cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false);
        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.setMessage("희망 도착 시간");
        timePickerDialog.show();
    }*/


}