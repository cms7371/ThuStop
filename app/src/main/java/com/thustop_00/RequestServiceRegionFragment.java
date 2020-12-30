package com.thustop_00;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.thustop_00.databinding.FragmentRequestTownServiceBinding;
import com.thustop_00.widgets.NotoEditText;

import java.util.Objects;

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

        _binding.cbMessage.setEnabled(true);
        _binding.etPhoneNum.setEnabled(true);
        _listener.setToolbarStyle(_listener.WHITE_BACK, "우리 동네 서비스 요청");
        // RequestButton enable control
        _binding.cbMessage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    if ((digits == 11)&&(townSelected.length() != 0)) {
                        _binding.btRequest.setBackgroundResource(R.drawable.bg_round25_green);
                        _binding.btRequest.setClickable(true);
                        _binding.btRequest.setClickable(true);
                    } else {
                        _binding.btRequest.setBackgroundResource(R.drawable.bg_round25_graycf);
                        _binding.btRequest.setClickable(false);
                        _binding.btRequest.setClickable(false);
                    }
                } else {
                    if ((digits != 11)&&(townSelected.length() != 0)) {
                        _binding.btRequest.setBackgroundResource(R.drawable.bg_round25_green);
                        _binding.btRequest.setClickable(true);
                        _binding.btRequest.setClickable(true);
                    } else {
                        _binding.btRequest.setBackgroundResource(R.drawable.bg_round25_graycf);
                        _binding.btRequest.setClickable(false);
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
                if ((digits == 11)&&_binding.cbMessage.isChecked()&&(townSelected.length() != 0)) {
                    _binding.btRequest.setBackgroundResource(R.drawable.bg_round25_green);
                    _binding.btRequest.setClickable(true);
                    _binding.btRequest.setClickable(true);
                } else {
                    _binding.btRequest.setBackgroundResource(R.drawable.bg_round25_graycf);
                    _binding.btRequest.setClickable(false);
                }

            }
        });

        _binding.etPhoneNum.setOnClickListener(view -> ((NotoEditText)view).setCursorVisible(true));

        _binding.etPhoneNum.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                textView.setCursorVisible(false);
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(Objects.requireNonNull(getActivity().getCurrentFocus()).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                return true;
            }
        });
        return _binding.getRoot();
    }

    public void onRequestClick(View view) {
        _listener.setFragment(DoneFragment.newInstance(getString(R.string.tv_request_done), getString(R.string.tv_request_continue), true));
    }

    public void onRegionSelectClick(View view) {
        RegionSelectorDialog regionSelectorDialog = new RegionSelectorDialog(getContext());
        regionSelectorDialog.setDialogListener(new RegionSelectorDialog.RegionSelectorListener() {
            @Override
            public void onSelect(String state, String city) {
                townSelected = String.format("%s %s", state, city);
                _binding.tvTownSel.setText(String.format("%s %s", state, city));
                if (((digits == 11)&&_binding.cbMessage.isChecked())||((digits != 11)&&!_binding.cbMessage.isChecked())) {
                    _binding.tvTownSel.setTextColor(getResources().getColor(R.color.TextBlack));
                    _binding.btRequest.setBackgroundResource(R.drawable.bg_round25_green);
                    _binding.btRequest.setClickable(true);
                }
                _binding.tvTownSel.setTextColor(getResources().getColor(R.color.TextBlack));
                _binding.btRequest.setBackgroundResource(R.drawable.bg_round25_green);
                _binding.btRequest.setClickable(true);
                //_binding.cbMessage.setEnabled(true);
                //_binding.etPhoneNum.setEnabled(true);
            }
        });
        regionSelectorDialog.show();
        //regionSelectorDialog.getWindow().setBackgroundDrawable(ColorDrawable.);
    }
}