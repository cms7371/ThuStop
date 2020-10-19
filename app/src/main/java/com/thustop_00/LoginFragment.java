package com.thustop_00;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thustop_00.databinding.FragmentLoginBinding;
import com.thustop_00.model.Token;
import com.thustop_00.model.UserData;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginFragment extends FragmentBase {
    private FragmentLoginBinding binding;
    //@OnClick(R.id.bt_register)
    /*void goLogin() {
        _listener.setFragment(RegisterTermsFragment.newInstance());
    }*/

   /*@OnClick(R.id.bt_login)
   void startlogin() {
       String phone_num = binding.etPhone.getText().toString();
       String password = binding.etPass.getText().toString();
   }*/


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater);
        ButterKnife.bind(this,binding.getRoot());

        _listener.setToolbar(true, false);
        _listener.setTitle(false,"로그인");
        _listener.showActionBar(true);
        return binding.getRoot();
    }

    public void OnFindPassClick(View view){
        _listener.addFragment(FindPasswordFragment.newInstance());
    }

    public void OnRegisterClick(View view){
        _listener.addFragment(RegisterTermsFragment.newInstance());
    }

    private void login(String phone_num, String password) {
        UserData user = new UserData();
        user.password1 = password;
        user.username = phone_num;

        //retrofit 객체 선언
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RestApi api = retrofit.create(RestApi.class);

        Call<Token> call = api.login(user);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {

            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {

            }
        });


    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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