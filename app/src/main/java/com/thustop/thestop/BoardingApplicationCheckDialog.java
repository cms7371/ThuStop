package com.thustop.thestop;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.thustop.R;

public class BoardingApplicationCheckDialog extends DialogBase {
    private String boarding_stop;
    private String alighting_stop;
    private BoardingCheckDialogListener listener;

    public BoardingApplicationCheckDialog(@NonNull Context context, String boarding_stop, String alighting_stop) {
        super(context, R.style.CustomFullDialog);
        this.boarding_stop = boarding_stop;
        this.alighting_stop = alighting_stop;

    }

    interface BoardingCheckDialogListener{
        void onBoardingConfirm();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_boarding_application_check);
        //처음 설정된 match_parent가 무시되기 때문에 다시 설정해줘야함.
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ((TextView)findViewById(R.id.tv_dbc_boarding_stop)).setText(boarding_stop);
        ((TextView)findViewById(R.id.tv_dbc_alighting_stop)).setText(alighting_stop);
        findViewById(R.id.bt_dbc_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.bt_dbc_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onBoardingConfirm();
            }
        });
    }

    public void setListener(BoardingCheckDialogListener listener){
        this.listener = listener;
    }
}