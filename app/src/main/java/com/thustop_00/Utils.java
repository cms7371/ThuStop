package com.thustop_00;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.pixplicity.easyprefs.library.Prefs;
import com.thustop_00.model.FCMReg;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Utils {
    private final static String TAG = "Utils";

    static public void registerDevice() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        if (!token.equals(Prefs.getString(Constant.FID, ""))) {
                            FCMReg reg = new FCMReg();
                            reg.registration_id = token;
                            //TODO FCM 테스트 코드 삭제해야함

                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(Constant.SERVER_URL)
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();
                            RestApi api = retrofit.create(RestApi.class);

                            Call<FCMReg> call = api.registerDevice(Prefs.getString(Constant.LOGIN_KEY, ""), reg);
                            call.enqueue(new Callback<FCMReg>() {
                                @Override
                                public void onResponse(@NotNull Call<FCMReg> call, @NotNull Response<FCMReg> response) {
                                    if (response.isSuccessful() && (response.body() != null)) {
                                        Prefs.putString(Constant.FID, response.body().registration_id);
                                    }
                                    Log.d(TAG, "onResponse: FCM 등록 성공");
                                }

                                @Override
                                public void onFailure(@NotNull Call<FCMReg> call, @NotNull Throwable t) {
                                    Log.d(TAG, "onFailure: FCM 서버 등록 실패");
                                }
                            });
                        }
                        // Log and toast
                    }
                });
    }

    /**
     * 예시 코드
     * textView.setText(Utils.getBoldSpan(getContext(),"1회\n무료 탑승권", new String[] {"1회", "탑승권"}));
     **/
    static public SpannableStringBuilder getBoldSpan(Context context, String mainString, String[] boldStrings) {
        Typeface boldFace = Typeface.createFromAsset(context.getAssets(), "NotoSansKR-Bold-Hestia.otf");
        SpannableStringBuilder span = new SpannableStringBuilder(mainString);
        int index_start, index_end;
        for (String bold : boldStrings) {
            index_start = mainString.indexOf(bold);
            index_end = index_start + bold.length();
            span.setSpan(new CustomTypefaceSpan("", boldFace), index_start, index_end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        return span;
    }

}
