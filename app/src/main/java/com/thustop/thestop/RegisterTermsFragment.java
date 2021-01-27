package com.thustop.thestop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.fragment.app.Fragment;

import com.thustop.R;
import com.thustop.databinding.FragmentRegisterTermsBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterTermsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterTermsFragment extends FragmentBase implements CompoundButton.OnCheckedChangeListener {
    FragmentRegisterTermsBinding binding;


    public RegisterTermsFragment() {
        // Required empty public constructor
    }
    //TODO : 약관 보이는 뷰 클릭 연결하기!

    public static RegisterTermsFragment newInstance() {
        RegisterTermsFragment fragment = new RegisterTermsFragment();
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
        binding = FragmentRegisterTermsBinding.inflate(inflater);
        binding.setAgreeTermsfrag(this);

        _listener.setToolbarStyle(_listener.WHITE_BACK, "");

        binding.cbAgreeAll.setOnCheckedChangeListener(this);
        binding.cbTermsOfService.setOnCheckedChangeListener(this);
        binding.cbTermsOfPersonalInfo.setOnCheckedChangeListener(this);
        binding.cbTermsOfMarketing.setOnCheckedChangeListener(this);
        binding.cbAgree4.setOnCheckedChangeListener(this);
        binding.cbAgree5.setOnCheckedChangeListener(this);
        binding.cbAgree6.setOnCheckedChangeListener(this);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    public void onAgreeClick(View view) {_listener.addFragment(RegisterVerificationFragment.newInstance());}



    public void onTermsOfServiceClick(View view) {
        _listener.addFragment(NavSettingTermsFragment.newInstance(NavSettingTermsFragment.SERVICE));
    }

    public void onTermsOfPersonalInfoClick(View view) {
        _listener.addFragment(NavSettingTermsFragment.newInstance(NavSettingTermsFragment.PERSONAL_INFO));
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()){
            case R.id.cb_agree_all :
                if(binding.cbAgreeAll.isChecked()) {
                    binding.cbTermsOfService.setChecked(true);
                    binding.cbTermsOfPersonalInfo.setChecked(true);
                    binding.cbTermsOfMarketing.setChecked(true);
                    binding.cbAgree4.setChecked(true);
                    binding.cbAgree5.setChecked(true);
                    binding.cbAgree6.setChecked(true);
                    Clickable(true);
                } else {
                    if(binding.cbAgreeAll.isPressed()){
                        binding.cbTermsOfService.setChecked(false);
                        binding.cbTermsOfPersonalInfo.setChecked(false);
                        binding.cbTermsOfMarketing.setChecked(false);
                        binding.cbAgree4.setChecked(false);
                        binding.cbAgree5.setChecked(false);
                        binding.cbAgree6.setChecked(false);
                        Clickable(false);
                    }
                }
                break;
            default:
                    binding.cbAgreeAll.setChecked(binding.cbTermsOfService.isChecked()
                            && binding.cbTermsOfPersonalInfo.isChecked()
                            && binding.cbTermsOfMarketing.isChecked()
                            && binding.cbAgree4.isChecked()
                            && binding.cbAgree5.isChecked()
                            && binding.cbAgree6.isChecked());
                    Clickable(binding.cbTermsOfService.isChecked()
                            && binding.cbTermsOfPersonalInfo.isChecked()
                            && binding.cbTermsOfMarketing.isChecked()
                            && binding.cbAgree4.isChecked()
                            && binding.cbAgree5.isChecked());
        }
    }

    public void Clickable(boolean b) {
        if(b) {
            binding.btAgree.setBackgroundResource(R.drawable.bg_round25_green);
            binding.btAgree.setEnabled(true);
        } else {
            binding.btAgree.setBackgroundResource(R.drawable.bg_round25_graycf);
            binding.btAgree.setEnabled(false);
        }

    }
}