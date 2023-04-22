package com.junhyeok.positionsensor_source;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager objSMG;
    Sensor sensor_Orientation;
    Sensor sensor_MagneticField;
    Sensor sensor_Proximity;

    TextView objTV_Azimuth;
    TextView objTV_Pitch;
    TextView objTV_Roll;

    TextView objTV_X_MagneticField;
    TextView objTV_Y_MagneticField;
    TextView objTV_Z_MagneticField;

    TextView objTV_Proximity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        objSMG = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor_Orientation = objSMG.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sensor_MagneticField = objSMG.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensor_Proximity = objSMG.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        objTV_Azimuth = (TextView) findViewById(R.id.txtAzimuth);
        objTV_Pitch = (TextView) findViewById(R.id.txtPitch);
        objTV_Roll = (TextView) findViewById(R.id.txtRoll);

        objTV_Proximity = (TextView) findViewById(R.id.txtProximity);
    }

    @Override
    public void onResume() {
        super.onResume();
        objSMG.registerListener(this, sensor_Orientation, SensorManager.SENSOR_DELAY_NORMAL);
        objSMG.registerListener(this, sensor_MagneticField, SensorManager.SENSOR_DELAY_NORMAL);
        objSMG.registerListener(this, sensor_Proximity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        objSMG.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        switch (sensorEvent.sensor.getType()) {
            case Sensor.TYPE_ORIENTATION:
                objTV_Azimuth.setText(("Azimuth: " + sensorEvent.values[0]));
                objTV_Pitch.setText(("Pitch: " + sensorEvent.values[1]));
                objTV_Roll.setText(("Roll: " + sensorEvent.values[2]));
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                objTV_X_MagneticField.setText(("X: " + sensorEvent.values[0]));
                objTV_Y_MagneticField.setText(("Y: " + sensorEvent.values[1]));
                objTV_Z_MagneticField.setText(("Z: " + sensorEvent.values[2]));
                break;
            case Sensor.TYPE_PROXIMITY:
                objTV_Proximity.setText((" " + sensorEvent.values[0]));
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}