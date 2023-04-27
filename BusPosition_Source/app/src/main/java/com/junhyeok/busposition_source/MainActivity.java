package com.junhyeok.busposition_source;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView objTV;
    private String strServiceUrl, strServiceKey, strRouteId;
    private StringBuilder strUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        objTV = (TextView) findViewById(R.id.txtTitle);

        strServiceUrl = "http://ws.bus.go.kr/api/rest/busRouteInfo/getStaionByRoute";
        strServiceKey = "bGqRxsuXpTAn8SxJgdXqAxFXyV1aWwo9rpLGFpnvaBO9QP7xf4gYfrpIro7KnsHPh13tUfoRmiQMP1cvcLoB7A%3D%3D";
        strRouteId = "100100063";

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

        protected void onPostExecute(String result) {
            String strHeaderCd = "";
            String strGpsX = "";
            String strGpsY = "";
            String strPlainNo = "";

            boolean bSet_HeaderCd = false;
            boolean bSet_GpsX = false;
            boolean bSet_GpsY = false;
            boolean bSet_PlainNo = false;
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();

                xpp.setInput(new StringReader(result));
                int eventType = xpp.getEventType();

                while(eventType != XmlPullParser.END_DOCUMENT) {
                    if(eventType == XmlPullParser.START_DOCUMENT) {
                        ;
                    } else if(eventType == XmlPullParser.START_TAG) {
                        String tag_name = xpp.getName();
                        if(tag_name.equals("headerCd")) bSet_HeaderCd = true;
                        if(tag_name.equals("gpsX")) bSet_GpsX = true;
                        if(tag_name.equals("gpsY")) bSet_GpsY = true;
                        if(tag_name.equals("plainNo")) bSet_PlainNo = true;
                    } else if(eventType == XmlPullParser.TEXT) {
                        if(bSet_HeaderCd) {
                            strHeaderCd = xpp.getText();
                            objTV.append("headerCd: " + strHeaderCd + "\n");
                            bSet_HeaderCd = false;
                        }
                        if(strHeaderCd.equals("0")) {
                            if(bSet_GpsX) {
                                strGpsX = xpp.getText();
                                objTV.append("gpsX: " + strGpsX + "\n");
                                bSet_GpsX = false;
                            }
                            if(bSet_GpsY) {
                                strGpsY = xpp.getText();
                                objTV.append("gpsY: " + strGpsY + "\n");
                                bSet_GpsY = false;
                            }
                            if(bSet_PlainNo) {
                                strPlainNo = xpp.getText();
                                objTV.append("strPlainNo: " + strPlainNo + "\n");
                                bSet_PlainNo = false;
                            }
                        }
                    } else if(eventType == XmlPullParser.END_TAG) {
                        ;
                    }
                    eventType = xpp.next();
                }
            } catch (Exception e) {
                objTV.setText(e.getMessage());
            }
        }

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

                while((strLine = bufReader.readLine()) != null) {
                    strPage += strLine;
                }
                return strPage;
            } finally {
                urlConn.disconnect();
            }
        }
    }
}