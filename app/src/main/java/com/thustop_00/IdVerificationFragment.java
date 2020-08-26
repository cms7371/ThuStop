package com.thustop_00;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.thustop_00.databinding.FragmentIdVerificationBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IdVerificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IdVerificationFragment extends FragmentBase {
    private Spinner spinner;
    FragmentIdVerificationBinding binding;

    public IdVerificationFragment() {
        // Required empty public constructor
    }


    public static IdVerificationFragment newInstance() {
        IdVerificationFragment fragment = new IdVerificationFragment();
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

        binding=FragmentIdVerificationBinding.inflate(inflater);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,(String[])getResources().getStringArray(R.array.nationality));
        binding.spAgency.setAdapter(adapter);

        binding.spAgency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                binding.tvAgency.setText(adapterView.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                binding.tvAgency.setText("통신사");
            }
        });

        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}