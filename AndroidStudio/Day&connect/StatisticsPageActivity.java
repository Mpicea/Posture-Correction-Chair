package com.example.tcptest2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.os.Handler;
import android.os.Looper;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class StatisticsPageActivity extends AppCompatActivity {

    private static String SERVER_IP = "";

    private static final int SERVER_PORT = 8080;
    String responseD="";
    String[] data;

    Button goMain;
    TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statisticspage);

        goMain = (Button) findViewById(R.id.go_mainpage);
        test = findViewById(R.id.test);

        // 전달받은 서버 확인
        Intent intent = getIntent();
        String signal = intent.getStringExtra("IP");
        SERVER_IP = signal;

        // 서버에 데이터 요청
        NetworkTask networkTask = new NetworkTask();
        networkTask.execute("statistics_page");


        goMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), MainPageActivity.class);
                intent.putExtra("IP", SERVER_IP);
                startActivity(intent);
            }
        });


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
}
