package org.firstinspires.ftc.teamcode.Auto;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.vision.PropProcessor;
import org.firstinspires.ftc.vision.VisionPortal;
import org.opencv.core.Scalar;

@Autonomous(name = "WAJAIUWDAIUWDHGAWUIHDILUJDAW", group = "Robot")
public class VisionRedTest extends OpMode {
    private VisionPortal visionPortal;
    private PropProcessor propProcessor;


    @Override
    public void init() {
        propProcessor = new PropProcessor();
        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .addProcessor(propProcessor)
                .build();
    }

    @Override
    public void init_loop() {


        telemetry.addData("Currently Recorded Position", propProcessor.getSelected());
    }



    @Override
    public void start() {

        if (visionPortal.getCameraState() == VisionPortal.CameraState.STREAMING) {
            visionPortal.stopLiveView();
            visionPortal.stopStreaming();
        }

    }


    @Override
    public void loop() {

    }


    @Override
public void stop() {
        visionPortal.close();
}
}
