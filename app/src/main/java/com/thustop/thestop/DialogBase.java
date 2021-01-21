package com.thustop.thestop;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.thustop.R;

public class DialogBase extends Dialog {
    public DialogBase(@NonNull Context context) {
        super(context);
    }

    public DialogBase(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected DialogBase(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        if( window != null ) {
            WindowManager.LayoutParams params = window.getAttributes();
            // 열기&닫기 시 애니메이션 설정
            params.windowAnimations = R.style.AnimationPopupStyle;
            window.setAttributes(params);
        }
    }
}
