package com.thustop_00;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.pixplicity.easyprefs.library.Prefs;
import com.thustop_00.databinding.FragmentRegisterBinding;
import com.thustop_00.model.Token;
import com.thustop_00.model.UserData;

import org.jetbrains.annotations.NotNull;

import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends FragmentBase {
    private FragmentRegisterBinding binding;
    private Context context;
    @OnClick(R.id.bt_confirm)
    void reg() {
        UserData user = new UserData();
        user.username = binding.etName.getText().toString();
        user.email = binding.etPass.getText().toString();
        user.password1=binding.etIdnum.getText().toString();
        user.password2=binding.etCheckPass.getText().toString();
        Log.d("id", user.username);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                RegisterRequest(user);
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
        Log.d("test", "끝");
    }


    public void RegisterRequest(UserData user) {

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.addInterceptor(loggingInterceptor);

        UserData NewUser = new UserData();
        NewUser.email = user.email;
        NewUser.username = user.username;
        NewUser.password1 = NewUser.password2=user.password1;
        Log.d("test1 regi", "등록중1");

        //RestApi restApi = new RestRequestHelper().getRetrofit();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(clientBuilder.build())
                .build();
        RestApi restApi = retrofit.create(RestApi.class);
        Call<Token> response = restApi.register(NewUser);
        Log.d("test1 reging", "call성공");
        response.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(@NotNull Call<Token> call, @NotNull Response<Token> response) {
                if (response.isSuccessful()) {
                    Log.d("ㅅㅅㅅㅅㅅ","ㄱㄱㄱ");
                    Prefs.putString(Constant.LOGIN_KEY, "Token " + response.body().key);
                    //Utils.registerDevice();
                    Toast.makeText(context,"success?", Toast.LENGTH_SHORT).show();

                }else{

                    Log.d("연결",  Integer.toString(response.code()));
                }
            }

            @Override
            public void onFailure(@NotNull Call<Token> call, @NotNull Throwable t) {
                Log.d("test1", "server error");
            }
        });
    }



    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
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
        context = container.getContext();
        binding = FragmentRegisterBinding.inflate(inflater);
        ButterKnife.bind(this,binding.getRoot());
        //_listener.showActionBar(true);
        _listener.setToolbar(true, false, false);
        _listener.setTitle("회원가입");


        return binding.getRoot();

    }
}