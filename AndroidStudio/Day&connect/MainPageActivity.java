package com.example.tcptest2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MainPageActivity extends AppCompatActivity {

    private static String SERVER_IP = "";
    private static final int SERVER_PORT = 8080;
    String responseD="";
    String[] data;

    Button goStatistics;
    LinearLayout bar;
    TextView test;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);

        goStatistics = findViewById(R.id.go_statisticpage);
        bar = findViewById(R.id.bar);
        test = findViewById(R.id.textView);

        // 전달받은 서버 확인
        Intent intent = getIntent();
        String signal = intent.getStringExtra("IP");
        SERVER_IP = signal;

        // 서버에 데이터 요청
        NetworkTask networkTask = new NetworkTask();
        networkTask.execute("main_page");


        goStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), StatisticsPageActivity.class);
                intent.putExtra("IP", SERVER_IP);
                startActivity(intent);
            }
        });


        myView mview= new myView(bar.getContext());
        bar.addView(mview);

    }

    public class NetworkTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String message = params[0];
            try {
                // 서버에 연결 및 작업 수행
                Socket socket = new Socket(SERVER_IP, SERVER_PORT);

                // 서버에 요청 전송
                // 입출력 스트림 생성
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                // 서버로 데이터 전송
                out.println(message);

                // 서버로부터 데이터 수신
                String responseData = in.readLine();
                System.out.println("서버에서 받은 메시지: " + responseData);

                data=responseData.split("");

                for(String i : data){
                    responseD = responseD + i;
                }

                // 결과 반환
                return "작업 완료";

            } catch (IOException e) {
                e.printStackTrace();
                return "작업 실패";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // 작업 완료 후 UI 업데이트 또는 페이지 이동 등 수행
            if (result.equals("작업 완료")) {
                // 페이지 이동 등 원하는 동작 수행
                test.setText(responseD);
            } else {
                // 에러 처리
                //socket.close();
            }
        }
    }

    public class myView extends View {
        public myView(Context context) {
            super(context);
            // TODO Auto-generated constructor stub
        }

        public void onDraw(Canvas canvas) {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            //canvas.drawColor(Color.YELLOW);

            int start = 25;
            int end=25;
            int j;
            Rect rect1 = new Rect(30,80,30+100,80+100);

            int i;
            for(i=0; i<responseD.length();i++){ //.charAt()
                end = start + 3;
                if(responseD.charAt(i) == '0'){paint.setColor(Color.WHITE);}
                else if(responseD.charAt(i) == '1'){paint.setColor(Color.BLUE);}
                else if(responseD.charAt(i) == '2'){paint.setColor(Color.RED);}
                else{paint.setColor(Color.BLACK);}
                rect1 = new Rect( start,0,end,150);
                canvas.drawRect(rect1, paint);
                //다음 시작점은 현재의 끝점
                start = end;
            }
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);
            Rect rect2 = new Rect( 25,0,end,150);
            canvas.drawRect(rect2, paint);

        }

    }
}
