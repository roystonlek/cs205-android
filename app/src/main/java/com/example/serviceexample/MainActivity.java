package com.example.serviceexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.serviceexample.*;

public class MainActivity extends AppCompatActivity{

    private Button start ;
    private TextView result , result1 , result2 , result3, result4;
    private TextView volatility , volatility1,volatility2,volatility3, volatility4;
    private EditText ticker , ticker1, ticker2, ticker3, ticker4;

    //    Uri CONTENT_URI = Uri.parse("content://com.example.serviceexample.HistoricalDataProvider/history");
    private BroadcastReceiver myBroadcastReceiver;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set up layout

        setContentView(R.layout.activitymain);
        //initializing of the variables that will be used in the main

        start = (Button) findViewById(R.id.start_button);
        result = (TextView) findViewById(R.id.textview_result);
        result1 = (TextView) findViewById(R.id.textview_result2);
        result2 = (TextView)findViewById(R.id.textview_result3);
        result3 = (TextView)findViewById(R.id.textview_result4);
        result4 = (TextView)findViewById(R.id.textview_result5);
        ticker = (EditText) findViewById(R.id.edit_ticker);
        ticker1 = (EditText) findViewById(R.id.edit_ticker1);
        ticker2 = (EditText) findViewById(R.id.edit_ticker2);
        ticker3 = (EditText) findViewById(R.id.edit_ticker3);
        ticker4 = (EditText) findViewById(R.id.edit_ticker4);
        volatility = (TextView) findViewById(R.id.textview_volatility1);
        volatility1 = (TextView) findViewById(R.id.textview_volatility2);
        volatility2 = (TextView) findViewById(R.id.textview_volatility3);
        volatility3 = (TextView) findViewById(R.id.textview_volatility4);
        volatility4 = (TextView) findViewById(R.id.textview_volatility5);

        // start service, pass ticker info via an intent
        //button .setOnclick
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int resultids[] = {0,0,0,0,0};
                String tickers[] = new String[5];
                int volatilities[] = {0,0,0,0,0};

                if(!String.valueOf(ticker.getText()).equals("")){
                    tickers[0] = String.valueOf(ticker.getText());
                    resultids[0] = R.id.textview_result;
                    volatilities[0] = R.id.textview_volatility1;
                    result.setText("Generating...");
                    volatility.setText("Generating...");
                }else{
                    result.setText("Empty Ticker");
                    volatility.setText("Empty Ticker");
                }
                if(!String.valueOf(ticker1.getText()).equals("")){
                    tickers[1] = String.valueOf(ticker1.getText());
                    resultids[1] = R.id.textview_result2;
                    volatilities[1] = R.id.textview_volatility2;
                    result1.setText("Generating... ");
                    volatility1.setText("Generating...");
                }else{
                    result1.setText("Empty Ticker");
                    volatility1.setText("Empty Ticker");
                }
                if(!String.valueOf(ticker2.getText()).equals("")){
                    tickers[2] = String.valueOf(ticker2.getText());
                    resultids[2] = R.id.textview_result3;
                    volatilities[2] = R.id.textview_volatility3;
                    result2.setText("Generating... ");
                    volatility2.setText("Generating...");
                }else{
                    result2.setText("Empty Ticker");
                    volatility2.setText("Empty Ticker");
                }
                if(!String.valueOf(ticker3.getText()).equals("")){
                    tickers[3] = String.valueOf(ticker3.getText());
                    resultids[3] = R.id.textview_result4;
                    volatilities[3] = R.id.textview_volatility4;
                    result3.setText("Generating...");
                    volatility3.setText("Generating...");
                }else{
                    result3.setText("Empty Ticker");
                    volatility3.setText("Empty Ticker");
                }
                if(!String.valueOf(ticker4.getText()).equals("")){
                    tickers[4] = String.valueOf(ticker4.getText());
                    resultids[4] = R.id.textview_result5;
                    volatilities[4] = R.id.textview_volatility5;
                    result4.setText("Generating...");
                    volatility4.setText("Generating...");
                }else{
                    result4.setText("Empty Ticker");
                    volatility4.setText("Empty Ticker");
                }

                Intent test = new Intent(getApplicationContext(),MyService.class);
                test.putExtra("ids", resultids);
                test.putExtra("tickers", tickers);
                test.putExtra("volatilities", volatilities);
                startService(test);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        // register broadcast receiver to get informed
        // about the data downloading
        myBroadcastReceiver = new MyBroadcastReceiver(new Handler(Looper.getMainLooper()));
        registerReceiver(myBroadcastReceiver, new IntentFilter("DOWNLOAD_COMPLETE"));
        registerReceiver(myBroadcastReceiver, new IntentFilter("DOWNLOAD_FAILED"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(myBroadcastReceiver);
    }


}