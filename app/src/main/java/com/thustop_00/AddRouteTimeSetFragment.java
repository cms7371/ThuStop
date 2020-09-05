package com.thustop_00;

import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import com.thustop_00.databinding.FragmentAddRouteTimeSetBinding;

import java.util.Calendar;

import static android.R.style.Theme_Holo_Light_Dialog;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddRouteTimeSetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddRouteTimeSetFragment extends FragmentBase {
    FragmentAddRouteTimeSetBinding binding;

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



        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    public void onTimeSetClick(View view) {
        showTime();
    }

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
    }


}