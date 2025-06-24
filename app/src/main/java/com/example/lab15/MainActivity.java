package com.example.lab15;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    EditText editQuery;
    TextView textThread, textThread2;
    Thread t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editQuery = findViewById(R.id.edit_query);
        textThread = findViewById(R.id.tv_thread_result);
        textThread2 = findViewById(R.id.tv_thread_result2);

        findViewById(R.id.btn_start_thread).setOnClickListener(v -> {
            startThread();
            startThread2();
        });
    }

    private void startThread() {
        final String threadName = editQuery.getText().toString();
        t = new Thread(() -> runOnUiThread(() -> textThread.setText("Thread: " + threadName)));
        t.setName(threadName);
        t.start();
    }

    private void startThread2() {
        new Thread(() -> {
            try {
                Thread.sleep(5000);
                runOnUiThread(() -> textThread2.setText("FPTUniversity.com"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}