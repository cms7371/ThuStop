package com.thustop_00;

import android.app.Application;

import io.gitple.android.sdk.Gitple;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // 깃플 상담 초기화
        Gitple.initialize(this, "ksawojeLwMyVW7m7nNTaAFvhPl1mq3");

        // 깃플 상담 화면에서 헤더메뉴를 표시하지 않을 경우
        Gitple.config().setHideHeader(true);
    }
}
