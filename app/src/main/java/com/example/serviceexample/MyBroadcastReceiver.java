package com.example.serviceexample;

import static java.lang.Math.pow;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyBroadcastReceiver extends BroadcastReceiver {

    private final Handler handler;

    public MyBroadcastReceiver(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals( "DOWNLOAD_COMPLETE") ) {
            String ticker = intent.getStringExtra("ticker");
            int result_id = intent.getIntExtra("result", 0 );
            int volatility_id = intent.getIntExtra("volatility", 0);
            //handler.post adds the runnable to the MESSAGEQUEUE to be dispatched by the looper
            handler.post( new Runnable() {
                @Override
                public void run() {
                    Uri CONTENT_URI = Uri.parse("content://com.example.serviceexample.HistoricalDataProvider/history");
                    TextView result = (TextView) ((Activity)context).findViewById(result_id);
                    TextView volatilityResult = (TextView) ((Activity)context).findViewById(volatility_id);

                    result.setText("Calculating...");
                    volatilityResult.setText("Calculating...");
                    double sum_returns = 0.0;
                    double sum_retsquare = 0.0;
                    int count = 0;
                    Cursor cursor = context.getContentResolver().query(CONTENT_URI, null, null , new String[]{ticker}, null);
                    if (cursor.moveToFirst()) {
                        double returns = cursor.getDouble(cursor.getColumnIndexOrThrow("returns"));
                        sum_returns += returns;
                        sum_retsquare += returns*returns;
                        count ++;

                        while (!cursor.isAfterLast()) {
                            int id = cursor.getColumnIndex("id");
                            returns = cursor.getDouble(cursor.getColumnIndexOrThrow("returns"));
                            sum_returns += returns;
                            sum_retsquare += returns*returns;
                            count ++;
                            cursor.moveToNext();
                            Log.v("data", returns + "");
                        }
                    }
                    else {
                        result.setText("No Records Found");
                    }

//                  logic for calculation of the annualized values
                    double avg = sum_returns / (double)(count- 1);
                    double annRet = 250 * avg;
                    double var = sum_retsquare/ (double)(count- 1) - avg*avg;
                    double asd = Math.sqrt(250) * Math.sqrt(var);
                    String toRet = String.format("%.2f", annRet*100.0);
                    result.setText(toRet + "%");
                    String toVRet = String.format("%.2f", asd*100.0);
                    volatilityResult.setText(toVRet + "%");
                    Log.v("DOWNLOAD ", "SUCCESS");
                }
            });
        }else if(intent.getAction().equals( "DOWNLOAD_FAILED")){
            Log.v("download", "FAILED");
            int result_id = intent.getIntExtra("result", 0 );
            int volatility_id = intent.getIntExtra("volatility", 0);
            handler.post( new Runnable() {
                @Override
                public void run() {
                    TextView result = (TextView) ((Activity)context).findViewById(result_id);
                    TextView volatilityResult = (TextView) ((Activity)context).findViewById(volatility_id);
                    result.setText("Bad Ticker");
                    volatilityResult.setText("Bad Ticker");
                }
            });
        }
    }
}
