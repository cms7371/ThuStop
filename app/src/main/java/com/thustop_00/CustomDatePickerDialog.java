package com.thustop_00;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.thustop_00.widgets.NotoTextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import butterknife.OnClick;

public class CustomDatePickerDialog extends Dialog {
    private CustomDatePickerDialogListener customDatePickerDialogListener;
    private RecyclerView calendar;
    private Context context;
    private NotoTextView btOk;
    private NotoTextView btCancel;
    private ArrayList<Calendar> calendarRange;
    private int width;
    private Calendar start, end;

    public CustomDatePickerDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public void setDialogListener(CustomDatePickerDialog.CustomDatePickerDialogListener customDatePickerDialogListener){
        this.customDatePickerDialogListener = customDatePickerDialogListener;
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_date_picker);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        calendar = findViewById(R.id.date_picker);
        CalendarAdapter rvAdapter = new CalendarAdapter(context);
        calendar.setAdapter(rvAdapter);
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(calendar);
        btOk = findViewById(R.id.tv_ok);
        btCancel = findViewById(R.id.tv_cancel);


        float density = context.getResources().getDisplayMetrics().density;
        width = Math.round((float) 30 * density);
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //customDatePickerDialogListener.onOkClick(year,month,day);
                dismiss();
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


    }

    public interface CustomDatePickerDialogListener {
        void onOkClick(int year, int month, int day);
    }


    private class CalendarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private Context context;
        CalendarAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_calendar, parent, false);
            return new CalendarHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 1;
        }

        public class CalendarHolder extends RecyclerView.ViewHolder {
            private GridView gv;
            private CalendarGridAdapter adapter;
            private ArrayList<String> dayList;
            private Date start, end;
            private NotoTextView BackSelectedItem, CurSelectedItem;
            private Calendar mCalendar;

            public CalendarHolder(@NonNull View itemView) {
                super(itemView);
                this.gv = itemView.findViewById(R.id.gv_calendar);

                dayList = new ArrayList<String>();
                dayList.add("일");
                dayList.add("월");
                dayList.add("화");
                dayList.add("수");
                dayList.add("목");
                dayList.add("금");
                dayList.add("토");

                mCalendar = Calendar.getInstance();
                mCalendar.set(2021,0,1);
                int dayNum = mCalendar.get(Calendar.DAY_OF_WEEK);

                for (int i = 1; i <dayNum; i++) {
                    dayList.add("");
                }
                Log.d("아이템",String.valueOf(dayList.size()));
                mCalendar.set(Calendar.MONTH, mCalendar.get(Calendar.MONTH)-1);
                for (int i = 0; i < mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                    dayList.add(String.valueOf(i + 1));
                }


                this.adapter = new CalendarGridAdapter(context, dayList);
                this.gv.setAdapter(adapter);
                gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        view.setBackgroundResource(R.drawable.bg_round25_green);
                    }
                });


            }
        }
    }

    private class CalendarGridAdapter extends BaseAdapter {
        private Context context;
        private Date start, end;
        private Date exception;
        private ArrayList<String> dayList;


        CalendarGridAdapter(Context context, ArrayList<String> dayList) {
            this.dayList = dayList;
            this.context = context;
        }

        @Override
        public int getCount() {
            return dayList.size();
        }

        @Override
        public Object getItem(int i) {
            return dayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean isEnabled(int position) {
            if(position%7 == 0 ||position%7 == 6 || position < 7)
                return false;
            else
                return true;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            NotoTextView tvDate = new NotoTextView(this.context);
            tvDate.setText(dayList.get(position));
            tvDate.setGravity(Gravity.CENTER);

            tvDate.setLayoutParams(new GridView.LayoutParams(width, width));
            tvDate.setTextSize(12);
            if(position%7 == 0 ||position%7 == 6 ) {
                tvDate.setTextColor(getContext().getResources().getColor(R.color.TextGray));
                tvDate.setEnabled(false);

            } else {
                tvDate.setTextColor(getContext().getResources().getColor(R.color.TextBlack));
            }
            return tvDate;
        }
    }
}
