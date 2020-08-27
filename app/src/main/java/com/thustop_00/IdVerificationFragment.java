package com.thustop_00;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.thustop_00.databinding.FragmentIdVerificationBinding;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IdVerificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IdVerificationFragment extends FragmentBase {
    // Databinding
    FragmentIdVerificationBinding binding;

    InputMethodManager imm;

    // Set timer for identity verification
    final int TIME = 180*1000; //3minutes
    final int COUNT_INTERVAL = 1000; //count interval is 1sec
    // set textview showing timer
    CountDownTimer countDownTimer;


    public IdVerificationFragment() {
        // Required empty public constructor
    }


    public static IdVerificationFragment newInstance() {
        IdVerificationFragment fragment = new IdVerificationFragment();
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

        binding=FragmentIdVerificationBinding.inflate(inflater);
        binding.setIDVerfrag(this);

        imm = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
        // use normal adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,(String[])getResources().getStringArray(R.array.nationality));
        // use custom adapter for set hint for spinner
        MySpinnerAdapter adapter1 = new MySpinnerAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item,(String[])getResources().getStringArray(R.array.agency));
        binding.spAgency.setAdapter(adapter1);
        binding.spNation.setAdapter(adapter);
        binding.spAgency.setSelection(adapter1.getCount()); // set last item of list as hint


        // Set textview for showing selected item of spinner
        binding.spNation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                binding.tvNation.setText(adapterView.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spAgency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                binding.tvAgency.setText(adapterView.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                binding.tvAgency.setText("통신사");
            }
        });

        // link ID number edit text views(생년월일 입력후 바로 7번째 자리 focus 이동)
        binding.etIdnum1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input=binding.etIdnum1.getText().toString();
                if(input.length() == 6) {
                    binding.etIdnum2.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable s){

            }
        });

        _listener.setToolbar(true, true, false);
        _listener.setTitle("휴대폰 본인인증");
        _listener.showActionBar(true);

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    // 인증번호 보내기 버튼 클릭
    public void onSendClick(View view) {
        // 전화번호 유효확인, 인증번호 시스템 연결
        //정상적일때 가정하고 타이머 가동
        CountDownTimer();
    }

    // 키보드 바깥 화면 클릭시 키보드 닫히기
    public void onBackgroundClick(View view) {
        imm.hideSoftInputFromWindow(binding.etName.getWindowToken(),0);

    }

    //완료버튼 누르면 이동
    //인증번호 유효시 작동하게 코드 수정 필요
    public void onDoneClick(View view) {
        _listener.setFragment(RegisterFragment.newInstance());
    }

    // custom spinner adapter
    public class MySpinnerAdapter extends ArrayAdapter<String> {

        public MySpinnerAdapter(Context context, int resource, String[] objects) {
            super(context, resource, objects);
        }

        public View getView(int position, View convertView, ViewGroup parent){
            View v = super.getView(position, convertView, parent);
            if(position == getCount()){
                ((TextView)v.findViewById(android.R.id.text1)).setText("");
                ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount()));
            }

            return v;
        }

        public int getCount() {
            return super.getCount()-1;
        }
    }

    // timer
    public void CountDownTimer() {
        countDownTimer = new CountDownTimer(TIME, COUNT_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                long codeTimeCount = millisUntilFinished/1000;

                if((codeTimeCount-((codeTimeCount/60)*60))>=10) {
                    binding.tvTimer.setText("0"+(codeTimeCount/60)+":"+(codeTimeCount-((codeTimeCount/60)*60)));
                } else {
                    binding.tvTimer.setText("0"+(codeTimeCount/60)+":0"+(codeTimeCount-((codeTimeCount/60)*60)));
                }
            }

            @Override
            public void onFinish() {
                Toast.makeText(getActivity(),"인증시간이 만료되었습니다. 다시 인증해주세요",Toast.LENGTH_SHORT).show();

            }
        }.start();
    }
}
