package com.thustop.thestop;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;

import com.thustop.R;
import com.thustop.thestop.widgets.NotoButton;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Objects;

public class CustomTimePickerDialog extends DialogBase {
    private Context context;
    private NotoButton btOk;
    private TimePicker timePicker;
    private ImageView back;
    private String noon;
    private int h, min;
    private String time;
    final Calendar cal = Calendar.getInstance();

    private CustomTimePickerDialogListener dialogListener;
    private static final int INTERVAL = 10;
    private static final DecimalFormat FORMATTER = new DecimalFormat("00");


    private NumberPicker minutePicker;

    public void setDialogListener(CustomTimePickerDialogListener dialogListener){
        this.dialogListener = dialogListener;
    }

    public CustomTimePickerDialog(@NonNull Context context) {
        super(context);
        this.context = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_time_picker);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btOk = findViewById(R.id.bt_ok);
        timePicker = findViewById(R.id.timePicker);
        timePicker.setIs24HourView(false);
        setMinutePicker();
        setCurTime();


        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                h = hour;
                min = getMinute();
            }
        });

        btOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(h <12) {
                    noon = "오전";
                } else {
                    noon = "오후";
                    if(h != 12) {h = h-12;}
                }
                dialogListener.onOkClick(h,min,noon);
                //time = (noon+" "+String.valueOf(h)+"시 "+String.valueOf(min)+"분");
                // 커스텀 다이얼로그를 종료한다.
                dismiss();
            }
        });
    }

    private void setCurTime(){
        h = cal.get(Calendar.HOUR_OF_DAY);
        min = (cal.get(Calendar.MINUTE)/10)*10;
    }


    public void setMinutePicker() {
        int numValues = 60 / INTERVAL;
        String[] displayedValues = new String[numValues];
        for (int i = 0; i < numValues; i++) {
            displayedValues[i] = FORMATTER.format(i * INTERVAL);
        }

        View minute = timePicker.findViewById(Resources.getSystem().getIdentifier("minute", "id", "android"));
        if ((minute != null) && (minute instanceof NumberPicker)) {
            minutePicker = (NumberPicker) minute;
            minutePicker.setMinValue(0);
            minutePicker.setMaxValue(numValues - 1);
            minutePicker.setDisplayedValues(displayedValues);
        }
    }

    public int getMinute() {
        if (minutePicker != null) {
            return (minutePicker.getValue() * INTERVAL);
        } else {
            return timePicker.getCurrentMinute();
        }
    }

    public interface CustomTimePickerDialogListener {
        void onOkClick(int hour, int minute, String noon);
    }


    /*private Context context;

    int h;
    int min;

    public TimePickerDialog(Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(final TextView time) {
        final int INTERVAL = 10;

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        //dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.time_picker_dialog);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        //final EditText message = (EditText) dlg.findViewById(R.id.mesgase);
        //final Button okButton = (Button) dlg.findViewById(R.id.okButton);
        //final Button cancelButton = (Button) dlg.findViewById(R.id.cancelButton);
        final NotoButton btOk = (NotoButton) dlg.findViewById((R.id.bt_ok));
        final TimePicker timePicker = (TimePicker) dlg.findViewById((R.id.timePicker));
        timePicker.setIs24HourView(false);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                h = hour;
                min = minute;
            }
        });
        btOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                time.setText(String.valueOf(h)+"시  "+String.valueOf(min)+"분");
                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });

    }

    public void setMinutePicker() {
        int numValues = 60 / INTERVAL;
        String[] displayedValues = new String[numValues];
        for (int i = 0; i < numValues; i++) {
            displayedValues[i] = FORMATTER.format(i * INTERVAL);
        }

        View minute = picker.findViewById(Resources.getSystem().getIdentifier("minute", "id", "android"));
        if ((minute != null) && (minute instanceof NumberPicker)) {
            minutePicker = (NumberPicker) minute;
            minutePicker.setMinValue(0);
            minutePicker.setMaxValue(numValues - 1);
            minutePicker.setDisplayedValues(displayedValues);
        }
    }

    public int getMinute() {
        if (minutePicker != null) {
            return (minutePicker.getValue() * INTERVAL);
        } else {
            return picker.getCurrentMinute();
        }
    }
*/
}