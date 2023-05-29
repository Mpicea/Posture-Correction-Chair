package com.example.capstone;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //Initialize variable
    BarChart barChart;
    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assign variable
        barChart = findViewById(R.id.bar_chart);
        pieChart = findViewById(R.id.pie_chart);

        //Initialize array list
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        //use for loop
        for (int i=1;i<10;i++){
            //Convert to float
            float value= (float) (i*10.0);
            //Initialize bar chart entry
            BarEntry barEntry = new BarEntry(i, value);
            //Initialize pie chart entry
            PieEntry pieEntry = new PieEntry(i,value);
            //Add values in array list
            barEntries.add(barEntry);
            pieEntries.add(pieEntry);
        }

        //Initialize bar data set
        BarDataSet barDataSet = new BarDataSet(barEntries, "Employees");
        //Set colors
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        //Hide draw value
        barDataSet.setDrawValues(false);
        //Set bar data
        barChart.setData(new BarData(barDataSet));
        //Set animation
        barChart.animateY(5000);
        //Set description text and color
        barChart.getDescription().setText("Employees Chart");
        barChart.getDescription().setTextColor(Color.BLUE);

        //Initialize pie data set
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Student");
        //Set colors
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        //Set pid data
        pieChart.setData(new PieData(pieDataSet));
        //Set animation
        pieChart.animateXY(5000,5000);
        //Mide description
        pieChart.getDescription().setEnabled(false);
    }
}
