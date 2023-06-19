package com.example.monthtest;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private PieChart pieChart;

    private  static  String TAG = "MainActivit";
    private  float[] yData = {355,220};
    private  String[] xData = {"Good","Bad"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        //다른 시도
        pieChart = findViewById(R.id.pieChart);
        setupPieChart();
        loadPieChartData();
         */


         //세번째 시도
        //Log.d(TAG, "Beach chart");


        pieChart = (PieChart) findViewById(R.id.pieChart);
        //pieChart.setDescription("Sales by employee In thousands $");
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(18);
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.getDescription().setEnabled(false);

        Legend l = pieChart.getLegend();
        l.setEnabled(false);

        addDataSet();


        /*
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override

            public void onValueSelected(Entry e, Highlight h) {
                Log.d(TAG, "onValueSelected: value selected from chart");
                Log.d(TAG, "onValueSelected: "+ e.toString());
                Log.d(TAG, "onValueSelected: "+ h.toString());
            }

            @Override
            public void onNothingSelected() {

            }
        });
         */


        /* // 작동하던 것
        pieChart=findViewById(R.id.pieChart);
        showPieChart();*/
    }

    //다른시도
    /*
    private void setupPieChart(){
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.getDescription().setEnabled(false);

        Legend l = pieChart.getLegend();
        l.setEnabled(false);
    }
    private void loadPieChartData(){
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(355, "Good"));
        entries.add(new PieEntry(220, "Bad"));

        //Log.d("TestArray", Arrays.toString(new ArrayList[]{entries}));

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.BLUE);

        PieDataSet dataSet = new PieDataSet(entries, "?");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();

        pieChart.animateY(1400, Easing.EaseInOutQuad);
    }*/

    /* // 작동하던 것
    private void showPieChart(){
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        String label = "type";

        //initial data 초기 데이터
        Map<String, Integer> typeAmountMap = new HashMap<>();
        typeAmountMap.put("Good",325);
        typeAmountMap.put("Bad",215);

        //initializing colors for the entries 각 항목 초기 색상
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.RED);
        //colors.add(Color.parseColor("#304567"));
        colors.add(Color.BLUE);

        //input data and fit data into pie chart entry 파이 차트 항목에 데이터 입력 및 데이터 적합
        for(String type:typeAmountMap.keySet()){
            pieEntries.add(new PieEntry(typeAmountMap.get(type).floatValue(),type));
        }

        //collecting the entries with label name 레이블 이름으로 항목 수집
        PieDataSet pieDataSet = new PieDataSet(pieEntries,label);

        //setting text size of the value 값의 텍스트 크기 설정
        pieDataSet.setValueTextSize(12f);

        //providing color list for coloring different entries 다른 항목에 색상을 지정하기 위한 색상 리스트 제공
        pieDataSet.setColors(colors);

        //grouping the data set from entry to chart 항목에서 차트로 데이터 세트 그룹화
        PieData pieData = new PieData(pieDataSet);

        //showing the value of the entries, default true if not set 항목에서 차트로 데이터 세트 그룹화
        pieChart.setData(pieData);
        pieChart.invalidate();
    }*/

     //세번째 시도
    private void addDataSet() {
        //Log.d(TAG, "addDataSet: started");

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
        //pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(20);
        pieDataSet.setValueTextColor(Color.WHITE);



        //add colors to dataSet

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        pieDataSet.setColors(colors);

        //pieDataSet.setSliceSpace(25);



    //add legend to chart
/*//세번째 시도 중 작동이 안됨
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
*/

    //create a pieData object

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();

    }

}
