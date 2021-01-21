package com.thustop.thestop;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import com.thustop.R;
import com.thustop.thestop.widgets.NotoTextView;

import java.util.Objects;

public class TicketPointNoticeDialog extends DialogBase {

    public TicketPointNoticeDialog(Context context) {super(context);}

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_ticket_point_notice);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        NotoTextView explain1 = findViewById(R.id.tv_explain1);
        NotoTextView explain2 = findViewById(R.id.tv_explain2);

        colorText(explain1,R.string.tv_explain1_colored,R.color.Primary);
        colorText(explain2,R.string.tv_explain2_colored_1,R.color.Primary);
        colorText(explain2,R.string.tv_explain2_colored_2,R.color.Primary);




    }

    public void colorText(NotoTextView textView, int strAddress, int color) {
        /* Get the string of the view to be colored */
        String str = textView.getText().toString();
        /* Get string to be colored from address */
        String coloredStr = getContext().getString(strAddress);
        /* Instantiate spannable to color view's string*/
        Spannable span = (Spannable)textView.getText();
        /* Find position of start and end of coloredStr on str */
        int index_s=str.indexOf(coloredStr);
        int index_e = index_s+coloredStr.length();
        /* Color the view's string with upper variables */
        span.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(color)),index_s,index_e, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

}
