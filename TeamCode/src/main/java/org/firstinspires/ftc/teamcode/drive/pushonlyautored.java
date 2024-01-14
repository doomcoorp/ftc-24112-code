package org.firstinspires.ftc.teamcode.drive;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.drive.tuningOpmodes.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.vision.PropProcessor;
import org.firstinspires.ftc.vision.VisionPortal;

/*
 * Op mode for preliminary tuning of the follower PID coefficients (located in the drive base
 * classes). The robot drives in a DISTANCE-by-DISTANCE square indefinitely. Utilization of the
 * dashboard is recommended for this tuning routine. To access the dashboard, connect your computer
 * to the RC's WiFi network. In your browser, navigate to https://192.168.49.1:8080/dash if you're
 * using the RC phone or https://192.168.43.1:8080/dash if you are using the Control Hub. Once
 * you've successfully connected, start the program, and your robot will begin driving in a square.
 * You should observe the target position (green) and your pose estimate (blue) and adjust your
 * follower PID coefficients such that you follow the target position as accurately as possible.
 * If you are using SampleMecanumDrive, you should be tuning TRANSLATIONAL_PID and HEADING_PID.
 * If you are using SampleTankDrive, you should be tuning AXIAL_PID, CROSS_TRACK_PID, and HEADING_PID.
 * These coefficients can be tuned live in dashboard.
 */

@Config
@Autonomous(group = "AWD",name = "redautonoturn")
public class pushonlyautored extends LinearOpMode {
    private VisionPortal visionPortal;
    private PropProcessor propProcessor;
    protected Servo arm2_servo;
    protected Servo hand_servo;
    protected DcMotor left_arm;
    protected DcMotor right_arm;
    private IMU imu;

    protected PropProcessor.Selected PropPosition;

    @Override
    public void runOpMode() throws InterruptedException {
        arm2_servo = hardwareMap.get(Servo.class, "arm2_servo");
        hand_servo = hardwareMap.get(Servo.class, "hand_servo");
        DcMotor right_arm = hardwareMap.dcMotor.get("right_arm");
        DcMotor left_arm = hardwareMap.dcMotor.get("left_arm");
        left_arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right_arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left_arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right_arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        left_arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        right_arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        left_arm.setDirection(DcMotor.Direction.REVERSE);
        right_arm.setDirection(DcMotor.Direction.FORWARD);

        IMU imu = hardwareMap.get(IMU.class, "imu");
        // Adjust the orientation parameters to match your robot
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.RIGHT));
        // Without this, the REV Hub's orientation is assumed to be logo up / USB forward
        imu.initialize(parameters);
        imu.resetYaw();


        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        Pose2d startPose = new Pose2d(12, -61, Math.toRadians(270));

        drive.setPoseEstimate(startPose);
        TrajectorySequence RIGHT = drive.trajectorySequenceBuilder(startPose)
                .strafeLeft(12)
                .lineToLinearHeading(new Pose2d(24,-20, Math.toRadians(270)))
                // move back
                //wait for lower arm
                .waitSeconds(1)
                //open claw distance required
                .back(2)
                .lineToLinearHeading(new Pose2d(24,-12, Math.toRadians(270)))
                .lineToLinearHeading(new Pose2d(38,-12, Math.toRadians(270)))

                .lineToLinearHeading(new Pose2d(38,-61, Math.toRadians(270)))
                .lineToLinearHeading(new Pose2d(61.5,-61, Math.toRadians(270)))


                .waitSeconds(30)
                // lower arm 2
                .addDisplacementMarker(52, () -> {
                    arm2_servo.setPosition(0);

                })
                // open claw
                .addDisplacementMarker(53, () -> {
                    hand_servo.setPosition(0.2);
                })
                .addDisplacementMarker(58, () -> {
                    arm2_servo.setPosition(1);
                    hand_servo.setPosition(0.5);
                })


                // lower arm 2

                .build();
        TrajectorySequence MIDDLE1 = drive.trajectorySequenceBuilder(startPose)
                .lineToLinearHeading(new Pose2d(12,-16, Math.toRadians(270)))

                // move back
                //wait for lower arm
                .waitSeconds(1)
                //open claw distance required
                .back(2)
                .lineToLinearHeading(new Pose2d(38,-10, Math.toRadians(270)))
                .lineToLinearHeading(new Pose2d(38,-61, Math.toRadians(270)))
                .lineToLinearHeading(new Pose2d(60,-61, Math.toRadians(270)))
                .waitSeconds(30)
                // lower arm 2
                // lower arm 2
                .addDisplacementMarker(44, () -> {
                    arm2_servo.setPosition(0);

                })
                // open claw
                .addDisplacementMarker(46, () -> {
                    hand_servo.setPosition(0.2);
                })
                // close claw, raise arm 2
                .addDisplacementMarker(50, () -> {
arm2_servo.setPosition(1);
hand_servo.setPosition(0.5);
                })
                // raise big arm to backdrop
                .build();

        /*Pose2d startPose2 = new Pose2d(-34, -32, Math.toRadians(90));
        TrajectorySequence MIDDLE2 = drive.trajectorySequenceBuilder(startPose2)
                .back(1)
                .waitSeconds(0.5)
                .back(2)
                .waitSeconds(0.7)
                .back(3)
                .build();

        Pose2d startPose3 = new Pose2d(-36, -41, Math.toRadians(180));
        TrajectorySequence MIDDLE3 = drive.trajectorySequenceBuilder(startPose3)
                .lineToLinearHeading(new Pose2d(-36, -58, Math.toRadians(180)))
                .lineToLinearHeading(new Pose2d(40, -58, Math.toRadians(180)))
                .splineToSplineHeading(new Pose2d(47, -35, Math.toRadians(180)), Math.toRadians(0))
                .waitSeconds(5)
                .lineToLinearHeading(new Pose2d(47, -61, Math.toRadians(180)))
                .lineToLinearHeading(new Pose2d(60, -61, Math.toRadians(180)))
                //.lineToLinearHeading(new Pose2d(47,-12, Math.toRadians(180)))
                //.lineToLinearHeading(new Pose2d(60,-12, Math.toRadians(180)))

                .waitSeconds(30)
                // lower arm 2
                /*.addDisplacementMarker(53, () -> {
                    arm2_servo.setPosition(0);


                })
                // open claw
                .addDisplacementMarker(56, () -> {
                    hand_servo.setPosition(0.2);

                })
                // close claw, raise arm 2
                .addDisplacementMarker(57,() -> {
                    hand_servo.setPosition(0.5);


                })
                .addDisplacementMarker(58, () -> {
                    arm2_servo.setPosition(1);


                })
                // raise big arm to backdrop
                /*addTemporalMarker(15, () -> {
                    left_arm.setDirection(DcMotor.Direction.REVERSE);
                    right_arm.setDirection(DcMotor.Direction.FORWARD);
                    left_arm.setTargetPosition(-500);
                    right_arm.setTargetPosition(-500);
                    left_arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    right_arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    left_arm.setPower(1);
                    right_arm.setPower(1);
                    while (opModeIsActive() && left_arm.isBusy()) {
                    }
                    left_arm.setPower(0);
                    right_arm.setPower(0);
                    left_arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    right_arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                })
                .addTemporalMarker(15.6, () -> {
                    arm2_servo.setPosition(0.4);


                })
                .addTemporalMarker(16, () -> {
                    hand_servo.setPosition(0.2);

                })
                // lower arm and claw
                .addTemporalMarker(16.5, () -> {
                    arm2_servo.setPosition(1);
                })
                .addTemporalMarker(16.6, () -> {
            left_arm.setDirection(DcMotor.Direction.FORWARD);
            right_arm.setDirection(DcMotor.Direction.REVERSE);
            left_arm.setTargetPosition(0);
            right_arm.setTargetPosition(0);
            left_arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            right_arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            left_arm.setPower(1);
            right_arm.setPower(1);
            while (opModeIsActive() && left_arm.isBusy()) {
            }
            left_arm.setPower(0);
            right_arm.setPower(0);
            left_arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            right_arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            arm2_servo.setPosition(1);
        })
                .waitSeconds(30)
                .build();*/

        TrajectorySequence LEFT = drive.trajectorySequenceBuilder(startPose)
                .strafeLeft(5)
                .lineToLinearHeading(new Pose2d(12, -36, Math.toRadians(180)))
                // move back
                //wait for lower arm
                .waitSeconds(2)
                //open claw distance required
                .back(1)
                .waitSeconds(0.5)
                .back(2)
                .waitSeconds(0.3)
                .back(3)
                .lineToLinearHeading(new Pose2d(51, -26, Math.toRadians(180)))
                .waitSeconds(4)
                .lineToLinearHeading(new Pose2d(51, -61, Math.toRadians(180)))
                .lineToLinearHeading(new Pose2d(60, -61, Math.toRadians(180)))
                .waitSeconds(30)


                // lower arm 2
                .addDisplacementMarker(30, () -> {
                    arm2_servo.setPosition(0);

                })
                // open claw
                .addDisplacementMarker(31, () -> {
                    hand_servo.setPosition(0.2);


                })
                // close claw, raise arm 2
                .addDisplacementMarker(32, () -> {
                    hand_servo.setPosition(0.5);

                })
                .addDisplacementMarker(34, () -> {
                    arm2_servo.setPosition(1);
                })
                .addTemporalMarker(9, () -> {
                    left_arm.setDirection(DcMotor.Direction.REVERSE);
                    right_arm.setDirection(DcMotor.Direction.FORWARD);
                    left_arm.setTargetPosition(-500);
                    right_arm.setTargetPosition(-500);
                    left_arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    right_arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    left_arm.setPower(1);
                    right_arm.setPower(1);
                    while (opModeIsActive() && left_arm.isBusy()) {
                    }
                    left_arm.setPower(0);
                    right_arm.setPower(0);
                    left_arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    right_arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                })
                .addTemporalMarker(9.6, () -> {
                    arm2_servo.setPosition(0.4);

                })
                .addTemporalMarker(10, () -> {
                    hand_servo.setPosition(0.2);

                })
                .addTemporalMarker(10.5, () -> {
                    arm2_servo.setPosition(1);
                })
                // lower arm and claw
                .addTemporalMarker(10.6, () -> {
                    left_arm.setDirection(DcMotor.Direction.FORWARD);
                    right_arm.setDirection(DcMotor.Direction.REVERSE);
                    left_arm.setTargetPosition(0);
                    right_arm.setTargetPosition(0);
                    left_arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    right_arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    left_arm.setPower(1);
                    right_arm.setPower(1);
                    while (opModeIsActive() && left_arm.isBusy()) {
                    }
                    left_arm.setPower(0);
                    right_arm.setPower(0);
                    left_arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    right_arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    arm2_servo.setPosition(1);
                })

                // raise big arm to backdrop
                .build();
        propProcessor = new PropProcessor();
        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .addProcessor(propProcessor)
                .build();

        while (!isStarted()) {
            if (gamepad1.right_bumper) {
                hand_servo.setPosition(0.2);
            }

            if (gamepad1.left_bumper) {
                hand_servo.setPosition(0.5);
            }
            telemetry.addData("arm pos", arm2_servo.getPosition());
            telemetry.addData("claw pos", hand_servo.getPosition());
            telemetry.addData("Currently Recorded Position", propProcessor.getSelected());
            telemetry.addData("Left Saturation", propProcessor.getsatRectLeft());
            telemetry.addData("Right Saturation", propProcessor.getsatRectRight());
            telemetry.addData("Middle saturation", propProcessor.getsatRectMiddle());
            telemetry.update();

        }
        waitForStart();

        if (isStopRequested()) return;

        while (!isStopRequested()) {
            telemetry.addData("larm direction", left_arm.getDirection());
            telemetry.addData("rarm direction", right_arm.getDirection());
            telemetry.addData("arm2 servo direction", arm2_servo.getDirection());
            telemetry.addData("arm2 servo position ", arm2_servo.getPosition());

            telemetry.addData("hand servo position", hand_servo.getPosition());
            telemetry.update();
            PropPosition = propProcessor.getSelected();
            visionPortal.stopLiveView();
            visionPortal.stopStreaming();
            switch (PropPosition) {

                case MIDDLE:
                    telemetry.addLine("Running trajectory MIDDLE");
                    telemetry.update();
                    drive.setPoseEstimate(startPose);
                    drive.followTrajectorySequence(MIDDLE1);
                    drive.turn(Math.toRadians(0));
                    sleep(20000);
                   /* drive.setPoseEstimate(startPose2);
                    drive.followTrajectorySequence(MIDDLE2);

                    sleep(20000);
                    drive.turn(Math.toRadians(-90.0));
                    drive.setPoseEstimate(startPose3);
                    drive.followTrajectorySequence(MIDDLE3);*/
                    break;
                case LEFT:
                    telemetry.addLine("Running trajectory LEFT");
                    drive.followTrajectorySequence(LEFT);
                    telemetry.update();
                    break;
                case RIGHT:
                    telemetry.addLine("Running trajectory RIGHT");
                    drive.followTrajectorySequence(RIGHT);
                    telemetry.update();
                    break;
            }
        }
    }
}
