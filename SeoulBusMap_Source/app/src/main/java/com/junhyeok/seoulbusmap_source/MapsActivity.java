package com.junhyeok.seoulbusmap_source;

import androidx.fragment.app.FragmentActivity;

import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.junhyeok.seoulbusmap_source.databinding.ActivityMapsBinding;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private String strServiceUrl, strServiceKey, strBusRouteId;
    private StringBuilder strUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        strServiceUrl = "http://ws.bus.go.kr/api/rest/busRouteInfo/getRouteInfo";
        strServiceKey = "bGqRxsuXpTAn8SxJgdXqAxFXyV1aWwo9rpLGFpnvaBO9QP7xf4gYfrpIro7KnsHPh13tUfoRmiQMP1cvcLoB7A==";
        strBusRouteId = "100100063";

        strUrl = new StringBuilder("");
        strUrl.append(strServiceUrl);
        strUrl.append("?ServiceKey=" + strServiceKey);
        strUrl.append("&busRouteId=" + strBusRouteId);

        DownloadWebpageTask1 objTask1 = new DownloadWebpageTask1();
        objTask1.execute(strUrl.toString());
    }

    private class DownloadWebpageTask1 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                String strData = downloadUrl((String) urls[0]);
                return strData;
            } catch (IOException e) {
                return "Fail download !";
            }
        }

        protected void onPostExecute(String result) {
            String strHeaderCd = "";
            String strBusRouteId = "";
            String strBusRouteNo = "";

            boolean bSet_HeaderCd = false;
            boolean bSet_BusRouteId = false;
            boolean bSet_BusRouteNo = false;

            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();

                xpp.setInput(new StringReader(result));
                int eventType = xpp.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_DOCUMENT) {
                        ;
                    } else if (eventType == XmlPullParser.START_TAG) {
                        String tag_name = xpp.getName();
                        if (tag_name.equals("headerCd")) bSet_HeaderCd = true;
                        if (tag_name.equals("busRouteId")) bSet_BusRouteId = true;
                        if (tag_name.equals("busRouteNm")) bSet_BusRouteNo = true;
                    } else if (eventType == XmlPullParser.TEXT) {
                        if (bSet_HeaderCd) {
                            strHeaderCd = xpp.getText();
                            bSet_HeaderCd = false;
                        }
                        if (strHeaderCd.equals("0")) {
                            if (bSet_BusRouteId) {
                                strBusRouteId = xpp.getText();
                                bSet_BusRouteId = false;
                            }
                            if (bSet_BusRouteNo) {
                                strBusRouteNo = xpp.getText();
                                bSet_BusRouteNo = false;
                            }
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        ;
                    }
                    eventType = xpp.next();
                }
            } catch (Exception e) {
                ;
            }
            strServiceUrl = "http://ws.bus.go.kr/api/rest/buspos/getBusPosByRtid";

            strUrl = new StringBuilder("");
            strUrl.append(strServiceUrl);
            strUrl.append("?ServiceKey=" + strServiceKey);
            strUrl.append("&busRouteId=" + strBusRouteId);

            DownloadWebpageTask2 objTask2 = new DownloadWebpageTask2();
            objTask2.execute(strUrl.toString());
        }

        private String downloadUrl(String myUrl) throws IOException {
            HttpURLConnection urlConn = null;
            try {
                URL url = new URL(myUrl);
                urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setRequestMethod("GET");
                urlConn.setRequestProperty("Content-type", "application/json");

                BufferedReader bufReader;
                bufReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

                String strLine = null;
                String strPage = "";
                while ((strLine = bufReader.readLine()) != null) {
                    strPage += strLine;
                }
                return strPage;
            } finally {
                urlConn.disconnect();
            }
        }

        private class DownloadWebpageTask2 extends DownloadWebpageTask1 {
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

                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {
                            ;
                        } else if (eventType == XmlPullParser.START_TAG) {
                            String tag_name = xpp.getName();
                            if (tag_name.equals("headerCd")) bSet_HeaderCd = false;
                            if (tag_name.equals("gpsX")) bSet_GpsX = true;
                            if (tag_name.equals("gpsY")) bSet_GpsY = true;
                            if (tag_name.equals("plainNo")) bSet_PlainNo = true;
                        } else if (eventType == XmlPullParser.TEXT) {
                            if (bSet_HeaderCd) {
                                strHeaderCd = xpp.getText();
                                bSet_HeaderCd = false;
                            }
                            if (strHeaderCd.equals("0")) {
                                if (bSet_GpsX) {
                                    strGpsX = xpp.getText();
                                    bSet_GpsX = false;
                                }
                                if (bSet_GpsY) {
                                    strGpsY = xpp.getText();
                                    bSet_GpsY = false;
                                }
                                if (bSet_PlainNo) {
                                    strPlainNo = xpp.getText();
                                    bSet_PlainNo = false;
                                    mDisplayBus(strGpsX, strGpsY, strPlainNo);
                                }
                            }
                        } else if (eventType == XmlPullParser.END_TAG) {
                            ;
                        }
                    }
                } catch (Exception e) {
                    ;
                }
            }
        }

        private void mDisplayBus(String gpsX, String gpsY, String plainNo) {
            double latitude;
            double longtitude;
            LatLng objLocation;

            latitude = Double.parseDouble(gpsY);
            longtitude = Double.parseDouble(gpsX);
            objLocation = new LatLng(latitude, longtitude);

            Marker objMK = mMap.addMarker(new MarkerOptions()
                    .position(objLocation)
                    .title(plainNo)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus)));
            objMK.showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLng(objLocation));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(11));
        }

    }
}