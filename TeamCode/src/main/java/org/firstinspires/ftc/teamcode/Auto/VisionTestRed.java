package org.firstinspires.ftc.teamcode.Auto;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.vision.PropProcessor;
import org.firstinspires.ftc.vision.VisionPortal;
import org.opencv.core.Scalar;

@Autonomous(name = "Opencv", group = "Robot")
public class VisionTestRed extends OpMode {
    // declare objects
    private VisionPortal visionPortal;
    private PropProcessor propProcessor;


    @Override
    public void init() {
        // build la vision portal
        propProcessor = new PropProcessor();
        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .addProcessor(propProcessor)
                .build();
    }

    @Override
    public void init_loop() {


        telemetry.addData("Currently Recorded Position", propProcessor.getSelected());
        telemetry.addData("Left Saturation", propProcessor.getsatRectLeft());
        telemetry.addData("Right Saturation", propProcessor.getsatRectRight());
        telemetry.addData("Middle saturation", propProcessor.getsatRectMiddle());
        telemetry.update();
    }



    @Override
    public void start() {
        // stop camera stream whne u start
        if (visionPortal.getCameraState() == VisionPortal.CameraState.STREAMING) {
            visionPortal.stopLiveView();
            visionPortal.stopStreaming();
        }

    }


    @Override
    public void loop() {

    }


    @Override
    // close vision porrtal when you stop
public void stop() {
        visionPortal.close();
}
}
