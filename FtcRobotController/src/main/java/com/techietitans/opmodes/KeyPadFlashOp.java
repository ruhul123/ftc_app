package com.techietitans.opmodes;


import android.hardware.Camera;
import android.hardware.Camera.Parameters;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ruhul on 6/28/15.
 */


/**
 *This sample OpMode demonstrates the use of gamepad to control the builtin flash light of
 *ZTE phone. For simplicity, the old Camera (instead of Camera2)API is used. It is deprecated..
 *still works.
 */
public class KeyPadFlashOp extends OpMode {

    private String startDate;
    private ElapsedTime runtime = new ElapsedTime();
    //Set boolean flag when torch is turned on/off
    private boolean isFlashOn = false;
    private boolean isBlinkerOn= false;
    //Create camera object to access flahslight
    private Camera camera;
    private Parameters p;

    /*
     * Constructor
     */
    public KeyPadFlashOp() {

    }

    /*
     * Code to run when the op mode is first enabled goes here
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */
    @Override
    public void init() {
        startDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
        runtime.reset();
        camera = Camera.open();
        p = camera.getParameters();

    }

    /*
     * This method will be called repeatedly in a loop
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
     */
    @Override
    public void loop() {
        telemetry.addData("1 Start", "FlashOp started at " + startDate);
        telemetry.addData("2 Status", "running for " + runtime.toString());


        if (gamepad1.a) {
            cameraSwitch(true);
            isBlinkerOn = false;

        }
        if (gamepad1.b) {
            cameraSwitch(false);
            isBlinkerOn = false;
        }

        if (gamepad1.y) {
            isBlinkerOn = true;
        }

        if (isBlinkerOn) {

            if (isFlashOn) {
                cameraSwitch(false);
            } else {
                cameraSwitch(true);
            }

        }



    }
    /*
     * Code to run when the op mode is first disabled goes here
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
     */
    @Override
    public void stop(){

        cameraSwitch(false);
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
