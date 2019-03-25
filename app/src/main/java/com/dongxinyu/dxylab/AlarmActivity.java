package com.dongxinyu.dxylab;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AlarmActivity extends AppCompatActivity {
    private TextView mConsoleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_console);
        mConsoleText = findViewById(R.id.console_text);
        mConsoleText.setText("Hello!Alarm");

        mConsoleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarm();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelAlarm(this);
    }

    public void setAlarm() {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(this, AlarmActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 30, pi); // Millisec * Second * Minute
        Toast.makeText(this, "setAlarm", Toast.LENGTH_SHORT).show();
    }

    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, AlarmActivity.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
        Toast.makeText(this, "cancelAlarm", Toast.LENGTH_SHORT).show();
    }
}
