package com.thustop_00;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.fragment.app.Fragment;

import com.thustop_00.databinding.FragmentRequestTownServiceBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RequestServiceRegionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestServiceRegionFragment extends FragmentBase {
    FragmentRequestTownServiceBinding _binding;
    private String townSelected ="";
    private int digits = 0;
    public RequestServiceRegionFragment() {
        // Required empty public constructor
    }

    public static RequestServiceRegionFragment newInstance() {
        RequestServiceRegionFragment fragment = new RequestServiceRegionFragment();
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
        _binding = FragmentRequestTownServiceBinding.inflate(inflater);
        _binding.setRequestTownFrag(this);

        _listener.setToolbarStyle(_listener.WHITE_BACK, "우리 동네 서비스 요청");
        // RequestButton enable control
        _binding.cbMessage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    if (digits == 11) {
                        _binding.btRequest.setBackgroundResource(R.drawable.bg_round25_green);
                        _binding.btRequest.setClickable(true);
                    } else {
                        _binding.btRequest.setBackgroundResource(R.drawable.bg_round25_graycf);
                        _binding.btRequest.setClickable(false);
                    }
                } else {
                    if (digits == 0) {
                        _binding.btRequest.setBackgroundResource(R.drawable.bg_round25_green);
                        _binding.btRequest.setClickable(true);
                    } else {
                        _binding.btRequest.setBackgroundResource(R.drawable.bg_round25_graycf);
                        _binding.btRequest.setClickable(false);
                    }
                }
            }
        });
        // RequestButton enable control
        _binding.etPhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            // 변경 완료후 문자 반영
            @Override
            public void afterTextChanged(Editable editable) {
                // 글자수 업데이트
                digits = _binding.etPhoneNum.getText().toString().length();
                if ((digits == 11)&&_binding.cbMessage.isChecked()) {
                    _binding.btRequest.setBackgroundResource(R.drawable.bg_round25_green);
                    _binding.btRequest.setClickable(true);
                } else {
                    _binding.btRequest.setBackgroundResource(R.drawable.bg_round25_graycf);
                    _binding.btRequest.setClickable(false);
                }
            }
        });
        return _binding.getRoot();
    }

    public void onRequestClick(View view) {
        _listener.setFragment(DoneFragment.newInstance(getString(R.string.tv_request_done), getString(R.string.tv_request_continue), true));
    }

    //TODO 질문: 제출하기 버튼 활성화 관련해서, 지역선택 안하면 그냥 폰번도 못적게 했는데 별로.? 아니면 경우의수가 너무 많아짐.
    public void onRegionSelectClick(View view) {
        RegionSelectorDialog regionSelectorDialog = new RegionSelectorDialog(getContext());
        regionSelectorDialog.setDialogListener(new RegionSelectorDialog.RegionSelectorListener() {
            @Override
            public void onSelect(String state, String city) {
                _binding.tvTownSel.setText(String.format("%s %s", state, city));
                _binding.tvTownSel.setTextColor(getResources().getColor(R.color.TextBlack));
                _binding.btRequest.setBackgroundResource(R.drawable.bg_round25_green);
                _binding.btRequest.setClickable(true);
                _binding.cbMessage.setEnabled(true);
                _binding.etPhoneNum.setEnabled(true);
            }
        });
        regionSelectorDialog.show();
        //regionSelectorDialog.getWindow().setBackgroundDrawable(ColorDrawable.);
    }
}