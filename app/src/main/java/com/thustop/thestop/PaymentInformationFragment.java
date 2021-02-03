package com.thustop.thestop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.thustop.R;
import com.thustop.databinding.FragmentPaymentInfomationBinding;
import com.thustop.thestop.model.Route;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Locale;

public class PaymentInformationFragment extends FragmentBase {
    private FragmentPaymentInfomationBinding binding;
    private Route route;
    private int boarding_stop_position;
    private int alighting_stop_position;
    private boolean isCouponSelected = false;
    private int couponMethod = -1;
    private int paymentMethod = -1;

    private static final int COUPON = 1;
    private static final int AFFILIATION = 2;
    private static final int NONE = 3;
    private static final int CREDIT_CARD = 4;
    private static final int KAKAO_PAY = 5;
    private static final int VIRTUAL_ACCOUNT = 6;

    public static PaymentInformationFragment newInstance() {
        PaymentInformationFragment fragment = new PaymentInformationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static PaymentInformationFragment newInstance(Route route, int boarding_stop_position, int alighting_stop_position) {
        PaymentInformationFragment fragment = new PaymentInformationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.route = route;
        fragment.boarding_stop_position = boarding_stop_position;
        fragment.alighting_stop_position = alighting_stop_position;
        return fragment;
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPaymentInfomationBinding.inflate(inflater);
        binding.setPaymentInfoFlag(this);
        binding.tvFpmDeparture.setText(route.getBoardingStopName(boarding_stop_position));
        binding.tvFpmDestination.setText(route.getAlightingStopName(alighting_stop_position));
        _listener.setToolbarStyle(_listener.GREEN_BACK, "결제하기");
        binding.rgFpmCouponAffiliation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == binding.rbFpmCoupon.getId()){
                    //TODO 쿠폰 목록 리사이클러로 넣어야함
                    couponMethod = COUPON;
                    isCouponSelected = false;
                } else if (checkedId == binding.rbFpmAffiliation.getId()){
                    //TODO 제휴 목록 리사이클러로 넣어야함
                    couponMethod = AFFILIATION;
                    isCouponSelected = false;
                } else {
                    couponMethod = NONE;
                    isCouponSelected = true;
                }
                checkPaymentIsReady();
            }
        });
        binding.rgFpmMethod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == binding.rbFpmCreditcard.getId())
                    paymentMethod = CREDIT_CARD;
                else if (checkedId == binding.rbFpmKakaopay.getId())
                    paymentMethod = KAKAO_PAY;
                else
                    paymentMethod = VIRTUAL_ACCOUNT;
                checkPaymentIsReady();
            }
        });
        binding.cbFpmAgreement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkPaymentIsReady();
            }
        });
        if (couponMethod == -1) {
            binding.rbFpmNothing.setChecked(true);
        }
        checkPaymentIsReady();

        return binding.getRoot();
    }

    private void checkPaymentIsReady() {
        if ((couponMethod != -1) && isCouponSelected && (paymentMethod != -1) && binding.cbFpmAgreement.isChecked()) {
            binding.btFpmPay.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Primary));
            binding.btFpmPay.setEnabled(true);
        } else {
            binding.btFpmPay.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.ButtonGray));
            binding.btFpmPay.setEnabled(false);
        }
    }

    public void onMakePaymentClick(View view) {
        String m;
        if (paymentMethod == CREDIT_CARD)
            _listener.addFragment(PaymentFragment.newInstance("danal_tpay", "card",route));
        else if (paymentMethod == KAKAO_PAY)
            _listener.addFragment(PaymentFragment.newInstance("kakaopay", "card",route));
        else if (paymentMethod == VIRTUAL_ACCOUNT)
            Toast.makeText(requireContext(), "테스트 모드에서는 가상계좌 결제를 지원하지 않습니다",Toast.LENGTH_SHORT).show();
            //_listener.addFragment(PaymentFragment.newInstance("danal_tpay", "vbank",route));
    }

}
