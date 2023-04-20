package com.junhyeok.motionsensor_source;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    SensorManager objSMG;
    Sensor sensor_Gravity;

    TextView objTV_X_Gravity;
    TextView objTV_Y_Gravity;
    TextView objTV_Z_Gravity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        objSMG = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensor_Gravity = objSMG.getDefaultSensor(Sensor.TYPE_GRAVITY);

        objTV_X_Gravity = (TextView) findViewById(R.id.txtX_Gravity);
        objTV_Y_Gravity = (TextView) findViewById(R.id.txtY_Gravity);
        objTV_Z_Gravity = (TextView) findViewById(R.id.txtZ_Gravity);
    }

    @Override
    protected void onResume() {
        super.onResume();

        objSMG.registerListener(this, sensor_Gravity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        objSMG.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        switch (sensorEvent.sensor.getType()) {
            case Sensor.TYPE_GRAVITY:
                objTV_X_Gravity.setText(("X: " + sensorEvent.values[0]));
                objTV_Y_Gravity.setText(("Y: " + sensorEvent.values[1]));
                objTV_Z_Gravity.setText(("Z: " + sensorEvent.values[2]));
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}