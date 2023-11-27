package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import org.firstinspires.ftc.teamcode.vision.ColourMassDetectionProcessor;
import org.firstinspires.ftc.vision.VisionPortal;
import org.opencv.core.Scalar;

import java.util.concurrent.TimeUnit;



@Autonomous(name="Red Row 4", group="Robot")
public class OCVAutoF4 extends Tigertest1 {
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
        getCameraSetting();
        myExposure = 30;
        myGain = 2;
        setManualExposure(myExposure, myGain);
        Scalar lower = new Scalar(150, 100, 100);
        Scalar upper = new Scalar(180, 255, 255);
        double minArea = 3000;
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

        while (!isStarted() && cDetection < 30000) {
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

            getCameraSetting();
            telemetry.addData("Exposure value", myExposure);
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
    private boolean    setManualExposure(int exposureMS, int gain) {

        if (visionPortal == null) {
            return false;
        }


        if (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
            telemetry.addData("Camera", "Waiting");
            telemetry.update();
            while (!isStopRequested() && (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING)) {
                sleep(20);
            }
            telemetry.addData("Camera", "Ready");
            telemetry.update();
        }


        if (!isStopRequested())
        {

            ExposureControl exposureControl = visionPortal.getCameraControl(ExposureControl.class);
            if (exposureControl.getMode() != ExposureControl.Mode.Manual) {
                exposureControl.setMode(ExposureControl.Mode.Manual);
                sleep(50);
            }
            exposureControl.setExposure((long)exposureMS, TimeUnit.MILLISECONDS);
            sleep(20);


            GainControl gainControl = visionPortal.getCameraControl(GainControl.class);
            gainControl.setGain(gain);
            sleep(20);
            return (true);
        } else {
            return (false);
        }
    }


    private void getCameraSetting() {

        if (visionPortal == null) {
            return;
        }


        if (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
            telemetry.addData("Camera", "Waiting");
            telemetry.update();
            while (!isStopRequested() && (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING)) {
                sleep(20);
            }
            telemetry.addData("Camera", "Ready");
            telemetry.update();
        }


        if (!isStopRequested()) {
            ExposureControl exposureControl = visionPortal.getCameraControl(ExposureControl.class);
            minExposure = (int) exposureControl.getMinExposure(TimeUnit.MILLISECONDS) + 1;
            maxExposure = (int) exposureControl.getMaxExposure(TimeUnit.MILLISECONDS);

            GainControl gainControl = visionPortal.getCameraControl(GainControl.class);
            minGain = gainControl.getMinGain();
            maxGain = gainControl.getMaxGain();
        }
    }
}

