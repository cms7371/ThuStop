package com.thustop_00;

import android.app.Application;
import android.content.ContextWrapper;

import com.kakao.sdk.common.KakaoSdk;
import com.pixplicity.easyprefs.library.Prefs;

import io.gitple.android.sdk.Gitple;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // 깃플 상담 초기화
        Gitple.initialize(this, "ksawojeLwMyVW7m7nNTaAFvhPl1mq3");
        KakaoSdk.init(this, Constant.KAKAO_API_KEY);
        // 깃플 상담 화면에서 헤더메뉴를 표시하지 않을 경우
        Gitple.config().setHideHeader(true);
        //pixplicity easy pref 설정
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
    }
}
