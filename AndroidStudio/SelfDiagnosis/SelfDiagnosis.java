package com.example.emptyviewactivityspd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.emptyviewactivityspd.decorators.EventDecorator;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    MaterialCalendarView materialCalendarView;
    String[][] csvData;
    ToggleButton tbn1;
    ToggleButton tbn2;
    ToggleButton tbn3;
    ToggleButton tbn4;
    ToggleButton tbn5;
    ToggleButton tbn6;
    TextView txtView1;
    TextView txtView2;
    TextView txtView3;
    TextView txtView4;
    TextView txtView5;
    TextView txtView6;
    TextView txtView7;

    TextView target;


    int nthLine;
    String[] dateList;
    String selectedDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clearCSV();
        writeCSVft();
        csvData = readCSV();

        materialCalendarView = (MaterialCalendarView)findViewById(R.id.calendarView);
        tbn1 = (ToggleButton) findViewById(R.id.toggleButton);
        tbn2 = (ToggleButton) findViewById(R.id.toggleButton1);
        tbn3 = (ToggleButton) findViewById(R.id.toggleButton2);
        tbn4 = (ToggleButton) findViewById(R.id.toggleButton3);
        tbn5 = (ToggleButton) findViewById(R.id.toggleButton4);
        tbn6 = (ToggleButton) findViewById(R.id.toggleButton5);
        txtView1 = (TextView)findViewById(R.id.textView);
        txtView2 = (TextView)findViewById(R.id.textView1);
        txtView3 = (TextView)findViewById(R.id.textView2);
        txtView4 = (TextView)findViewById(R.id.textView3);
        txtView5 = (TextView)findViewById(R.id.textView4);
        txtView6 = (TextView)findViewById(R.id.textView5);
        txtView7 = (TextView)findViewById(R.id.textView6);

        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2023, 0, 1)) // 달력의 시작
                .setMaximumDate(CalendarDay.from(2030, 11, 31)) // 달력의 끝
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();


        dateList = new String[csvData.length];
        for (int d = 0; d < csvData.length; d++) {
            dateList[d] = csvData[d][0];
        }

        new ApiSimulator(dateList).executeOnExecutor(Executors.newSingleThreadExecutor());

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int Year = date.getYear();
                int Month = date.getMonth() + 1;
                int Day = date.getDay();

                selectedDate = Year + "," + Month + "," + Day;

                nthLine = isIn(csvData, 0, selectedDate);
                // if the date is not in the data (nthLine == -1), add it to the data and update nthLine.
                if (nthLine < 0) {
                    csvData = Add(csvData, selectedDate);
                    updateCSV(csvData);
                    nthLine = isIn(csvData, 0, selectedDate);
                }
                showToggle(nthLine);

                // result[n][1~7]에 1이 있는 날짜에만 점 찍기 (X)
                // 사용자가 선택한 적이 있는 날짜에 점 찍기 <- 선택을 안 한 것도 기록이므로
                String[] dateList = new String[csvData.length];
                for (int d = 0; d < csvData.length; d++) {
                    dateList[d] = csvData[d][0];
                }

                new ApiSimulator(dateList).executeOnExecutor(Executors.newSingleThreadExecutor());

                monitorToggle(csvData, dateList, selectedDate);
                countOnesByMonth(Month, csvData);
                updateCSV(csvData);
                showCSV();
            }
        });
    }

    // Read the csv file and add the values into 2D string array.
    private String[][] readCSV() {
        // Get the absolute path of the file
        String filePath = getFilesDir().getAbsolutePath() + File.separator + "SelfDiagnosis.csv";

        // List to store the CSV data
        List<String[]> csvData = new ArrayList<>();

        // Read the CSV file using OpenCSV
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                csvData.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }

        // Convert the list to a 2D array
        String[][] dataArray = new String[csvData.size()][];
        for (int i = 0; i < csvData.size(); i++) {
            dataArray[i] = csvData.get(i);
        }

        return dataArray;
    }

    // Show the data in the csv file on txtView7.
    private void showCSV() {
        StringBuilder contentBuilder = new StringBuilder();

        // Get the CSV data
        String[][] csvData = readCSV();

        // Build the formatted content from the 2D array
        for (String[] row : csvData) {
            // Join the elements of the row with a comma delimiter
            String rowString = String.join(",", row);
            // Append the row string to the output with a line break
            contentBuilder.append(rowString).append("\n");
        }

        // Set the formatted content to txtView7
        txtView7.setText(contentBuilder.toString());
    }


    // Add sample values for the first time.
    private void writeCSVft() {
        // Your modified data to be written to the CSV file
        List<String[]> modifiedData = new ArrayList<>();
        modifiedData.add(new String[]{"2023,6,1", "1", "0", "0", "0", "0", "0"});
        modifiedData.add(new String[]{"2023,6,2", "0", "1", "0", "0", "0", "0"});
        modifiedData.add(new String[]{"2023,6,3", "0", "0", "1", "0", "0", "0"});

        // Get the path to the internal storage directory
        String internalStoragePath = getFilesDir().getAbsolutePath();

        // Specify the file name and path in the internal storage
        String csvFilePath = internalStoragePath + File.separator + "SelfDiagnosis.csv";

        // Write modified data to the CSV file in internal storage
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter(csvFilePath, true));
            for (String[] data : modifiedData) {
                StringBuilder lineBuilder = new StringBuilder();
                for (String value : data) {
                    lineBuilder.append('"').append(value).append('"').append(",");
                }
                lineBuilder.deleteCharAt(lineBuilder.length() - 1);  // Remove the trailing comma
                printWriter.println(lineBuilder.toString());
            }
            printWriter.close();

            // Show a success message
            Toast.makeText(this, "Data written to CSV file", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateCSV(String[][] csvData) {
        // Get the path to the internal storage directory
        String internalStoragePath = getFilesDir().getAbsolutePath();

        // Specify the file name and path in the internal storage
        String csvFilePath = internalStoragePath + File.separator + "SelfDiagnosis.csv";

        // Write the modified csvData array to the CSV file in internal storage
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath))) {
            for (String[] row : csvData) {
                writer.writeNext(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        showCSV();
    }


    // Delete all data in the csv file.
    private void clearCSV() {
        // Get the absolute path of the file
        String filePath = getFilesDir().getAbsolutePath() + File.separator + "SelfDiagnosis.csv";

        // Create the file object
        File csvFile = new File(filePath);

        // Check if the file exists
        if (csvFile.exists()) {
            // Delete the file
            boolean deleted = csvFile.delete();

            if (deleted) {
                // Create a new empty file
                try {
                    boolean created = csvFile.createNewFile();
                    if (created) {
                        Toast.makeText(this, "CSV file cleared", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to clear CSV file", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to clear CSV file", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Failed to clear CSV file", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "CSV file not found", Toast.LENGTH_SHORT).show();
        }
    }

    // Check if there is the value in the array.
    private int isIn(String[][] array, int nthVal, String val) {
        int n = -1;
        for (int sIndex = 0; sIndex < array.length; sIndex++) {
            if (csvData[sIndex][nthVal].equals(val)) {
                n = sIndex;
            }
        }
        return n;
    }

    // Add the item into the array.
    private String[][] Add(String[][] originArr, String val) {
        // (원본 배열의 크기 + 1)를 크기를 가지는 배열을 생성
        String[][] newArray = new String[originArr.length + 1][7];

        // 새로운 배열에 값을 순차적으로 할당
        for (int index = 0; index < originArr.length; index++) {
            System.arraycopy(originArr[index], 0, newArray[index], 0, 7);
        }

        // 새로운 배열의 마지막 위치에 추가하려는 값을 할당
        newArray[originArr.length][0] = val;
        newArray[originArr.length][1] = "0";
        newArray[originArr.length][2] = "0";
        newArray[originArr.length][3] = "0";
        newArray[originArr.length][4] = "0";
        newArray[originArr.length][5] = "0";
        newArray[originArr.length][6] = "0";

        // 새로운 배열을 반환
        return newArray;
    }

    // Return the length of the array.
    private void printText(String input, int nth) {
        TextView[] textViews = {txtView1, txtView2, txtView3, txtView4, txtView5, txtView6, txtView7};
        target = textViews[nth - 1];
        target.setText(input);
    }


    private void monitorToggle(String[][] csvD, String[] dList, String selDate) {
        int nLine = isIn(csvD, 0, selDate);
        tbn1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Handle the toggle button state change here
                if (isChecked) {
                    // Toggle button is checked (On)
                    csvD[nLine][1] = "1";
                } else {
                    // Toggle button is unchecked (Off)
                    csvD[nLine][1] = "0";
                }
                updateCSV(csvD);
            }
        });
        tbn2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    csvD[nLine][2] = "1";
                } else {
                    csvD[nLine][2] = "0";
                }
                updateCSV(csvD);
            }
        });
        tbn3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    csvD[nLine][3] = "1";
                } else {
                    csvD[nLine][3] = "0";
                }
                updateCSV(csvD);
            }
        });
        tbn4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    csvD[nLine][4] = "1";
                } else {
                    csvD[nLine][4] = "0";
                }
                updateCSV(csvD);
            }
        });
        tbn5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    csvD[nLine][5] = "1";
                } else {
                    csvD[nLine][5] = "0";
                }
                updateCSV(csvD);
            }
        });
        tbn6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    csvD[nLine][6] = "1";
                } else {
                    csvD[nLine][6] = "0";
                }
                updateCSV(csvD);
            }
        });

        //showStats(dList, selDate);
    }
    public void showToggle(int dateIndex) {
        if (dateIndex < 0) {
            // if the date is not in the data
        } else {
            // if the date is in the data
            tbn1.setChecked(csvData[nthLine][1].equals("1"));
            tbn2.setChecked(csvData[nthLine][2].equals("1"));
            tbn3.setChecked(csvData[nthLine][3].equals("1"));
            tbn4.setChecked(csvData[nthLine][4].equals("1"));
            tbn5.setChecked(csvData[nthLine][5].equals("1"));
            tbn6.setChecked(csvData[nthLine][6].equals("1"));
        }
    }

    private void countOnesByMonth(int month, String[][] csvData) {
        int sumOfOnes;
        int eachMonth = 0;
        for (int ith = 1; ith <= 6; ith++) {
            sumOfOnes = 0;
            // Iterate over the csvData array
            for (int d = 0; d < csvData.length; d++) {
                eachMonth = Integer.parseInt(csvData[d][0].split(",")[1]);
                // Get the value for the corresponding month (csvData[i][month])
                String value = csvData[d][ith];

                // Check if the value is "1" and increment the sum if true
                if ("1".equals(value) && month == eachMonth) {
                    sumOfOnes++;
                }
            }
            printText(Integer.toString(sumOfOnes), ith);
        }
    }


    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {
        String[] Time_Result;

        ApiSimulator(String[] Time_Result){
            this.Time_Result = Time_Result;
        }

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            Calendar calendar = Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();

            /*
            특정 날짜 달력에 점 표시
            월은 0이 1월
            년, 일은 그대로
            string 문자열인 Time_Result를 받아와서 ','를 기준으로 자르고 string을 int로 변환
            */

            for(int i = 0 ; i < Time_Result.length ; i ++){
                CalendarDay day = CalendarDay.from(calendar.getTime());
                String[] time = Time_Result[i].split(",");
                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int date = Integer.parseInt(time[2]);

                dates.add(day);
                calendar.set(year,month-1, date);
            }

            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }

            materialCalendarView.addDecorator(new EventDecorator(Color.RED, calendarDays,MainActivity.this));
        }


    }


}
