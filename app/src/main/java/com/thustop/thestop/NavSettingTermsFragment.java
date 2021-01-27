package com.thustop.thestop;

import android.os.Bundle;

import androidx.annotation.IntDef;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thustop.R;
import com.thustop.databinding.FragmentNavSettingTermsBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NavSettingTermsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavSettingTermsFragment extends FragmentBase {
    FragmentNavSettingTermsBinding binding;
    int position;

    private static final String ARG_PARAM1 = "param1";
    public static final int SERVICE = 0;
    public static final int PERSONAL_INFO = 1;
    public static final int MARKETING = 2;

    @IntDef({SERVICE, PERSONAL_INFO, MARKETING})
    public @interface contentsType{
    }




    public static NavSettingTermsFragment newInstance(int position) {
        NavSettingTermsFragment fragment = new NavSettingTermsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNavSettingTermsBinding.inflate(inflater);
        binding.setNavSettingTermsFrag(this);
        textSetting(position);

        return binding.getRoot();
    }

    public void textSetting(@contentsType int contentType) {
        switch (position) {
            case 0:
                binding.tvTermsContainer.setText(R.string.terms_of_service_content);
                _listener.setToolbarStyle(_listener.WHITE_BACK, "서비스 이용약관");
                break;
            case 1:
                binding.tvTermsContainer.setText(R.string.terms_of_personal_info_content);
                _listener.setToolbarStyle(_listener.WHITE_BACK, "개인정보 보호정책");
                break;
            case 2:
                binding.tvTermsContainer.setText(R.string.personal_data_policies_content);
                _listener.setToolbarStyle(_listener.WHITE_BACK, getString(R.string.tv_personal_data_policies));
                break;
            case 3:
                binding.tvTermsContainer.setText(R.string.personal_data_policies_content);
                _listener.setToolbarStyle(_listener.WHITE_BACK, getString(R.string.tv_operational_policies));
                break;
            case 4:
                binding.tvTermsContainer.setText(getString(R.string.business_information_content));
                _listener.setToolbarStyle(_listener.WHITE_BACK, "사업자 정보");
        }
    }
}