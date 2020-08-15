package com.thustop_00;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.thustop_00.databinding.FragmentMainBinding;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends FragmentBase {
    FragmentMainBinding binding;
    boolean toggle;
    boolean[] tog_local = {false, false, false} ;
    

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
        ButterKnife.bind(this,binding.getRoot());



        binding.vPause.setVisibility(View.GONE);
        binding.layoutLocal.setVisibility(View.GONE);
        toggle = false;

        binding.gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "YY", Toast.LENGTH_SHORT).show();
            }
        });

        binding.tvSelLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!toggle) {
                    binding.vPause.setVisibility(View.VISIBLE);

                    binding.layoutLocal.setVisibility(View.VISIBLE);

                    toggle = true;
                } else {
                    binding.vPause.setVisibility(View.GONE);
                    binding.layoutLocal.setVisibility(View.GONE);
                    toggle = false;
                }
            }
        });

        _listener.setToolbar(false, false, true);
        _listener.showActionBar(true);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }
    public void onLocal1Click(View view) {
        if(tog_local[1]||tog_local[2]) {
            Toast.makeText(getActivity(), "지역을 하나만 선택해주세요", Toast.LENGTH_SHORT).show();
        } else {
            tog_local[0] = !tog_local[0];
            if(tog_local[0]) {
                binding.btLocal1.setBackgroundResource(R.drawable.button_round_light_gray_sel);
                binding.tvSelLocal.setText(R.string.bt_local1);
            } else {
                binding.btLocal1.setBackgroundResource(R.drawable.button_round_light_gray);
            }
        }

    }
    public void onLocal2Click(View view) {
        if(tog_local[0] || tog_local[2]) {
            Toast.makeText(getActivity(), "지역을 하나만 선택해주세요", Toast.LENGTH_SHORT).show();
        } else{
            tog_local[1] = !tog_local[1];
            if(tog_local[1]) {
                binding.btLocal2.setBackgroundResource(R.drawable.button_round_light_gray_sel);
                binding.tvSelLocal.setText(R.string.bt_local2);
            } else {
                binding.btLocal2.setBackgroundResource(R.drawable.button_round_light_gray);
            }
        }

    }
    public void onLocal3Click(View view) {
        if(tog_local[0] || tog_local[1]) {
            Toast.makeText(getActivity(), "지역을 하나만 선택해주세요", Toast.LENGTH_SHORT).show();
        } else {
            tog_local[2] = !tog_local[2];
            if(tog_local[2]) {
                binding.btLocal3.setBackgroundResource(R.drawable.button_round_light_gray_sel);
                binding.tvSelLocal.setText(R.string.bt_local3);
            } else {
                binding.btLocal3.setBackgroundResource(R.drawable.button_round_light_gray);
            }
        }

    }

}