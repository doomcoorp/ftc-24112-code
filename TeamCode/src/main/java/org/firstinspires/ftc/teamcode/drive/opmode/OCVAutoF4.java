package org.firstinspires.ftc.teamcode.drive.opmode;

import static org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl.Mode.AperturePriority;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.*;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import org.firstinspires.ftc.teamcode.vision.ColourMassDetectionProcessor;
import org.firstinspires.ftc.vision.VisionPortal;
import org.opencv.core.Scalar;

import java.util.concurrent.TimeUnit;



@Autonomous(name="OCVAutoF4", group="Robot")
public class OCVAutoF4 extends Andytest1 {
    private int     myExposure  ;
    private int     minExposure ;
    private int     maxExposure ;
    private int     myGain      ;
    private int     minGain ;
    private int     maxGain ;

    private boolean isRed;
    private VisionPortal visionPortal;
    private ColourMassDetectionProcessor colourMassDetectionProcessor;
    @Override
    public void runOpMode() {
        Scalar lower = new Scalar(150, 100, 100);
        Scalar upper = new Scalar(180, 255, 255);
        double minArea = 1000;
        int cDetection = 0;

        ColourMassDetectionProcessor.PropPositions recordedPropPosition = ColourMassDetectionProcessor.PropPositions.RIGHT;

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

        while (!isStarted() && cDetection < 300000) {
            cDetection++;
            if (colourMassDetectionProcessor.getLargestContourX() != -1 && colourMassDetectionProcessor.getLargestContourY() != -1) {
                recordedPropPosition = colourMassDetectionProcessor.getRecordedPropPosition();
                if (recordedPropPosition == ColourMassDetectionProcessor.PropPositions.RIGHT) {
                    recordedPropPosition = ColourMassDetectionProcessor.PropPositions.MIDDLE;
                }
                telemetry.addData("position detected", cDetection);
                telemetry.addData("Currently Recorded Position", recordedPropPosition);
                telemetry.addData("Camera State", visionPortal.getCameraState());
                telemetry.addData("Currently Detected Mass Center", "x: " + colourMassDetectionProcessor.getLargestContourX() + ", y: " + colourMassDetectionProcessor.getLargestContourY());
                telemetry.addData("Currently Detected Mass Area", colourMassDetectionProcessor.getLargestContourArea());
            } else {
                telemetry.addLine("Position Currently Unfound");
                telemetry.addData("Camera State", visionPortal.getCameraState());
                telemetry.addData("Currently Detected Mass Center", "x: " + colourMassDetectionProcessor.getLargestContourX() + ", y: " + colourMassDetectionProcessor.getLargestContourY());
                telemetry.addData("Currently Detected Mass Area", colourMassDetectionProcessor.getLargestContourArea());
                recordedPropPosition = ColourMassDetectionProcessor.PropPositions.RIGHT;
            }

            telemetry.update();
        }

        // tell red or blue team
        isRedField = 1; // on the red side (column F)

        // stop camera
        visionPortal.stopLiveView();
        visionPortal.stopStreaming();

        // run case based on prop position
        switch (recordedPropPosition) {
            case LEFT:
                telemetry.addLine("Position on LEFT");
                telemetry.update();
                dropYellow = true; // start from row 4
                turnClockWise = -1; // Team Prop on left
                break;
            case MIDDLE:
                telemetry.addLine("Position on CENTER");
                telemetry.update();
                dropYellow = true;
                turnClockWise = 0; // Team Prop on center
                break;
            case RIGHT:
                telemetry.addLine("Position on right");
                telemetry.update();
                dropYellow = true;
                turnClockWise = 1; // Team Prop on right
                break;
        }
        telemetry.update();

        super.runOpMode();
    }
}