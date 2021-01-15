package com.thustop_00;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.thustop_00.widgets.NotoTextView;

import java.util.Objects;

public class TicketPointConfirmDialog extends DialogBase {
    private NotoTextView tvDate;
    private NotoTextView tvPoint;
    private NotoTextView btCancel;
    private NotoTextView btConfirm;
    private TicketPointConfirmListener ticketPointConfirmListener;
    private int year, month, day;
    private int point;


    public TicketPointConfirmDialog(@NonNull Context context,int year, int month, int day, int point) {
        super(context);
        this.year = year;
        this.month = month;
        this.day = day;
        this.point = point;
    }

    public interface TicketPointConfirmListener {
        void onConfirmClick();

    }
    public void setDialogListener(TicketPointConfirmListener ticketPointConfirmListener){
        this.ticketPointConfirmListener = ticketPointConfirmListener;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_ticket_point_confirm);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        tvDate = findViewById(R.id.tv_dtpc_date);
        tvPoint = findViewById(R.id.tv_dtpc_point);
        btCancel = findViewById(R.id.tv_dtpc_cancel);
        btConfirm = findViewById(R.id.tv_dtpc_confirm);
        tvDate.setText(String.format("%d년 %d월 %d일", year, month, day));
        tvPoint.setText(String.format("%d P", point));

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ticketPointConfirmListener.onConfirmClick();
                dismiss();
            }
        });

    }
}
