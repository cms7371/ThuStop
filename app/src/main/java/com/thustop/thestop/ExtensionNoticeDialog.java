package com.thustop.thestop;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thustop.R;
import com.thustop.thestop.widgets.NotoTextView;

import java.util.Objects;


public class ExtensionNoticeDialog extends DialogBase {
    private int date;
    private NotoTextView tvExtensionDate;
    private NotoTextView tvExtension2;
    private NotoTextView tvExtension3;


    public ExtensionNoticeDialog(@NonNull Context context, int date) {
        super(context);
        this.date=date;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_extension_notice);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        tvExtensionDate = findViewById(R.id.tv_extension_date);
        tvExtension2 = findViewById(R.id.tv_extension_2);
        tvExtension3 = findViewById(R.id.tv_extension_3);
        switch (date) {
            case 7:
                tvExtensionDate.setText("7일");
                break;
            case 5:
                tvExtensionDate.setText("5일");
                break;
            case 3:
                tvExtensionDate.setText("3일");
                break;
            case 0:
                tvExtensionDate.setText("오늘");
                tvExtension2.setText("끝나요!");
                tvExtension3.setText("내일부터 탑승 불가해요ㅠㅠ");
                break;

        }

    }

}