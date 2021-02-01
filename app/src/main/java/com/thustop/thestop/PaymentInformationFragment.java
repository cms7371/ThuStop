package com.thustop.thestop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

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
    private int year, month, day;
    private String date;
    private boolean isDateSelected = false;
    private boolean isCouponSelected = false;
    private int couponMethod;
    private int paymentMethod;

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
        _listener.setToolbarStyle(_listener.GREEN_BACK, "탑승 신청");

        couponMethod = -1;
        paymentMethod = -1;

        binding.rgFpmCouponAffiliation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                checkPaymentIsReady();
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
            }
        });

        binding.rgFpmMethod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                checkPaymentIsReady();
                if (checkedId == binding.rbFpmCreditcard.getId())
                    paymentMethod = CREDIT_CARD;
                else if (checkedId == binding.rbFpmKakaopay.getId())
                    paymentMethod = KAKAO_PAY;
                else
                    paymentMethod = VIRTUAL_ACCOUNT;
            }
        });

        return binding.getRoot();
    }

    public void onCalendarClick(View view) {
        // 임시 적용 중
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        end.set(2021, 1, 27);
        CustomDatePickerDialog datePickerDialog = new CustomDatePickerDialog(requireContext(), start, end);
        datePickerDialog.setDialogListener(new CustomDatePickerDialog.CustomDatePickerDialogListener() {
            @Override
            public void onOkClick(int year_picker, int month_picker, int day_picker) {
                year = year_picker;
                month = month_picker + 1;
                day = day_picker;
                date = String.format(Locale.KOREA, "%d-%02d-%02d", year, month, day);
                binding.etvFpmBoardingDate.setTextColor(ContextCompat.getColor(requireContext(), R.color.TextBlack));
                binding.etvFpmBoardingDate.setText(date);
                isDateSelected = true;
                checkPaymentIsReady();
            }
        });
        datePickerDialog.show();
    }

    private void checkPaymentIsReady() {
        if (isDateSelected && (couponMethod != -1) && isCouponSelected && (paymentMethod != -1)) {
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
            m = "danal_tpay";
        else
            m = "kakopay";


    }

}
