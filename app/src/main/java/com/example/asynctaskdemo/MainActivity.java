package com.example.asynctaskdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
    }

    public void startAsyncTask(View view) {
        ExampleAsyncTask asyncTask = new ExampleAsyncTask(this);
        asyncTask.execute(10);
    }

    private static class ExampleAsyncTask extends AsyncTask<Integer, Integer, String> {

        private WeakReference<MainActivity> activityWR;

        public ExampleAsyncTask(MainActivity mainActivity) {
            activityWR = new WeakReference<>(mainActivity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Runs on UI Thread
            activityWR.get().progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Integer... numIterations) {
            for(int i = 0; i < numIterations[0]; i++) {
                publishProgress((i * 100) / numIterations[0]);
                SystemClock.sleep(1000);
            }
            return "Finished!";
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // Runs on UI Thread
            activityWR.get().progressBar.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Runs on UI Thread
            Toast.makeText(activityWR.get(), result, Toast.LENGTH_LONG).show();
            activityWR.get().progressBar.setProgress(0);
            activityWR.get().progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
