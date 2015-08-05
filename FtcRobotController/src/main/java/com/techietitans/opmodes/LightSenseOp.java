package com.techietitans.opmodes;

/**
 * Created by ruhul on 7/16/15.
 */
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * An op mode that measures ambient light and turns the flash light on at a given low threshold.
 * We used the built in light sensor and camera of ZTE phone. A good future enhancement will be
 * Noise removal of light sensor with running average of last 5 measurements.
 */
public class LightSenseOp extends OpMode implements SensorEventListener {
    private String startDate;
    private SensorManager mSensorManager;
    Sensor light;

    private float lightLevel = 0.0f;       // Ambient light level in SI lux units
    private float[] mLight;       // latest sensor values
    //Set boolean flag when torch is turned on/off
    private boolean isFlashOn = false;
    //Create camera object to access flahslight
    private Camera camera;
    private Parameters p;


    /*
    * Constructor
    */
    public LightSenseOp() {

    }

    /*
    * Code to run when the op mode is first enabled goes here
    * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
    */
    @Override
    public void init() {
        startDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());

        mSensorManager = (SensorManager) hardwareMap.appContext.getSystemService(Context.SENSOR_SERVICE);
        light = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        // delay value is SENSOR_DELAY_UI which is ok for telemetry, maybe not for actual robot use
        mSensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_UI);
        //Set camera
        camera = Camera.open();
        p = camera.getParameters();


    }

    /*
    * This method will be called repeatedly in a loop
    * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
    */
    @Override
    public void loop() {
        telemetry.addData("1 Start", "LightOp started at " + startDate);
        telemetry.addData("2 light", "Light Level = " + Math.round(lightLevel) + " SI lux");

        //We set the threshold at 50 SI Lux to turn the flash light on.
        // Also, we give an offset of 30 before we turn it off.
        if ((lightLevel < 50.0)&&(isFlashOn==false)) {
            cameraSwitch(true);
            telemetry.addData("3 Flash", "ON");
        } else if ((lightLevel > 80.0)&& (isFlashOn)){
            cameraSwitch(false);
            telemetry.addData("3 Flash", "OFF");
        }
    }

    /*
    * Code to run when the op mode is first disabled goes here
    * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
    */
    @Override
    public void stop() {
        mSensorManager.unregisterListener(this);
        cameraSwitch(false);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not sure if needed, placeholder just in case
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            mLight = event.values;
        }
        if (mLight != null) {
            lightLevel = mLight[0]; // only one value from this sensor
        }
    }
    public void cameraSwitch(boolean state){
        if (state) {
            p.setFlashMode(Parameters.FLASH_MODE_TORCH);
            camera.setParameters(p);
            camera.startPreview();
            isFlashOn = true;


        } else {

            p.setFlashMode(Parameters.FLASH_MODE_OFF);
            camera.setParameters(p);
            camera.stopPreview();
            isFlashOn = false;
        }


    }



}
