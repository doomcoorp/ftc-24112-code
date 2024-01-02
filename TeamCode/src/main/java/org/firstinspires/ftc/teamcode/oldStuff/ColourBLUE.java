package org.firstinspires.ftc.teamcode.oldStuff;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.teamcode.oldStuff.ColourMassDetectionProcessor;
import org.firstinspires.ftc.vision.VisionPortal;
import org.opencv.core.Scalar;
@Disabled
@Autonomous(name = "BLUEOPENCV", group = "Robot")
public class ColourBLUE extends OpMode {
    private VisionPortal visionPortal;
    private ColourMassDetectionProcessor colourMassDetectionProcessor;
    private final ExposureControl.Mode exposureMode = ExposureControl.Mode.AperturePriority;
    private final long exposureMs = 1;


    @Override
    public void init() {

        Scalar lower = new Scalar(90, 100, 100);
        Scalar upper = new Scalar(130, 255, 255);
        double minArea = 4000;


        colourMassDetectionProcessor = new ColourMassDetectionProcessor(
                lower,
                upper,
                () -> minArea,
                () -> 213,
                () -> 426
        );
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
        telemetry.addData("Currently Detected Mass Area", colourMassDetectionProcessor.getLargestContourArea());
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
