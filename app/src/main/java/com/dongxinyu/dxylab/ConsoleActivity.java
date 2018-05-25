package com.dongxinyu.dxylab;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ConsoleActivity extends AppCompatActivity {
    private TextView mConsoleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_console);
        mConsoleText = findViewById(R.id.console_text);
        mConsoleText.setText("Hello!");

        printStaticThread();
    }

    private void printStaticThread() {
        TestCaseStaticThread.testThread();
    }


}
