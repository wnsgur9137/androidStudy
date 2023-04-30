package com.junhyeok.busrouteid_source;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView objTV; // TextView object
    private String strServiceUrl, strServiceKey, strRouteId; // String variable
    private StringBuilder strUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        objTV = (TextView) findViewById(R.id.txtTitle);

        strServiceUrl = "/getStationByRoute";
        strServiceKey = "";
        strRouteId = "";

        strUrl = new StringBuilder("");
        strUrl.append(strServiceUrl);
        strUrl.append("?ServiceKey=" + strServiceKey);
        strUrl.append("&busRouteId=" + strRouteId);

        DownloadWebpageTask objTask = new DownloadWebpageTask();
        objTask.execute(strUrl.toString());
    }

    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                String strData = downloadUrl((String)urls[0]);
                return strData;
            } catch(IOException e) {
                return "Fail download !";
            }
        }

        // Output the result to the activity
        protected void onPostExecute(String result) { objTV.setText(result); }

        private String downloadUrl(String myUrl) throws IOException {
            String strLine = null;
            String strPage = "";

            HttpURLConnection urlConn = null;
            try {
                URL url = new URL(myUrl);
                urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setRequestMethod("GET");
                urlConn.setRequestProperty("Content-type", "application/json");

                BufferedReader bufReader;
                bufReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

                while ((strLine = bufReader.readLine()) != null) {
                    strPage += strLine;
                }
                return strPage;
            } finally {
                urlConn.disconnect();
            }
        }
    }
}