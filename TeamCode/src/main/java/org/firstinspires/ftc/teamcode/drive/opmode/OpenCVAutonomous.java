package org.firstinspires.ftc.teamcode.drive.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import org.firstinspires.ftc.teamcode.vision.ColourMassDetectionProcessor;
import org.firstinspires.ftc.vision.VisionPortal;
import org.opencv.core.Scalar;

import java.util.concurrent.TimeUnit;



@Autonomous(group = "drive")
public class OpenCVAutonomous extends LinearOpMode {
    private int     myExposure  ;
    private int     minExposure ;
    private int     maxExposure ;
    private int     myGain      ;
    private int     minGain ;
    private int     maxGain ;

    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;

    private DcMotor left_arm;
    private DcMotor right_arm;
    private Servo arm2_servo;
    private Servo hand_servo;

    private ElapsedTime runtime = new ElapsedTime();


    static final double COUNTS_PER_MOTOR_REV = 560;
    static final double DRIVE_GEAR_REDUCTION = 1.0;
    static final double WHEEL_DIAMETER_INCHES = 3.6;
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double DRIVE_SPEED = 0.4;
    static final double TURN_SPEED = 0.5;

    static final int isRedField = -1;
    private VisionPortal visionPortal;
    private ColourMassDetectionProcessor colourMassDetectionProcessor;


    @Override
    public void runOpMode() {
        getCameraSetting();
        myExposure = 25;
        myGain = 2;
        setManualExposure(myExposure, myGain);
        Scalar lower = new Scalar(150, 100, 100);
        Scalar upper = new Scalar(180, 255, 255);
        double minArea = 1500;


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


        leftDrive = hardwareMap.get(DcMotor.class, "left_drive");
        rightDrive = hardwareMap.get(DcMotor.class, "right_drive");

        right_arm = hardwareMap.get(DcMotor.class, "right_arm");
        left_arm = hardwareMap.get(DcMotor.class, "left_arm");
        hand_servo = hardwareMap.get(Servo.class, "hand_servo");
        arm2_servo = hardwareMap.get(Servo.class, "arm2_servo");


        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);

        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        telemetry.addData("Starting at", "%7d :%7d",
                leftDrive.getCurrentPosition(),
                rightDrive.getCurrentPosition());
        telemetry.update();


        preloadPixels();


        waitForStart();
        while (opModeIsActive() && !isStopRequested()) {
            double telemetryTest = colourMassDetectionProcessor.getLargestContourX();
            telemetry.addData("Currently Recorded Position", colourMassDetectionProcessor.getRecordedPropPosition());
            telemetry.addData("Camera State", visionPortal.getCameraState());
            telemetry.addData("Currently Detected Mass Center", "x: " + colourMassDetectionProcessor.getLargestContourX() + ", y: " + colourMassDetectionProcessor.getLargestContourY());
            telemetry.addLine(String.valueOf(telemetryTest));
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
                    encoderDrive(DRIVE_SPEED, 24, 24, 5.0);
                    encoderDrive(TURN_SPEED, isRedField * 2, isRedField * -2, 2.0);
                    placePurplePickYellowPixel();


                    encoderDrive(TURN_SPEED, isRedField * -6, isRedField * 6, 4.0);
                    encoderDrive(DRIVE_SPEED, -12, -12, 2.0);
                    encoderDrive(TURN_SPEED, isRedField * -12, isRedField * 12, 4.0);
                    encoderDrive(DRIVE_SPEED, -19, -19, 5.0);


                    encoderDrive(TURN_SPEED, isRedField * 6, isRedField * -6, 4.0);
                    encoderDrive(DRIVE_SPEED, -12, -12, 5.0);
                    placePixelOnBackdrop(1);


                    encoderDrive(TURN_SPEED, isRedField * 11, isRedField * -11, 4.0);
                    encoderDrive(DRIVE_SPEED, -20, -20, 5.0);
                    encoderDrive(TURN_SPEED, isRedField * -11, isRedField * 11, 4.0);
                    encoderDrive(DRIVE_SPEED, -15, -15, 5.0);


                    telemetry.addData("Path", "Complete");
                    telemetry.update();
                    sleep(1000);
                    break;
                case RIGHT:
                    break;
            }
        }
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
        public void preloadPixels() {
            hand_servo.setPosition(-1);
            arm2_servo.setPosition(-1);

            sleep(2000);
            hand_servo.setPosition(1);
            sleep(800);

            arm2_servo.setPosition(1);
        }

        
        public void placePurplePickYellowPixel () {

            arm2_servo.setPosition(-1);
            sleep(1500);


            hand_servo.setPosition(-1);
            sleep(800);


            encoderDrive(DRIVE_SPEED, -3.5, -3.5, 5.0);


            hand_servo.setPosition(1);
            sleep(800);
            arm2_servo.setPosition(1);
        }

        
        public void placePixelOnBackdrop ( int isMainArmDirectionForward){


            if (isMainArmDirectionForward == 1) {
                left_arm.setDirection(DcMotor.Direction.FORWARD);
                right_arm.setDirection(DcMotor.Direction.REVERSE);
                isMainArmDirectionForward = 0;
            }


            left_arm.setTargetPosition(530);
            right_arm.setTargetPosition(530);

            left_arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            right_arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            left_arm.setPower(0.3);
            right_arm.setPower(0.3);

            while (opModeIsActive() && left_arm.isBusy()) {
            }

            left_arm.setPower(0);
            right_arm.setPower(0);
            sleep(600);


            hand_servo.setPosition(-1);


            left_arm.setDirection(DcMotor.Direction.REVERSE);
            right_arm.setDirection(DcMotor.Direction.FORWARD);
            isMainArmDirectionForward = 1;
            left_arm.setTargetPosition(0);
            right_arm.setTargetPosition(0);
            left_arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            right_arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            left_arm.setPower(0.3);
            right_arm.setPower(0.3);
            while (opModeIsActive() && left_arm.isBusy()) {
            }
            left_arm.setPower(0);
            right_arm.setPower(0);


            left_arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            right_arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        
        public void encoderDrive ( double speed, double leftInches, double rightInches,
        double timeoutS){
            int newLeftTarget;
            int newRightTarget;


            if (opModeIsActive()) {


                newLeftTarget = leftDrive.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
                newRightTarget = rightDrive.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);
                leftDrive.setTargetPosition(newLeftTarget);
                rightDrive.setTargetPosition(newRightTarget);


                leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);


                runtime.reset();
                double absSpeed = Math.abs(speed);
                leftDrive.setPower(absSpeed);
                rightDrive.setPower(absSpeed);







                while (opModeIsActive() &&
                        (runtime.seconds() < timeoutS) &&
                        (leftDrive.isBusy() && rightDrive.isBusy())) {


                    telemetry.addData("Running to", " %7d :%7d", newLeftTarget, newRightTarget);
                    telemetry.addData("Currently at", " at %7d :%7d",
                            leftDrive.getCurrentPosition(), rightDrive.getCurrentPosition());
                    telemetry.update();
                }


                leftDrive.setPower(0);
                rightDrive.setPower(0);


                leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                sleep(250);
            }
        }

        
        public void moveRobot ( double x, double yaw){

            double leftPower = x - yaw;
            double rightPower = x + yaw;


            double max = Math.max(Math.abs(leftPower), Math.abs(rightPower));
            if (max > 1.0) {
                leftPower /= max;
                rightPower /= max;
            }


            leftDrive.setPower(leftPower);
            rightDrive.setPower(rightPower);
        }
            }

