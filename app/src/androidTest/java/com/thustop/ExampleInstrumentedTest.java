package com.thustop;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.gson.Gson;
import com.pixplicity.easyprefs.library.Prefs;
import com.thustop.thestop.Constant;
import com.thustop.thestop.RestApi;
import com.thustop.thestop.model.PageResponse;
import com.thustop.thestop.model.User;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private Context appContext;
    private final static String TAG = "InstrumentedTest";

    @Test
    public void testUserMe() {
        useAppContext();
        initializePrefs();
        String token = Prefs.getString(Constant.LOGIN_KEY, "");
        Log.d(TAG, "testUserMe: 로그인키" + token);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RestApi api = retrofit.create(RestApi.class);

        Call<User> call = api.getUserDetails(token);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NotNull Call<User> call, @NotNull Response<User> response) {
                if (response.isSuccessful() && (response.body() != null)) {
                    Gson gson = new Gson();
                    Log.d(TAG, "onResponse: " + gson.toJson(response.body()));
                } else {
                    Log.d(TAG, "onResponse: " + "유저 정보 불러오기 실패" + response.message());
                }
            }

            @Override
            public void onFailure(@NotNull Call<User> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure: 유저 정보 불러오기 실패함", t);
            }
        });
    }

    public void useAppContext() {
        // Context of the app under test.
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.thustop_00", appContext.getPackageName());
    }

    public void initializePrefs() {
        new Prefs.Builder()
                .setContext(appContext)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(appContext.getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
    }
}