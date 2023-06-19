package com.example.monthtest;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;
import android.app.Fragment;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;

public class analyze extends Activity {
    Switch switchMonthChart; //점수차트와 시간차트 스위치
    int[] monthTimeColor = new int[]{Color.BLUE, Color.RED}; //시간차트의 색
    TextView showYYYYMM, showYYYY; //선택한 날짜 출력
    DatePicker datePicker; //날짜 선택
    private  int[] scoreData, goodData, badData; //한달 DB의 값
    private float[] scoreYearDate; //1년 점수 값
    BarChart monthChart, yearChart; //한달 차트와 년 차트
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.analyze);

        datePicker = (DatePicker)findViewById(R.id.date);
        showYYYYMM = (TextView)findViewById(R.id.datetext);
        showYYYY = findViewById(R.id.yeardatetext);
        switchMonthChart = findViewById(R.id.timeOrscore);
        monthChart = findViewById(R.id.MonthView);
        yearChart = findViewById(R.id.YearView);

        //날짜 출력
        int Year = datePicker.getYear();
        int Month = datePicker.getMonth();
        showYYYYMM.setText(String.format("%d/%d",Year,Month+1));
        showYYYY.setText(String.format("%d",Year));

        //설정한 날짜 출력
        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                new DatePicker.OnDateChangedListener(){
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth){
                        showYYYYMM.setText(String.format("%d/%d",year,monthOfYear+1));
                        showYYYY.setText(String.format("%d",year));
                    }
                });

        //초기 차트 표시
        MonthScoreBar();
        YearScoreBar();

        //스위치로 한달 차트(점수제/시간제)전환
        switchMonthChart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    MonthTimeBar();
                }else{
                    MonthScoreBar();
                }
            }
        });

        //Toast myToast = Toast.makeText(this.getApplicationContext(),String.valueOf(a),Toast.LENGTH_SHORT);
        //myToast.show();
    }

    //한 달 차트(점수제)
    private void MonthScoreBar(){
        BarDataSet barDataSet1 = new BarDataSet(monthScoreDate(), "(Good/Total*100)%");
        BarData barData1 = new BarData();
        barData1.addDataSet(barDataSet1);
        monthChart.setData(barData1);
        monthChart.invalidate();
    }

    //한 달 차트(시간제)
    private void MonthTimeBar(){
        BarDataSet barDataSet2 = new BarDataSet(monthTimeDate(), "Bar Set");
        barDataSet2.setColors(monthTimeColor);

        BarData barData2 = new BarData(barDataSet2);
        monthChart.setData(barData2);
        Legend l = monthChart.getLegend();
        l.setEnabled(false);
        monthChart.invalidate();
    }

    //년 차트(점수제)
    private void YearScoreBar(){
        BarDataSet barDataSet3 = new BarDataSet(yearDate(), "(Good/Total*100)%");
        BarData barData3 = new BarData();
        barData3.addDataSet(barDataSet3);
        yearChart.setData(barData3);
        yearChart.invalidate();
    }

    //엔트리에 한 달 점수 데이터 삽임
    private ArrayList<BarEntry> monthScoreDate(){
        scoreData = new int[]{0,15,0,30,0,50,0,0,0,0,0,80,0,0,0,0,90,0,0,0,0,0,0,50,0,100,0,0,0,0,0};

        ArrayList<BarEntry> scoreList = new ArrayList<>();

        for(int i=0; i<scoreData.length; i++){
            scoreList.add(new BarEntry(i+1,scoreData[i]));
        }

        return scoreList;
    }

    //엔트리에 한 달 시간 데이터 삽입
    private ArrayList<BarEntry> monthTimeDate() {
        goodData = new int[]{0, 50, 0, 100, 0, 150, 0, 250, 0, 0, 0, 200, 0, 0, 0, 0, 300, 0, 0, 0, 0, 0, 0, 450, 0, 400, 0, 0, 0, 0, 0};
        badData = new int[]{0, 400, 0, 300, 0, 250, 0, 150, 0, 0, 0, 200, 0, 0, 0, 0, 50, 0, 100, 0, 0, 0, 0, 150, 0, 50, 0, 0, 0, 0, 0};

        ArrayList<BarEntry> timeList = new ArrayList<>();

        for (int i = 0; i < goodData.length; i++) {
            timeList.add(new BarEntry(i + 1, new float[]{Math.round((float)goodData[i]/60), Math.round((float)badData[i]/60)}));
        }

        return timeList;
    }

    //엔트리에 년 점수 데이터 삽입
    private ArrayList<BarEntry> yearDate(){
        scoreYearDate = new float[]{20,0,85,40,50,60,70,95,30,34,56,0};

        ArrayList<BarEntry> yearList = new ArrayList<>();

        for(int i=0; i<scoreYearDate.length; i++){
            yearList.add(new BarEntry(i+1,scoreYearDate[i]));
        }

        return yearList;
    }
}

