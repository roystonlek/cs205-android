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

    private Button start, calc;
    private TextView result , result1 , result2 , result3;
    private TextView volatility , volatility1,volatility2,volatility3;
    private EditText ticker , ticker1,ticker2 , ticker3;

//    Uri CONTENT_URI = Uri.parse("content://com.example.serviceexample.HistoricalDataProvider/history");
    private BroadcastReceiver myBroadcastReceiver;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set up layout

        setContentView(R.layout.activitymain);

        start = (Button) findViewById(R.id.start_button);
        calc = (Button) findViewById(R.id.calc_button);
        result = (TextView) findViewById(R.id.textview_result);
        result1 = (TextView) findViewById(R.id.textview_result2);
        result2 = (TextView)findViewById(R.id.textview_result3);
        result3 = (TextView)findViewById(R.id.textview_result4);
        ticker = (EditText) findViewById(R.id.edit_ticker);
        ticker1 = (EditText) findViewById(R.id.edit_ticker1);
        ticker2 = (EditText) findViewById(R.id.edit_ticker2);
        ticker3 = (EditText) findViewById(R.id.edit_ticker3);
        volatility = (TextView) findViewById(R.id.textview_volatility1);
        volatility1 = (TextView) findViewById(R.id.textview_volatility2);
        volatility2 = (TextView) findViewById(R.id.textview_volatility3);
        volatility3 = (TextView) findViewById(R.id.textview_volatility4);

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
                    volatility.setText("Generating...");
                }
                if(!String.valueOf(ticker1.getText()).equals("")){
                    tickers[1] = String.valueOf(ticker1.getText());
                    resultids[1] = R.id.textview_result2;
                    volatilities[1] = R.id.textview_volatility2;
                    result1.setText("Generating... ");
                    volatility1.setText("Generating...");
                }else{
                    result1.setText("Empty Ticker");
                    volatility1.setText("Generating...");
                }
                if(!String.valueOf(ticker2.getText()).equals("")){
                    tickers[2] = String.valueOf(ticker2.getText());
                    resultids[2] = R.id.textview_result3;
                    volatilities[2] = R.id.textview_volatility3;
                    result2.setText("Generating... ");
                    volatility2.setText("Generating...");
                }else{
                    result2.setText("Empty Ticker");
                    volatility2.setText("Generating...");
                }
                if(!String.valueOf(ticker3.getText()).equals("")){
                    tickers[3] = String.valueOf(ticker3.getText());
                    resultids[3] = R.id.textview_result4;
                    volatilities[3] = R.id.textview_volatility4;
                    result3.setText("Generating...");
                    volatility3.setText("Generating...");
                }else{
                    result3.setText("Empty Ticker");
                    volatility3.setText("Generating...");
                }

                Intent test = new Intent(getApplicationContext(),MyService.class);
                test.putExtra("ids", resultids);
                test.putExtra("tickers", tickers);
                test.putExtra("volatilities", volatilities);
                startService(test);
            }
        });

        // register broadcast receiver to get informed that data is downloaded so that we can calc

        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.setText("Waiting for data.. ");
                result1.setText("Waiting for data.. ");
                result2.setText("Waiting for data.. ");
                result3.setText("Waiting for data.. ");
//                myBroadcastReceiver = new MyBroadcastReceiver(new Handler(Looper.getMainLooper()));
//                registerReceiver(myBroadcastReceiver, new IntentFilter("DOWNLOAD_COMPLETE"));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
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