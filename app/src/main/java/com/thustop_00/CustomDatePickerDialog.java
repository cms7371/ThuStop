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
    private int width;
    private Calendar start, end;
    private ArrayList<ArrayList<String>> calendarList;
    private int year, month, day;
    private int preposition = -1;
    private NotoTextView preSelectedItem;


    public CustomDatePickerDialog(@NonNull Context context, Calendar start, Calendar end) {
        super(context);
        this.context = context;
        this.start = start;
        this.end = end;
    }

    public void setDialogListener(CustomDatePickerDialog.CustomDatePickerDialogListener customDatePickerDialogListener){
        this.customDatePickerDialogListener = customDatePickerDialogListener;
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_date_picker);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        calendarList = setCalendarList(start, end);


        yearMonth = findViewById(R.id.tv_year_month);
        calendar = findViewById(R.id.date_picker);
        CalendarAdapter rvAdapter = new CalendarAdapter(context, calendarList);
        calendar.setAdapter(rvAdapter);




        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(calendar);

        SnapPagerListener listener = new SnapPagerListener(
                pagerSnapHelper,
                SnapPagerListener.ON_SETTLED,
                false,
                new SnapPagerListener.OnChangeListener() {
                    @Override
                    public void onSnapped(int position) {
                        Log.d("스냅", String.valueOf(position));
                        yearMonth.setText(calendarList.get(position).get(calendarList.get(position).size()-1));
                    }
                }
        );
        calendar.addOnScrollListener(listener);


        btOk = findViewById(R.id.tv_ok);
        btCancel = findViewById(R.id.tv_cancel);


        float density = context.getResources().getDisplayMetrics().density;
        width = Math.round((float) 30 * density);
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (day != 0) {
                    customDatePickerDialogListener.onOkClick(year,month,day);
                }

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

    public ArrayList<ArrayList<String>> setCalendarList(Calendar start, Calendar end) {
        ArrayList<ArrayList<String>> Calendars = new ArrayList<>();
        // 현재 날짜 셋팅
        Calendar c = Calendar.getInstance();
        // 시작일이 오늘 보다 이전이라면,
        if (start.before(c)) start = Calendar.getInstance();
        int startYear = start.get(Calendar.YEAR);
        int startMonth = start.get(Calendar.MONTH);
        int endYear = end.get(Calendar.YEAR);
        int endMonth = end.get(Calendar.MONTH);


        if (startMonth == endMonth) {
            c.set(startYear, startMonth, 1);
            int dayNum = c.get(Calendar.DAY_OF_WEEK);
            ArrayList<String> dayList = new ArrayList<String>();

            dayList.add("일");
            dayList.add("월");
            dayList.add("화");
            dayList.add("수");
            dayList.add("목");
            dayList.add("금");
            dayList.add("토");

            for(int i = 1; i < dayNum; i++) {
                dayList.add("");
            }
            int finalDate = c.getActualMaximum(Calendar.DAY_OF_MONTH);
            Log.d("데이오브먼스", String.valueOf(finalDate));
            for (int i = 0; i < finalDate; i++) {
                dayList.add(String.valueOf(i + 1));
            }
            Calendars.add(dayList);
            dayList.add(startYear+"년 "+(startMonth+1)+"월");

            return Calendars;
        } else if(startYear == endYear) {

            for (int i = startMonth; i <= endMonth; i++) {
                c.set(startYear, i, 1);
                int dayNum = c.get(Calendar.DAY_OF_WEEK);
                ArrayList<String> dayList = new ArrayList<String>();

                dayList.add("일");
                dayList.add("월");
                dayList.add("화");
                dayList.add("수");
                dayList.add("목");
                dayList.add("금");
                dayList.add("토");

                for(int j = 1; j < dayNum; j++) {
                    dayList.add("");
                }
                int finalDate = c.getActualMaximum(Calendar.DAY_OF_MONTH);
                Log.d("데이오브먼스", String.valueOf(finalDate));
                for (int j = 0; j < finalDate; j++) {
                    dayList.add(String.valueOf(j + 1));
                }
                Calendars.add(dayList);
                dayList.add(startYear+"년 "+(i+1)+"월");
            }

            return Calendars;

        }
        return Calendars;
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
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                        if (view instanceof NotoTextView) {
                            if (preposition != -1) {
                                preSelectedItem.setBackgroundResource(R.color.White);
                                preSelectedItem.setTextColor(context.getResources().getColor(R.color.TextBlack));
                            }
                            if (preposition == position) {
                                preposition = -1;
                                preSelectedItem = null;
                                day = 0;
                            } else {
                                view.setBackgroundResource(R.drawable.bg_round25_green);
                                ((NotoTextView)view).setTextColor(context.getResources().getColor(R.color.White));
                                String ym = yearMonth.getText().toString();
                                int ymLen = ym.length();
                                year = Integer.parseInt(ym.substring(0,4));
                                Log.d("년", String.valueOf(year));
                                month = Integer.parseInt(ym.substring(6,ymLen-1));
                                Log.d("달", String.valueOf(month));
                                day = Integer.parseInt(((NotoTextView)view).getText().toString());
                                preposition = position;
                                preSelectedItem = (NotoTextView)view;
                            }
                        }

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
