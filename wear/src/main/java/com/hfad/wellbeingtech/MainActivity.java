package com.hfad.wellbeingtech;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.ServerValue;
import com.hfad.wellbeingtech.databinding.ActivityMainBinding;



public class MainActivity extends Activity implements SensorEventListener {

    private TextView mTextView;
    private ActivityMainBinding binding;
    private SensorManager mSensorManager;
    private Sensor mHeartRateSensor;
    private TextView mHeartTextView;
    private Button mStartButton;
    private Button mStopButton;
    private static final int BODY_SENSOR = 21;
    private DAODBHeartRate daodbHeartRate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mTextView = binding.text;

        mStartButton = (Button) findViewById(R.id.start_btn);
        mStopButton = (Button) findViewById(R.id.stop_btn);
        mHeartTextView = (TextView) findViewById(R.id.heart_rate);



        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BODY_SENSORS) == PackageManager.PERMISSION_GRANTED) {

            mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            mHeartRateSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

            mStartButton.setOnClickListener(v -> {
                mStartButton.setVisibility(Button.GONE);
                mStopButton.setVisibility(Button.VISIBLE);
                //mHeartTextView.setText(mHeartRateSensor.getName());

                registerSensor();


            });

            mStopButton.setOnClickListener(v -> {
                mStopButton.setVisibility(Button.GONE);
                mStartButton.setVisibility(Button.VISIBLE);

                unRegisterSensor();
            });

            Toast.makeText(this, "Permision Granted 1",Toast.LENGTH_LONG).show();


        }else if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.BODY_SENSORS)){

            // new AlertDialog.Builder(MainActivity.this).setTitle("Permission Required").setMessage("Permision Granted").setMessage("Grant permission ");
            //   .setPositiveButton("Grant Permission", (dialog, which) -> ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BODY_SENSORS},BODY_SENSOR)).setNegativeButton("No Thanks", (dialog, which) -> dialog.dismiss()).show();

            Toast.makeText(this, "Permision Denied",Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.BODY_SENSORS},BODY_SENSOR);
        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == BODY_SENSOR){

            if(grantResults.length >0 && grantResults[0]== PackageManager.PERMISSION_GRANTED ){

                Toast.makeText(this, "Permision Granted",Toast.LENGTH_LONG).show();

                // Do the measure and stuff.
                // startMeasure();


            } else {

                Toast.makeText(this, "Sensor Permision Denied",Toast.LENGTH_LONG).show();

            }
        }
    }




    private void registerSensor() {

        boolean sensorRegistered = mSensorManager.registerListener(this, mHeartRateSensor, SensorManager.SENSOR_DELAY_FASTEST);
        Log.d("Sensor Status:", " Sensor registered: " + (sensorRegistered ? "yes" : "no"));
        Log.d("Sensor Status:", " Sensor ID: " + mHeartRateSensor.getId());
        Log.d("Sensor Status:", " Sensor Reporting Mode: " + mHeartRateSensor.getReportingMode());
        Log.d("Sensor Status:", " Sensor Name: " + mHeartRateSensor.getName());
        Log.d("Sensor Status:", " Sensor Wake Up: " + mHeartRateSensor.isWakeUpSensor());
        //Log.e("Sensor Status:", " Sensor registered: " + (sensorRegistered ? "yes" : "no"));



        // mHeartRateSensor.getReportingMode();

    }

    private void unRegisterSensor() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float mHeartRateFloat = event.values[0];

        int mHeartRate = Math.round(mHeartRateFloat);

        Log.d("Sensor Status:", " Sensor registered: " + mHeartRate);

        mTextView.setText(String.valueOf(mHeartRate));

        DBHeartRate dbHeartRate = new DBHeartRate(mHeartRate, ServerValue.TIMESTAMP);
        daodbHeartRate = new DAODBHeartRate();

        daodbHeartRate.inserts(dbHeartRate).addOnSuccessListener(suc->{

            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();

        }).addOnFailureListener(err->{
            Toast.makeText(this, err.getMessage(), Toast.LENGTH_SHORT).show();
        });










        if(mHeartRateSensor.getName()!=null){
            mHeartTextView.setText(mHeartRateSensor.getName());
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}