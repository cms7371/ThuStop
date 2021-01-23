package com.thustop.thestop;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kakao.sdk.auth.LoginClient;
import com.kakao.sdk.user.UserApiClient;
import com.pixplicity.easyprefs.library.Prefs;
import com.thustop.databinding.FragmentLoginBinding;
import com.thustop.thestop.model.Auth;
import com.thustop.thestop.model.Login;
import com.thustop.thestop.model.Token;
import com.thustop.thestop.model.UserDetails;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginFragment extends FragmentBase {
    private FragmentLoginBinding binding;
    private static final String TAG = "LoginFragment";

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater);
        binding.setLoginFrag(this);
        _listener.setToolbarStyle(_listener.WHITE_HAMBURGER, "로그인");
        return binding.getRoot();
    }

    public void OnFindPassClick(View view) {
        _listener.addFragment(FindPasswordFragment.newInstance());
    }

    public void OnRegisterClick(View view) {
        _listener.addFragment(RegisterTermsFragment.newInstance());
    }

    public void onKakaoLoginClick(View view) {
        LoginClient.getInstance().loginWithKakaoTalk(getContext(), (token, loginError) -> {
            if (loginError != null) {
                Log.e(TAG, "로그인 실패", loginError);
            } else {
                Log.d(TAG, "로그인 성공");
                // 사용자 정보 요청
                UserApiClient.getInstance().me((user, meError) -> {
                    if (meError != null) {
                        Log.e(TAG, "사용자 정보 요청 실패", meError);
                    } else {
                        Log.i(TAG, user.toString());
                        kakaoLogin(user.getId());
                    }
                    return null;
                });
            }
            return null;
        });

    }

    private void kakaoLogin(long ID) {
        Login auth = new Login();
        auth.password = auth.username = "kakao" + ID;

        //retrofit 객체 선언
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RestApi api = retrofit.create(RestApi.class);

        Call<Token> call = api.login(auth);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(@NotNull Call<Token> call, @NotNull Response<Token> response) {
                if (response.isSuccessful() && (response.body() != null)) {
                    Prefs.putString(Constant.LOGIN_KEY, "Token " + response.body().key);
                    Log.d(TAG, "onResponse: 서버 로그인 토큰 " + Prefs.getString(Constant.LOGIN_KEY, ""));
                    Utils.registerDevice();
                    getUserDetail(response.body().key);
                    //_listener.setFragment(MainFragment.newInstance());
                } else {
                    Log.e(TAG, "onResponse: Thustop 서버 에러 Null 반환", new NullPointerException());
                    Log.d(TAG, "onResponse: Thustop 서버 에러 " + response.message());
                    testKakaoRegister(ID);
                }
            }

            @Override
            public void onFailure(@NotNull Call<Token> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure: Thustop 서버 에러 접속 실패", new Exception());
            }
        });
    }

    private void testKakaoRegister(long id) {
        Auth auth = new Auth();
        auth.password1 = auth.password2 = auth.username = "kakao" + id;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RestApi api = retrofit.create(RestApi.class);

        Log.d(TAG, "testKakaoRegister: 회원 가입 시도" + auth.username + auth.password);
        Call<Token> call = api.register(auth);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(@NotNull Call<Token> call, @NotNull Response<Token> response) {
                if (response.isSuccessful() && (response.body() != null)) {
                    Prefs.putString(Constant.LOGIN_KEY, "Token " + response.body().key);
                    Utils.registerDevice();
                    Log.d(TAG, "onResponse: 회원가입 성공 " + Prefs.getString(Constant.LOGIN_KEY, ""));
                    _listener.setFragment(MainFragment.newInstance());
                } else {
                    Log.e(TAG, "onResponse: Thustop 서버 에러 Null 반환", new NullPointerException());
                    Log.d(TAG, "onResponse: Thustop 서버 에러 " + response.message());
                }
            }

            @Override
            public void onFailure(@NotNull Call<Token> call, @NotNull Throwable t) {
                Log.d(TAG, "server error");
            }
        });

    }

    //TODO 테스트 유저 정보 불러오기 수정해야함
    private void getUserDetail(String token) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RestApi api = retrofit.create(RestApi.class);

        Call<UserDetails> call = api.getUserDetails("Token " + token);
        call.enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(@NotNull Call<UserDetails> call, @NotNull Response<UserDetails> response) {
                if (response.isSuccessful() && (response.body() != null)) {
                    Toast.makeText(getContext(), response.body().first_name + "로그인 성공", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "유저 정보 불러오기 실패" + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<UserDetails> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure: 유저 정보 불러오기 실패함", t);
            }
        });

    }


    /* private FragmentLoginBinding binding;
    private Route _route;


    @OnClick(R.id.bt_login) void login(long id) {
        UserData user = new UserData();
        user.password = user.username = "kakao"+id;
        RestApi api = new RestRequestHelper().getRetrofit();

        Call<Token> call = api.login(user);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if(response.isSuccessful() && (response.body() != null)) {
                    Prefs.putString(Constant.LOGIN_KEY, "Token " + response.body().key);
                    _listener.addFragmentNotBackStack(MainFragment.newInstance());
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Log.d("test1", "server error");
            }
        });

    }

    public LoginFragment() {

    }

    public static LoginFragment newInstance(Route route) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constant.ROUTE, Parcels.wrap(route));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            _route = Parcels.unwrap(getArguments().getParcelable(Constant.ROUTE));
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater);
        View v = binding.getRoot();
        ButterKnife.bind(this, v);
        UserManagement.getInstance()
                .requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        Log.i("KAKAO_SESSION", "로그아웃 완료");
                    }
                });

        Session.getCurrentSession().addCallback(sessionCallback);
        binding.kakaoButton.setSupportFragment(this);
        return v;
    }

    @Override
    public void onDestroyView() {
        //Session.getCurrentSession().removeCallback(sessionCallback);

        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            Log.d("test1", "okok");
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    @OnClick(R.id.bt_register)
    void register() {_listener.addFragment(RegisterFragment.newInstance());    }

*/

   /*
   FragmentLoginBinding binding;

   public static LoginFragment newInstance() {
       LoginFragment fragment = new LoginFragment();
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
       binding = FragmentLoginBinding.inflate(inflater);
        // Inflate the layout for this fragment
        ButterKnife.bind(this,binding.getRoot());
        return binding.getRoot();
    }*/


}