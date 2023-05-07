package com.junhyeok.navermap_source;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        LatLng objLocation;
        double dLatitude = 37.448344;
        double dLongitude = 126.657474;

        naverMap.setMapType(NaverMap.MapType.Navi);

        naverMap.setSymbolScale(1.5f);

        objLocation = new LatLng(dLatitude, dLongitude);

        CameraUpdate cameraUpdate1 = CameraUpdate.scrollTo(objLocation);
        naverMap.moveCamera(cameraUpdate1);

        CameraUpdate cameraUpdate2 = CameraUpdate.zoomTo(15);
        naverMap.moveCamera(cameraUpdate2);

        Marker objMK = new Marker();
        objMK.setPosition(objLocation);
        objMK.setMap(naverMap);

        objMK.setSubCaptionText("인하공업전문대학");
        objMK.setSubCaptionColor(Color.BLUE);
        objMK.setSubCaptionHaloColor(Color.YELLOW);
        objMK.setSubCaptionTextSize(10);

        InfoWindow infoWindow1 = new InfoWindow();
        infoWindow1.setAdapter(new InfoWindow.DefaultTextAdapter(this) {
            @NonNull
            @Override
            public CharSequence getText(@NonNull InfoWindow infoWindow) {
                return "인하공전";
            }
        });
        infoWindow1.open(objMK);

        dLatitude = 37.449402;
        dLongitude = 126.657348;
        objLocation = new LatLng(dLatitude, dLongitude);

        Marker objMK2 = new Marker();
        objMK2.setPosition(objLocation);
        objMK2.setMap(naverMap);

        objMK2.setSubCaptionText("수준 원점");
        objMK2.setSubCaptionColor(Color.RED);
        objMK2.setSubCaptionHaloColor(Color.YELLOW);
        objMK2.setSubCaptionTextSize(10);

        InfoWindow infoWindow2 = new InfoWindow();
        infoWindow2.setAdapter(new InfoWindow.DefaultTextAdapter(this) {
            @NonNull
            @Override
            public CharSequence getText(@NonNull InfoWindow infoWindow) {
                return "수준 원점";
            }
        });
        infoWindow2.open(objMK2);
    }
}