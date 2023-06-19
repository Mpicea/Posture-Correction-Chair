package com.example.monthtest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class main extends Activity {
    int total, good, bad;
    private TextView ShowText;
    private Button btntest;
    PieChart pieChart;
    private  static  String TAG = "MainActivit";
    private  float[] yData = {355,220};
    private  String[] xData = {"Good","Bad"};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        pieChart=findViewById(R.id.pieChart);
        btntest=(Button) findViewById(R.id.testmain);
        ShowText=findViewById(R.id.timeshow);

        //타페이지 이동 버튼
        btntest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), analyze.class);
                startActivity(intent);
            }
        });

        //파이차트를 그림
        showChart();

        //텍스트 보여줌
        total = Math.round((int)(yData[0]+yData[1])/60);
        good = Math.round((int)yData[0]/60);
        bad = Math.round((int)yData[1]/60);
        ShowText.setText(String.format("총 앉은 시간 : 약 %d시간 " +
                "\n잘 앉은 시간 : 약 %d시간 " +
                "\n잘 못 앉은 시간 : 약 %d시간",total,good,bad));
    }
    //기본적인 파이차트 설정
    private void showChart(){
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(16);
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.getDescription().setEnabled(false);

        Legend l = pieChart.getLegend();
        l.setEnabled(false);

        addDataSet();
    }
    //파이차트에 값을 설정하고 조정함
    private void addDataSet() {

        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();

        for(int i = 0 ; i <yData.length;i++){
            yEntrys.add(new PieEntry(yData[i],xData[i % xData.length]));
        }

        for(int i = 0 ; i <xData.length;i++){
            xEntrys.add(xData[i]);
        }

        //create the DATA
        PieDataSet pieDataSet = new PieDataSet(yEntrys,"Time");
        pieDataSet.setValueTextSize(16);
        pieDataSet.setValueTextColor(Color.WHITE);

        //add colors to dataSet

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        pieDataSet.setColors(colors);

        //create a pieData object

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter(pieChart));
        pieChart.setData(pieData);
        pieChart.invalidate();

    }
}
