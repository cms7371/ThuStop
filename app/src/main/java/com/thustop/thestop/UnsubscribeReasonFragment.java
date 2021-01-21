package com.thustop.thestop;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.thustop.databinding.FragmentUnsubscribeReasonBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UnsubscribeReasonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UnsubscribeReasonFragment extends FragmentBase {
    FragmentUnsubscribeReasonBinding binding;
    private int prePosition = -1;


    public UnsubscribeReasonFragment() {
        // Required empty public constructor
    }


    public static UnsubscribeReasonFragment newInstance() {
        UnsubscribeReasonFragment fragment = new UnsubscribeReasonFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    //TODO:탈퇴 사유 서버 백업
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUnsubscribeReasonBinding.inflate(inflater);


        binding.rgUrReason.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == binding.cbUrReason8.getId()) {
                    binding.etUrReasonEtc.setVisibility(View.VISIBLE);
                } else if (binding.etUrReasonEtc.getVisibility() == View.VISIBLE) {
                    binding.etUrReasonEtc.setVisibility(View.GONE);
                }
            }
        });

        binding.tvUrUnsubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _listener.setFragment(DoneFragment.newInstance("탈퇴가 완료되었습니다", "조금 더 발전한 모습으로 찾아뵐게요  :)",true));
            }
        });

        return binding.getRoot();
    }


}