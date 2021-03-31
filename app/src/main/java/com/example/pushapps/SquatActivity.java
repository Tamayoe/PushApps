package com.example.pushapps;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.FloatMath;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class SquatActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorMan;
    private Sensor accelerometer;
    private int integer1 = 0;
    private int integer2 = 0;
    private float[] mGravity;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    private Integer squatCounter = 0;
    Date lastSwitch = Calendar.getInstance().getTime();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.squat_activity);
        sensorMan = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorMan.registerListener(this, accelerometer,
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorMan.unregisterListener(this);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent event) {
        TextView counterText = (TextView) findViewById(R.id.textView);
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mGravity = event.values.clone();
            // Shake detection
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            if (Math.abs(x) < 1) {
                x = 0;
            }
            if (Math.abs(y) < 1) {
                y = 0;
            }
            if (Math.abs(z) < 1) {
                z = 0;
            }

            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt(x * x + y * y + z * z);
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            // Make this higher or lower according to how much
            // motion you want to detect


//            Log.d("STATE", String.valueOf(elapsedTime));
            if ( mAccel > 10 ) {
//                if (mAccel > 13) {

//                }
                 long elapsedTime = Calendar.getInstance().getTime().getTime() - lastSwitch.getTime();
                  if (TimeUnit.MILLISECONDS.toSeconds(elapsedTime) >= 1) {

                      lastSwitch = Calendar.getInstance().getTime();
                      squatCounter++;
                      Log.d("STATE", String.valueOf(TimeUnit.MILLISECONDS.toSeconds(elapsedTime)));


                  }
            }



            counterText.setText(squatCounter.toString());
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // required method
    }

    public void goToSquatActivity(View view) {

    }

    public void goTobBackToMainActivity(View view) {
        finish();
    }
}