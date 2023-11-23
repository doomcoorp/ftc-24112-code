package org.firstinspires.ftc.teamcode.drive.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import org.firstinspires.ftc.teamcode.vision.ColourMassDetectionProcessor;
import org.firstinspires.ftc.vision.VisionPortal;
import org.opencv.core.Scalar;

import java.util.concurrent.TimeUnit;



@Autonomous(name="OpenCVAutonomousBlue", group="Robot")
public class OpenCVAutonomousBlue extends Andytest1 {
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
        double minArea = 1000;
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

        while (!isStarted()) {
            if (colourMassDetectionProcessor.getLargestContourX() != -1 || colourMassDetectionProcessor.getLargestContourY() != -1) {
                recordedPropPosition = colourMassDetectionProcessor.getRecordedPropPosition();
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
        waitForStart();

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
                dropYellow = false; // start from row 2
                turnClockWise = -1; // Team Prop on left
                break;
            case MIDDLE:
                telemetry.addLine("Position on CENTER");
                telemetry.update();
                break;
            case RIGHT:
                telemetry.addLine("Position on right");
                telemetry.update();
                dropYellow = false; // start from row 2
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

