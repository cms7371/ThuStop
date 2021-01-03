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

import java.time.YearMonth;
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
    private NotoTextView yearMonth;
    private ArrayList<Calendar> calendarRange;
    private int width;
    private Calendar start, end;
    private ArrayList<ArrayList<String>> calendarList;
    private String title;

    public CustomDatePickerDialog(@NonNull Context context, ArrayList<ArrayList<String>> calendarList) {
        super(context);
        this.context = context;
        this.calendarList = calendarList;
    }

    public void setDialogListener(CustomDatePickerDialog.CustomDatePickerDialogListener customDatePickerDialogListener){
        this.customDatePickerDialogListener = customDatePickerDialogListener;
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_date_picker);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        yearMonth = findViewById(R.id.tv_year_month);
        calendar = findViewById(R.id.date_picker);
        CalendarAdapter rvAdapter = new CalendarAdapter(context, calendarList);
        calendar.setAdapter(rvAdapter);




        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(calendar);

        SnapPagerListener listener = new SnapPagerListener(
                pagerSnapHelper,
                SnapPagerListener.ON_SETTLED,
                true,
                new SnapPagerListener.OnChangeListener() {
                    @Override
                    public void onSnapped(int position) {
                        yearMonth.setText(calendarList.get(position).get(calendarList.get(position).size()-1));
                    }
                }
        );
        calendar.addOnScrollListener(listener);
        calendar.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                yearMonth.setText(title);
                Log.d("변경", "스크롤 중");
            }
        });

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


    private class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarHolder> {
        private Context context;
        private ArrayList<ArrayList<String>> calendarList;
        CalendarAdapter(Context context, ArrayList<ArrayList<String>> calendarList) {
            this.context = context;
            this.calendarList = calendarList;
        }

        @NonNull
        @Override
        public CalendarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_calendar, parent, false);
            return new CalendarHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull CalendarHolder holder, int position) {


            CalendarGridAdapter adapter = new CalendarGridAdapter(context, calendarList.get(position));
            holder.gv.setAdapter(adapter);


        }

        @Override
        public int getItemCount() {
            return calendarList.size();
        }


        public class CalendarHolder extends RecyclerView.ViewHolder {
            private GridView gv;

            public CalendarHolder(@NonNull View itemView) {
                super(itemView);
                this.gv = itemView.findViewById(R.id.gv_calendar);

                gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        view.setBackgroundResource(R.drawable.bg_round25_green);
                        ((NotoTextView) view).setTextColor(context.getResources().getColor(R.color.White));
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

            tvDate.setGravity(Gravity.CENTER);

            tvDate.setLayoutParams(new GridView.LayoutParams(width, width));
            tvDate.setTextSize(12);
            if(position%7 == 0 ||position%7 == 6 ) {
                tvDate.setText(dayList.get(position));
                tvDate.setTextColor(getContext().getResources().getColor(R.color.AchCF));
                tvDate.setEnabled(false);

            } else if (position == dayList.size()-1) {
                tvDate.setText("");
            } else {
                tvDate.setText(dayList.get(position));
                tvDate.setTextColor(getContext().getResources().getColor(R.color.TextBlack));
            }
            return tvDate;
        }
    }
}
