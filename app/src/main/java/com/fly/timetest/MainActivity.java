package com.fly.timetest;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    OptionsPickerView<String> DatePicker;
    private ArrayList<String> year = new ArrayList<>();
    private ArrayList<String> month = new ArrayList<>();
    private ArrayList<String> day = new ArrayList<>();
    private int nowYear;
    private int nowMonth;
    private int nowDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConstraintLayout constraintLayout = findViewById(R.id.dd);
        final TextView date = findViewById(R.id.date);

        getAllDate();

        DatePicker = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String year = String.valueOf((options1+1));
                String month = String.valueOf((options2+1));
                String day = String.valueOf((options3+1));

                if (year.length() < 2) {
                    year = "0" + year;
                } else if (month.length() < 2) {
                    month = "0" + month;
                } else if (day.length() < 2) {
                    day = "0" + day;
                }
                date.setText( year + "/" + month + "/" + day);
            }
        })
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {
                        Calendar calendar = Calendar.getInstance();
                        //用西元年跟月 去換最大的天數
                        calendar.set(Calendar.YEAR, options1 + 1912);
                        calendar.set(Calendar.MONTH, options2);
                        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                        //只抓取天
                        @SuppressLint("SimpleDateFormat") DateFormat formatter = new SimpleDateFormat("dd");
                        String endDate = formatter.format(calendar.getTime());
                        changeTheDay(Integer.parseInt(endDate), options1, options2, options3);
                    }
                })
                .setSubmitText("確定")
                .setContentTextSize(20)
                .build();
        DatePicker.setNPicker(year, month, day);
        DatePicker.setSelectOptions(nowYear, nowMonth - 1, nowDay);
        DatePicker.show();
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker.show();
            }
        });
    }

    private void changeTheDay(int thisDay, int options1, int options2, int options3) {
        day.clear();
        for (int i = 1; i <= thisDay; i++) {
            day.add(i + "日");
        }
        DatePicker.setNPicker(year, month, day);
        DatePicker.setSelectOptions(options1, options2, options3);
    }

    private void getAllDate() {
        Calendar calendar = Calendar.getInstance();
        nowYear = calendar.get(Calendar.YEAR) - 1911;
        for (int i = 1; i <= nowYear; i++) {
            year.add("民國" + i + "年");
        }

        nowMonth = calendar.get(Calendar.MONTH) + 1;
        for (int i = 1; i <= 12; i++) {
            month.add(i + "月");
        }

        nowDay = calendar.get(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= nowDay; i++) {
            day.add(i + "日");
        }
    }
}