package com.example.lab15;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class AsyncTaskActivity extends AppCompatActivity {

    private ProgressBar pbCounter;
    private TextView tvMessage, tvWebContent;
    private Button btnCounter;
    private ImageView imvAvatar;
    private static final int MAX = 100000;
    private static final String TAG = "AsyncTaskDemo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_task);

        pbCounter = findViewById(R.id.pb_counter);
        tvMessage = findViewById(R.id.tv_message);
        tvWebContent = findViewById(R.id.tv_web_content);
        btnCounter = findViewById(R.id.btn_counter);
        imvAvatar = findViewById(R.id.imv_avatar);

        btnCounter.setOnClickListener(v -> {
            new UpdateProgressBarTask().execute(MAX);
            btnCounter.setEnabled(false);
        });

        findViewById(R.id.btn_download_image).setOnClickListener(v -> {
            new DownloadImageTask().execute("https://example.com/image.jpg"); // Replace with real URL
        });

        findViewById(R.id.btn_download_text).setOnClickListener(v -> {
            new DownloadContentWeb().execute("https://example.com"); // Replace with real URL
        });
    }

    class UpdateProgressBarTask extends AsyncTask<Integer, Integer, Void> {
        @Override
        protected void onPreExecute() {
            tvMessage.setText("Updating...");
        }

        @Override
        protected Void doInBackground(Integer... params) {
            int n = params[0];
            for (int i = 0; i < n; i++) {
                publishProgress(i);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            pbCounter.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void unused) {
            tvMessage.setText("Done");
            btnCounter.setEnabled(true);
        }
    }

    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap bm = null;
            try {
                URL url = new URL(urls[0]);
                InputStream is = url.openStream();
                bm = BitmapFactory.decodeStream(is);
                is.close();
            } catch (IOException e) {
                Log.e(TAG, "Image download error: " + e.getMessage());
            }
            return bm;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imvAvatar.setImageBitmap(bitmap);
        }
    }

    class DownloadContentWeb extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder builder = new StringBuilder();
            try {
                URL url = new URL(strings[0]);
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(url.openStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line).append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return builder.toString();
        }

        @Override
        protected void onPostExecute(String content) {
            tvWebContent.setText(content);
        }
    }
}

