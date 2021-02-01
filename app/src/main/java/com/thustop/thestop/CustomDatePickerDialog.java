package com.thustop.thestop;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.thustop.R;
import com.thustop.thestop.widgets.NotoTextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class CustomDatePickerDialog extends DialogBase {
    private CustomDatePickerDialogListener customDatePickerDialogListener;
    private RecyclerView calendar;
    private Context context;
    private NotoTextView btOk;
    private NotoTextView btCancel;
    private NotoTextView yearMonth;
    private int width;
    private Calendar start, end;
    private ArrayList<ArrayList<Integer>> calendarList;
    private int year, month, day;
    private int preposition = -1;
    private NotoTextView preSelectedItem;

    private ImageView left, right;


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
         left = findViewById(R.id.iv_chevron_l);
         right = findViewById(R.id.iv_chevron_r);


        calendarList = setCalendarList(start, end);

        yearMonth = findViewById(R.id.tv_year_month);
        calendar = findViewById(R.id.date_picker);
        CalendarAdapter rvAdapter = new CalendarAdapter(context, calendarList);
        calendar.setAdapter(rvAdapter);
        if (calendar.getAdapter().getItemCount() == 1) {
            year = calendarList.get(0).get(0);
            month = calendarList.get(0).get(1);
            yearMonth.setText(String.format("%d년 %d월",year, month));
            left.setVisibility(View.GONE);
            right.setVisibility(View.GONE);

        }


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
                        year = calendarList.get(position).get(0);
                        month = calendarList.get(position).get(1);
                        yearMonth.setText(String.format("%d년 %d월",year, month));

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


    /***********************************************
     *
     * @param start : 티켓의 시작일을 받아옴
     * @param end   : 티켓의 끝나는 일을 받아옴
     *
     * Date picker 달력을 생성하기 위한 정보를 설정하는 함수
     *              dayList라는 배열(달력의 한 페이지 정보)을 갖는 배열(전체 date picker)을 생성(2차원 배열)
     *              daylist = [년, 월, 공란 수(시작요일 셋팅), 이번 달의 마지막 날, 티켓 시작일, 끝 일, + 운행 안하는 날...]
     *
     *
     * @return 해당 티켓의 가능한 지정범위를 반영하는 달력 정보 배열
     */
    public ArrayList<ArrayList<Integer>> setCalendarList(Calendar start, Calendar end) {
        int startYear;
        int startMonth;
        int startDate;
        ArrayList<ArrayList<Integer>> Calendars = new ArrayList<>();
        // 현재 날짜 셋팅
        Calendar c = Calendar.getInstance();
        // 시작일이 오늘 보다 이전이라면,
        if (start.before(c)) {
            startYear = c.get(Calendar.YEAR);
            startMonth = c.get(Calendar.MONTH);
            startDate = c.get(Calendar.DATE);
        } else {
            startYear = start.get(Calendar.YEAR);
            startMonth = start.get(Calendar.MONTH);
            startDate = start.get(Calendar.DATE);
        }

        int endYear = end.get(Calendar.YEAR);
        int endMonth = end.get(Calendar.MONTH);
        int endDate = end.get(Calendar.DATE);


        if (startMonth == endMonth) {
            c.set(startYear, startMonth, 1);
            int dayNum = c.get(Calendar.DAY_OF_WEEK);
            ArrayList<Integer> dayList = new ArrayList<Integer>();
            dayList.add(startYear);
            dayList.add(startMonth+1);
            Log.d("스타트먼스", String.valueOf(startMonth));
            dayList.add(dayNum-1);
            int finalDate = c.getActualMaximum(Calendar.DAY_OF_MONTH);
            Log.d("데이오브먼스", String.valueOf(finalDate));
            dayList.add(finalDate);
            dayList.add(startDate);
            Log.d("시작일", String.valueOf(startDate));
            dayList.add(endDate);
            Calendars.add(dayList);
            return Calendars;
        } else if(startYear == endYear) {

            for (int i = startMonth; i <= endMonth; i++) {
                c.set(startYear, i, 1);
                int dayNum = c.get(Calendar.DAY_OF_WEEK);
                ArrayList<Integer> dayList = new ArrayList<Integer>();


                dayList.add(startYear);
                dayList.add(i+1);
                dayList.add(dayNum-1);
                int finalDate = c.getActualMaximum(Calendar.DAY_OF_MONTH);
                Log.d("데이오브먼스", String.valueOf(finalDate));
                dayList.add(finalDate);
                if(i== startMonth) {
                    dayList.add(startDate);
                    Log.d("시작일", String.valueOf(startDate));
                } else {
                    dayList.add(0);
                    Log.d("시작일", String.valueOf(0));
                }
                if (i == endMonth) {
                    dayList.add(endDate);
                    Log.d("끝일", String.valueOf(endDate));
                } else {
                    dayList.add(0);
                    Log.d("끝일", String.valueOf(0));
                }
                Calendars.add(dayList);
            }

            return Calendars;

        }
        return Calendars;
    }

    public interface CustomDatePickerDialogListener {
        void onOkClick(int year, int month, int day);
    }


    private class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarHolder> {
        private int maxLow;
        private Context context;
        private ArrayList<ArrayList<Integer>> calendarList;
        CalendarAdapter(Context context, ArrayList<ArrayList<Integer>> calendarList) {
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
                                preSelectedItem.setTextColor(ContextCompat.getColor(context, R.color.TextBlack));
                            }
                            if (preposition == position) {
                                preposition = -1;
                                preSelectedItem = null;
                                day = 0;
                            } else {
                                view.setBackgroundResource(R.drawable.bg_round25_green);
                                ((NotoTextView)view).setTextColor(ContextCompat.getColor(context, R.color.White));
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
        private int start, end;
        private ArrayList<Integer> dayList;
        private int blankNum;
        private int finalDate;
        int date = 1;

        CalendarGridAdapter(Context context, ArrayList<Integer> dayList) {
            this.dayList = dayList;
            this.blankNum = dayList.get(2);
            this.finalDate = dayList.get(3);
            this.start = dayList.get(4);
            this.end = dayList.get(5);
            this.context = context;
        }

        @Override
        public int getCount() {
            return (blankNum+finalDate+7);
        }

        @Override
        public Object getItem(int i) {
            return (blankNum+i+7);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean isEnabled(int position) {
            if(position%7 == 0 ||position%7 == 6 || position < 7) {
                return false;
            } else {
                if (start != 0 && end != 0) {
                    if(position < (7+blankNum+start) || (position > (6+blankNum+end)&&position<(7+blankNum+finalDate))) {
                        return false;
                    } else{
                        return true;

                    }
                } else if (start != 0) {
                    if (position < (7 + blankNum + start)) {
                        return false;
                    } else {
                        return true;
                    }
                } else if (end != 0) {
                    if((position > (6+blankNum+end)&&position<(7+blankNum+finalDate))) {
                        return false;
                    } else{
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            NotoTextView tvDate = new NotoTextView(this.context);
            tvDate.setGravity(Gravity.CENTER);
            tvDate.setLayoutParams(new GridView.LayoutParams(width, width));
            tvDate.setTextSize(12);
            tvDate.setTextColor(ContextCompat.getColor(context, R.color.TextBlack));
            if (position < 7) {
                switch (position) {
                    case 0 :tvDate.setText("일");
                            tvDate.setTextColor(ContextCompat.getColor(context, R.color.AchCF));
                            tvDate.setEnabled(false);
                            break;
                    case 1 :tvDate.setText("월");
                            break;
                    case 2 :tvDate.setText("화");
                            break;
                    case 3 :tvDate.setText("수");
                            break;
                    case 4 : tvDate.setText("목");
                            break;
                    case 5 :tvDate.setText("금");
                            break;
                    case 6 :tvDate.setText("토");
                            tvDate.setTextColor(ContextCompat.getColor(context, R.color.AchCF));
                            tvDate.setEnabled(false);
                            break;
                }
            } else if(position < (7+blankNum)) {
                tvDate.setText("");
            } else if(position%7 == 0 ||position%7 == 6 ) {
                tvDate.setText(String.valueOf(date));
                tvDate.setTextColor(ContextCompat.getColor(context, R.color.AchCF));
                tvDate.setEnabled(false);
                date++;
                if (position == 6+blankNum+finalDate) { date = 1;}
            } else {
                if (start != 0 && end != 0) {
                    if(position < (7+blankNum+start) || (position > (6+blankNum+end)&&position<(7+blankNum+finalDate))) {
                        tvDate.setText(String.valueOf(date));
                        tvDate.setTextColor(ContextCompat.getColor(context, R.color.AchCF));
                        tvDate.setEnabled(false);
                    } else{
                        tvDate.setText(String.valueOf(date));
                    }
                } else if (start != 0) {
                    if (position < (7 + blankNum + start)) {
                        tvDate.setText(String.valueOf(date));
                        tvDate.setTextColor(ContextCompat.getColor(context, R.color.AchCF));
                        tvDate.setEnabled(false);
                    } else {
                        tvDate.setText(String.valueOf(date));
                    }
                } else if (end != 0) {
                    if((position > (6+blankNum+end)&&position<(7+blankNum+finalDate))) {
                        tvDate.setText(String.valueOf(date));
                        tvDate.setTextColor(ContextCompat.getColor(context, R.color.AchCF));
                        tvDate.setEnabled(false);
                    } else{
                        tvDate.setText(String.valueOf(date));
                    }
                } else {
                    tvDate.setText(String.valueOf(date));
                }
                date++;
                if (position == 6+blankNum+finalDate) { date = 1;}
            }
            Log.d("포지션", String.valueOf(date));
            return tvDate;
        }
    }






}
