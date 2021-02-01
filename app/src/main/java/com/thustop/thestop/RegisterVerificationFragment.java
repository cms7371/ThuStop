package com.thustop.thestop;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.Certification;
import com.thustop.databinding.FragmentRegisterVerificationBinding;
import com.thustop.thestop.model.Auth;
import com.thustop.thestop.model.CertificationBootPay;

import java.io.IOException;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterVerificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterVerificationFragment extends FragmentBase{


    // Databinding
    FragmentRegisterVerificationBinding binding;
    InputMethodManager imm;
    Auth auth;
    private final static String TAG = "VerificationFragment";

    public RegisterVerificationFragment() {
        // Required empty public constructor
    }

    public static RegisterVerificationFragment newInstance() {
        RegisterVerificationFragment fragment = new RegisterVerificationFragment();
        Bundle args = new Bundle();
        fragment.auth = null;
        fragment.setArguments(args);
        return fragment;
    }

    public static RegisterVerificationFragment newInstance(Auth auth){
        RegisterVerificationFragment fragment = new RegisterVerificationFragment();
        Bundle args = new Bundle();
        fragment.auth = auth;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding= FragmentRegisterVerificationBinding.inflate(inflater);
        binding.setIDVerfrag(this);

        imm = (InputMethodManager)requireActivity().getSystemService(INPUT_METHOD_SERVICE);

        _listener.setToolbarStyle(_listener.WHITE_BACK, "휴대폰 본인인증");
        _listener.setOnKeyboardStateChangedListener(new MainActivity.OnKeyboardStateChangedListener() {
            @Override
            public void onKeyboardShown(int currentKeyboardHeight) {
                ConstraintLayout.LayoutParams newLayoutParams = (ConstraintLayout.LayoutParams) binding.wvCertification.getLayoutParams();
                newLayoutParams.bottomMargin = currentKeyboardHeight;
                binding.wvCertification.setLayoutParams(newLayoutParams);
            }

            @Override
            public void onKeyboardHidden() {
                ConstraintLayout.LayoutParams newLayoutParams = (ConstraintLayout.LayoutParams) binding.wvCertification.getLayoutParams();
                newLayoutParams.bottomMargin = 0;
                binding.wvCertification.setLayoutParams(newLayoutParams);
            }
        });
        WebSettings webSettings = binding.wvCertification.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setDisplayZoomControls(false);
        binding.wvCertification.setWebChromeClient(new CustomChromeClient());
        binding.wvCertification.setWebViewClient(new WebViewClient());
        binding.wvCertification.addJavascriptInterface(new JsHandler(), "Android");
        binding.wvCertification.loadUrl("file:///android_asset/certification.html");


/*        Bootpay.init(getParentFragmentManager())
                .setApplicationId(Constant.BOOTPAY_KEY)
                .setPG(PG.DANAL)
                .setMethod(Method.AUTH)
                .setContext(getContext())
                .setUX(UX.PG_DIALOG)
                .setName("본인인증")
                .setOrderId("123")
                .onDone(new DoneListener() {
                    @Override
                    public void onDone(String data) {
                        Gson gson = new Gson();
                        CertificationBootPay certification = gson.fromJson(data, CertificationBootPay.class);
                        Toast.makeText(getContext(), "사용자 " + certification.getName() + " 본인인증 완료", Toast.LENGTH_LONG).show();
                    }
                }).request();*/



        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onPause() {
        super.onPause();
        _listener.setOnKeyboardStateChangedListener(null);
    }

    public class JsHandler {
        public IamportClient iamportClient;
        private Context context;

        public JsHandler(){
            iamportClient = new IamportClient(Constant.IAMPORT_API_KEY, Constant.IAMPORT_API_SECRET);

        }

        @JavascriptInterface
        public void getData(String impUid) throws IOException, IamportResponseException {
            Certification data =  iamportClient.certificationByImpUid(impUid).getResponse();
            if (auth != null)
                kakaoRegister(data);
            else
                basicRegister(data);
            Toast.makeText(getContext(), data.getName() + data.getPhone(), Toast.LENGTH_LONG).show();
            //TODO 핸드폰 번호 받을 수 있도록 문의해야함 후....


        }

        @JavascriptInterface
        public void onFailure(String msg) {
            Toast.makeText(getContext(), "본인인증에 에러가 발생했습니다. 계속 문제 발생 시 고객센터에 문의 바랍니다.", Toast.LENGTH_LONG).show();
            Log.e(TAG, "onFailure: ", new Throwable(msg));
            _listener.setFragment(MainFragment.newInstance());
        }
    }

    public class CustomChromeClient extends WebChromeClient {
        @Override
        public void onCloseWindow(WebView window) {
            _listener.pressBackButton();
            super.onCloseWindow(window);
        }
    }

    public void kakaoRegister(Certification data){

    }

    public void basicRegister(Certification data){

    }

    public void certificationToAuth(CertificationBootPay cert, Auth auth){
        if (auth.username == null)
            auth.username = cert.getPhone();
        auth.name = cert.getName();
        auth.phone = cert.getPhone();
        auth.gender = cert.getGender();
        auth.birth = cert.getBirth();
    }
}

//삭제 예정 코드
//onCreateView
/*      // use normal adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item,(String[])getResources().getStringArray(R.array.nationality));
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
        binding.etIdNum1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input=binding.etIdNum1.getText().toString();
                if(input.length() == 6) {
                    binding.etIdNum2.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable s){

            }
        });

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
        _listener.setFragment(RegisterPasswordFragment.newInstance());
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
    }*/