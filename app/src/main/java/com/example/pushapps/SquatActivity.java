package com.example.pushapps;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.Layout;
import android.util.FloatMath;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class SquatActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorMan;
    private Sensor accelerometer;

    //Interface
    private ConstraintLayout layout;
    private TextView counterText;
    private Button switchButton;

    //Data
    private float[] mGravity;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    private Integer squatCounter = 0;

    //State
    private boolean ongoing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.squat_activity);
        layout = (ConstraintLayout) findViewById(R.id.layout);
        counterText = (TextView) findViewById(R.id.textView);
        switchButton = (Button) findViewById(R.id.sw);

        sensorMan = (SensorManager)getSystemService(SENSOR_SERVICE);
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER && ongoing){
            mGravity = event.values.clone();
            // Shake detection
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            if (Math.abs(x) < 1) { x = 0; }
            if (Math.abs(y) < 1) { y = 0; }
            if (Math.abs(z) < 1) { z = 0; }

            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float)Math.sqrt(x*x + y*y + z*z);
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            // Make this higher or lower according to how much
            // motion you want to detect
            if(mAccel > 15) {
                squatCounter++;
                counterText.setText(squatCounter.toString());
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // required method
    }

    public void onBegin(View view) {
        if(!ongoing) {
            ongoing = true;
            switchButton.setText("Arrêter");
        } else {
            ongoing = false;
            switchButton.setText("Démarrer");
        }
    }

    public void goToSquatActivity(View view) {

    }

    public void goTobBackToMainActivity(View view) {
        finish();
    }
}