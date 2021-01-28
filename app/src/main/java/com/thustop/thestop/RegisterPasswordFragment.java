package com.thustop.thestop;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.pixplicity.easyprefs.library.Prefs;
import com.thustop.databinding.FragmentRegisterPassowordBinding;
import com.thustop.thestop.model.Auth;
import com.thustop.thestop.model.Token;
import com.thustop.thestop.model.User;

import org.jetbrains.annotations.NotNull;

import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterPasswordFragment extends FragmentBase {
    private FragmentRegisterPassowordBinding binding;
    private Context context;


    public void RegisterRequest(User user) {

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.addInterceptor(loggingInterceptor);

        Auth NewUser = new Auth();
        NewUser.username = user.username;
        NewUser.password1 = NewUser.password = "00";
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
                    Log.d("ㅅㅅㅅㅅㅅ", "ㄱㄱㄱ");
                    Prefs.putString(Constant.LOGIN_KEY, "Token " + response.body().key);
                    //Utils.registerDevice();
                    Toast.makeText(context, "success?", Toast.LENGTH_SHORT).show();

                } else {

                    Log.d("연결", Integer.toString(response.code()));
                }
            }

            @Override
            public void onFailure(@NotNull Call<Token> call, @NotNull Throwable t) {
                Log.d("test1", "server error");
            }
        });
    }


    public static RegisterPasswordFragment newInstance() {
        RegisterPasswordFragment fragment = new RegisterPasswordFragment();
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
        binding = FragmentRegisterPassowordBinding.inflate(inflater);
        ButterKnife.bind(this, binding.getRoot());
        _listener.setToolbarStyle(_listener.GREEN_HAMBURGER, "회원가입");


        return binding.getRoot();

    }
}