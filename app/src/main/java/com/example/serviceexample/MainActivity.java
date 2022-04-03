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
    private TextView result , result1;
    private EditText ticker , ticker1;

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
        ticker = (EditText) findViewById(R.id.edit_ticker);
        ticker1 = (EditText) findViewById(R.id.edit_ticker1);

        // start service, pass ticker info via an intent
        //button .setOnclick
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyService.class);
                intent.putExtra("ticker", String.valueOf(ticker.getText()));
                intent.putExtra("result", R.id.textview_result);
//                Log.v("main res =", String.valueOf(R.id.textview_result) );
                startService(intent);

                // comment off this part the thing will work normally
                Intent intent1 = new Intent(getApplicationContext(), MyService.class);
                intent1.putExtra("ticker", String.valueOf(ticker1.getText()));
                intent1.putExtra("result", R.id.textview_result2);
                startService(intent1);
            }
        });

        // register broadcast receiver to get informed that data is downloaded so that we can calc

        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.setText("Waiting for data.. ");
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
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(myBroadcastReceiver);
    }


}