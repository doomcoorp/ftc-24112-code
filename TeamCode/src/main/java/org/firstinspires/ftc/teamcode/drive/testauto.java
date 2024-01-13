/*package org.firstinspires.ftc.teamcode.drive;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.drive.tuningOpmodes.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.vision.PropProcessor;
import org.firstinspires.ftc.vision.VisionPortal;*/

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


@Config
@Autonomous(group = "AWD",name = "redautopixelstack")
public class testauto extends LinearOpMode {
    private VisionPortal visionPortal;
    private PropProcessor propProcessor;
    protected Servo arm2_servo;
    protected Servo hand_servo;
    protected DcMotor left_arm;
    protected DcMotor right_arm;

    protected PropProcessor.Selected PropPosition;
    @Override
    public void runOpMode() throws InterruptedException {
        arm2_servo = hardwareMap.get(Servo .class, "arm2_servo");
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


        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        Pose2d startPose = new Pose2d(-36,-61 , Math.toRadians(270));

        drive.setPoseEstimate(startPose);

        TrajectorySequence MIDDLE = drive.trajectorySequenceBuilder(startPose)

                .turn(Math.toRadians(90))
                //wait for lower arm
                .waitSeconds(10999)
                //open claw distance required
                .back(1)
                .waitSeconds(0.5)
                .back(2)
                .waitSeconds(0.7)
                .back(3)
                .lineToLinearHeading(new Pose2d(-36,-58, Math.toRadians(180)))
                .lineToLinearHeading(new Pose2d(40,-58, Math.toRadians(180)))
                .splineToSplineHeading(new Pose2d(47, -35, Math.toRadians(180)), Math.toRadians(0))
                .waitSeconds(5)
                .lineToLinearHeading(new Pose2d(47,-61, Math.toRadians(180)))
                .lineToLinearHeading(new Pose2d(60,-61, Math.toRadians(180)))
                //.lineToLinearHeading(new Pose2d(47,-12, Math.toRadians(180)))
                //.lineToLinearHeading(new Pose2d(60,-12, Math.toRadians(180)))

                .waitSeconds(30)


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

                .waitSeconds(30)
                .build();

                waitForStart();

        if (isStopRequested()) return;

        while (!isStopRequested()) {
                    telemetry.addLine("Running trajectory MIDDLE");
                    telemetry.update();
                    drive.followTrajectorySequence(MIDDLE);
                    break;
            }
        }
    }
}
*/

