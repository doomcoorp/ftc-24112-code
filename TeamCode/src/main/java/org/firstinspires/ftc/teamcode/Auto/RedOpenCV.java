package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;

import org.firstinspires.ftc.teamcode.vision.ColourMassDetectionProcessor;
import org.firstinspires.ftc.vision.VisionPortal;
import org.opencv.core.Scalar;

@Autonomous(name = "RedOpenCv", group = "Robot")
public class RedOpenCV extends OpMode {
    private VisionPortal visionPortal;
    private ColourMassDetectionProcessor colourMassDetectionProcessor;
    Scalar lowerycrcb = new Scalar(110, 130, 100);
    Scalar upperycrcb = new Scalar(140, 255, 255);
    double minArea = 2000;  
    public volatile boolean error = false;
    public volatile Exception debug;
    private int loopCounter = 0;
    private int pLoopCounter = 0;




    @Override
    public void init() {
        colourMassDetectionProcessor = new ColourMassDetectionProcessor(lowerycrcb, upperycrcb, () -> minArea, () -> 213, () -> 426);
        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .addProcessor(colourMassDetectionProcessor)
                .build();
    }

    @Override
    public void init_loop() {


        double MEGABOMBA = colourMassDetectionProcessor.getLargestContourX();
        telemetry.addData("Currently Recorded Position", colourMassDetectionProcessor.getRecordedPropPosition());
        telemetry.addData("Camera State", visionPortal.getCameraState());
        telemetry.addData("Currently Detected Mass Center", "x: " + colourMassDetectionProcessor.getLargestContourX() + ", y: " + colourMassDetectionProcessor.getLargestContourY());
        telemetry.addData("Area:", colourMassDetectionProcessor.getLargestContourArea());
    }



    @Override
    public void start() {

        if (visionPortal.getCameraState() == VisionPortal.CameraState.STREAMING) {
            visionPortal.stopLiveView();
            visionPortal.stopStreaming();
        }
        ColourMassDetectionProcessor.PropPositions recordedPropPosition = colourMassDetectionProcessor.getRecordedPropPosition();

        if (recordedPropPosition == ColourMassDetectionProcessor.PropPositions.UNFOUND) {
            recordedPropPosition = ColourMassDetectionProcessor.PropPositions.RIGHT;
        }
        switch (recordedPropPosition) {
            case LEFT:
                break;
            case MIDDLE:
                break;
            case RIGHT:
                break;
        }
    }


    @Override
    public void loop() {

    }


    @Override
public void stop() {

    colourMassDetectionProcessor.close();
    visionPortal.close();
}
}
