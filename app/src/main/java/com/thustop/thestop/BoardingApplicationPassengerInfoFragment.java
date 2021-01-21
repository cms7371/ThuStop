package com.thustop.thestop;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.pixplicity.easyprefs.library.Prefs;
import com.thustop.R;
import com.thustop.databinding.FragmentBoardingApplicationPassengerInfoBinding;
import com.thustop.thestop.model.Route;
import com.thustop.thestop.model.Ticket;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

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
    private int year, month, day;
    private String date;
    private Ticket ticket;


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBoardingApplicationPassengerInfoBinding.inflate(inflater);
        binding.setBoardingApplicationPassengerInfoFrag(this);
        binding.etvFbapiName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 1) {
                    if (binding.etvFbapiBoardingDate.length() > 0) {
                        binding.btFbapiOk.setBackgroundColor(getResources().getColor(R.color.Primary));
                        binding.btFbapiOk.setEnabled(true);
                    }
                } else {
                    binding.btFbapiOk.setBackgroundColor(getResources().getColor(R.color.ButtonGray));
                    binding.btFbapiOk.setEnabled(false);
                }
            }
        });
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
                    if (binding.etvFbapiName.length() > 1) {
                        binding.btFbapiOk.setBackgroundColor(getResources().getColor(R.color.Primary));
                        binding.btFbapiOk.setEnabled(true);
                    }
                } else {
                    binding.btFbapiOk.setBackgroundColor(getResources().getColor(R.color.ButtonGray));
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
        // 임시 적용 중
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        end.set(2021,1,27);
        CustomDatePickerDialog datePickerDialog = new CustomDatePickerDialog(getContext(), start, end);
        datePickerDialog.setDialogListener(new CustomDatePickerDialog.CustomDatePickerDialogListener() {
            @Override
            public void onOkClick(int year_picker, int month_picker, int day_picker) {
                year = year_picker;
                month = month_picker + 1;
                day = day_picker;
                date = String.format("%d-%02d-%02d", year, month, day);
                binding.etvFbapiBoardingDate.setTextColor(getResources().getColor(R.color.TextBlack));
                binding.etvFbapiBoardingDate.setText(date);
            }
        });
        datePickerDialog.show();
    }

    private void postTestTicket(){
        int boarding_via_id = route.boarding_stops.get(boarding_stop_position).id;
        int alighting_via_id = route.alighting_stops.get(alighting_stop_position).id;
        ticket = new Ticket(route.id, boarding_via_id, alighting_via_id, date, date);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RestApi api = retrofit.create(RestApi.class);

        Call<Ticket> call = api.postTicket(Prefs.getString(Constant.LOGIN_KEY, ""), ticket);
        call.enqueue(new Callback<Ticket>() {

            @Override
            public void onResponse(@NotNull Call<Ticket> call, @NotNull Response<Ticket> response) {
                if (response.isSuccessful() && response.body() != null){
                    Toast.makeText(getContext(), "티켓 생성됨", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "티켓 띠용쓰", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<Ticket> call, @NotNull Throwable t) {
                Toast.makeText(getContext(), "아예 에러남", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void onBtOkClick(View view) {
        // TODO 확인 화면 다이얼로그로 바꾸고 결제화면 연결해야함

        BoardingApplicationCheckDialog dialog = new BoardingApplicationCheckDialog(getContext() ,route.getBoardingStopName(boarding_stop_position), route.getAlightingStopName(alighting_stop_position));
        dialog.setListener(new BoardingApplicationCheckDialog.BoardingCheckDialogListener() {
            @Override
            public void onBoardingConfirm() {
                //TODO 결제 프로세스 연결 또는 탑승 신청 완료로 연결
                postTestTicket();
/*                _listener.addFragment(PaymentFragment.newInstance());
                //_listener.setFragment(DoneFragment.newInstance("탑승 신청이 완료되었습니다.", "배차가 확정되면 푸시알림, 또는 문자를 드립니다.",true));
                Handler H = new Handler(Looper.getMainLooper());
                H.postDelayed(dialog::dismiss, 500);*/
            }
        });
        dialog.show();
    }
}