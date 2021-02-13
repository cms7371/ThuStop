package com.thustop.thestop;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.pixplicity.easyprefs.library.Prefs;
import com.thustop.R;
import com.thustop.databinding.FragmentBoardingApplicationPassengerInfoBinding;
import com.thustop.thestop.model.Route;
import com.thustop.thestop.model.Ticket;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BoardingApplicationPassengerInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BoardingApplicationPassengerInfoFragment extends FragmentBase {
    private static final String TAG = "BoardingInfo";
    private FragmentBoardingApplicationPassengerInfoBinding binding;
    private Route route;
    private int boarding_stop_position;
    private int alighting_stop_position;
    private String boarding_start, boarding_end;
    private Ticket ticket;

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);


    public static BoardingApplicationPassengerInfoFragment newInstance(Route route, int boarding_stop_position, int alighting_stop_position) {
        BoardingApplicationPassengerInfoFragment fragment = new BoardingApplicationPassengerInfoFragment();
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
        binding = FragmentBoardingApplicationPassengerInfoBinding.inflate(inflater);
        binding.setBoardingApplicationPassengerInfoFrag(this);

        binding.etvFbapiBoardingDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    binding.btFbapiOk.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.Primary));
                    binding.btFbapiOk.setEnabled(true);
                } else {
                    binding.btFbapiOk.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.ButtonGray));
                    binding.btFbapiOk.setEnabled(false);
                }
            }
        });
        binding.tvFbapiBoardingStop.setText(route.getBoardingStopName(boarding_stop_position));
        binding.tvFbapiAlightingStop.setText(route.getAlightingStopName(alighting_stop_position));
        _listener.setToolbarStyle(_listener.GREEN_BACK, "탑승 신청");
        return binding.getRoot();
    }

    //TODO: 데이트피커 변경중. 나중에 재적용 필요

    public void onCalendarClick(View view) {
        // 캘린더 객체 생성(현재 날짜)
        Calendar application_start = Calendar.getInstance();
        Calendar application_end = Calendar.getInstance();
        //Status 처리(운행중이 아닐 경우 시작일로 셋팅 필요)
        if (route.status.equals("모집중") || route.status.equals("운행대기")) {
            // TODO : route 시작일로 변경 필요.

            try {
                Date start_date = dateFormat.parse("2021-02-13");
                application_start.setTime(start_date);
                application_start.add(Calendar.DATE, -1);
                application_end.setTime(start_date);
                application_end.add(Calendar.DATE, -1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        // 최대 탑승 신청기간은 가능일(다음날)로 부터 1주일
        application_end.add(Calendar.DATE, 7);
        CustomDatePickerDialog datePickerDialog = new CustomDatePickerDialog(requireContext(), application_start, application_end);
        datePickerDialog.setDialogListener(new CustomDatePickerDialog.CustomDatePickerDialogListener() {
            @Override
            public void onOkClick(int year_picker, int month_picker, int day_picker) {
                boarding_start = String.format(Locale.KOREA, "%d-%02d-%02d", year_picker, month_picker, day_picker);
                setBoardingEndDate(boarding_start);
                Log.d("끝나는 날", boarding_end);
                binding.etvFbapiBoardingDate.setTextColor(ContextCompat.getColor(requireContext(), R.color.TextBlack));
                binding.etvFbapiBoardingDate.setText(boarding_start);
            }
        });
        datePickerDialog.show();
    }


    public void onBtOkClick(View view) {
        // TODO 확인 화면 다이얼로그로 바꾸고 결제화면 연결해야함
        if (route.status.equals("모집중"))
            postTestTicket();
        else
            //TODO 날짜도 넘겨줘야함
            _listener.addFragment(PaymentInformationFragment.newInstance(route, boarding_stop_position, alighting_stop_position, boarding_start));
    }

    private void postTestTicket() {
        int boarding_via_id = route.boarding_stops.get(boarding_stop_position).id;
        int alighting_via_id = route.alighting_stops.get(alighting_stop_position).id;
        ticket = new Ticket(route.id, boarding_via_id, alighting_via_id, boarding_start, boarding_end);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RestApi api = retrofit.create(RestApi.class);

        Call<Ticket> call = api.postTicket(Prefs.getString(Constant.LOGIN_KEY, ""), ticket);
        call.enqueue(new Callback<Ticket>() {
            @Override
            public void onResponse(@NotNull Call<Ticket> call, @NotNull Response<Ticket> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getContext(), "티켓 생성됨", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "티켓 생성실패", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: 티켓 생성 실패" + response.message());
                }
            }

            @Override
            public void onFailure(@NotNull Call<Ticket> call, @NotNull Throwable t) {
                Toast.makeText(getContext(), "아예 에러남", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setBoardingEndDate(String boarding_start) {
        try {
            Date end_date = dateFormat.parse(boarding_start);
            Calendar cal = Calendar.getInstance();
            cal.setTime(end_date);
            cal.add(Calendar.MONTH, 1);
            boarding_end = dateFormat.format(cal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}