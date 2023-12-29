package org.firstinspires.ftc.teamcode.drive;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name="Field centric")
public class FieldCentricTest extends LinearOpMode {
    protected DcMotor left_arm;
    protected DcMotor right_arm;
    protected Servo arm2_servo;
    protected Servo hand_servo;
    protected ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() throws InterruptedException {
        int isMainArmDirectionForward;
        // Declare our motors and servos
        DcMotor leftFront = hardwareMap.dcMotor.get("leftFront");
        DcMotor leftRear = hardwareMap.dcMotor.get("leftRear");
        DcMotor rightFront = hardwareMap.dcMotor.get("rightFront");
        DcMotor rightRear = hardwareMap.dcMotor.get("rightRear");

        DcMotor right_arm = hardwareMap.dcMotor.get("right_arm");
        DcMotor left_arm = hardwareMap.dcMotor.get("left_arm");

        arm2_servo = hardwareMap.get(Servo.class, "arm2_servo");
        hand_servo = hardwareMap.get(Servo.class, "hand_servo");
        // Reverse the right side motors. This may be wrong for your setup.
        // If your robot moves backwards when commanded to go forwards,
        // reverse the left side instead.
        // See the note about this earlier on this page.
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightRear.setDirection(DcMotorSimple.Direction.REVERSE);
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);

        //Set servo position
        left_arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right_arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        arm2_servo.setPosition(-1);
        hand_servo.setPosition(1);
        //reverse left arm, set left and right to brake
        left_arm.setDirection(DcMotor.Direction.REVERSE);
        left_arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        right_arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        // Set arm position
        isMainArmDirectionForward = 1;

        // Retrieve the IMU from the hardware map
        IMU imu = hardwareMap.get(IMU.class, "imu");
        // Adjust the orientation parameters to match your robot
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.RIGHT));
        // Without this, the REV Hub's orientation is assumed to be logo up / USB forward
        imu.initialize(parameters);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x;

            // This button choice was made so that it is hard to hit on accident,
            // it can be freely changed based on preference.
            // The equivalent button is start on Xbox-style controllers.
            if (gamepad1.options) {
                imu.resetYaw();
            }

            double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

            // rotate the movement direction counter to counter rotation
            double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
            double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);
            //set direciton
            if (gamepad1.y) {
                if (isMainArmDirectionForward == 1) {
                    left_arm.setDirection(DcMotor.Direction.REVERSE);
                    right_arm.setDirection(DcMotor.Direction.FORWARD);
                    isMainArmDirectionForward = 0;
                } else if (isMainArmDirectionForward == 0) {
                    left_arm.setDirection(DcMotor.Direction.FORWARD);
                    right_arm.setDirection(DcMotor.Direction.REVERSE);
                    isMainArmDirectionForward = 1;
                }
            }
            // pick up pixel w A
            if (gamepad1.a) {
                arm2_servo.setPosition(0.75);
                while (arm2_servo.getPosition() == 0.75) {
                    // arm 2 to floor
                    hand_servo.setPosition(-1);
                    // wait to reach to floor
                    if (gamepad1.dpad_left) {
                        if (hand_servo.getPosition() == -1) {
                            hand_servo.setPosition(1);
                            arm2_servo.setPosition(-1);
                        }
                    }
                }
            }
            // close / open hand w x
            if (gamepad1.x && hand_servo.getPosition() == 1) {
                hand_servo.setPosition(-1);
            } else if (gamepad1.x && hand_servo.getPosition() == -1) {
                hand_servo.setPosition(1);
            }

            //Drop on backdrop
            if (gamepad1.left_bumper) {
                arm2_servo.setPosition(0.5);
                while (arm2_servo.getPosition() == 0.5) {
                    if (gamepad1.right_bumper) {
                        hand_servo.setPosition(-1);
                        sleep(600);
                        arm2_servo.setPosition(-1);
                    }
                }
            }

            if (gamepad1.b) {
                if (isMainArmDirectionForward == 1) {
                    left_arm.setDirection(DcMotor.Direction.REVERSE);
                    right_arm.setDirection(DcMotor.Direction.FORWARD);
                    left_arm.setTargetPosition(-360);
                    right_arm.setTargetPosition(-360);
                    left_arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    right_arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    left_arm.setPower(0.6);
                    right_arm.setPower(0.6);
                    while (opModeIsActive() && left_arm.isBusy()) {
                    }
                    left_arm.setPower(0);
                    right_arm.setPower(0);
                    sleep(600);
                    left_arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    right_arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

                } else {
                    left_arm.setDirection(DcMotor.Direction.FORWARD);
                    right_arm.setDirection(DcMotor.Direction.REVERSE);
                    left_arm.setTargetPosition(0);
                    right_arm.setTargetPosition(0);
                    left_arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    right_arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    left_arm.setPower(0.6);
                    right_arm.setPower(0.6);
                    while (opModeIsActive() && left_arm.isBusy()) {
                    }
                    left_arm.setPower(0);
                    right_arm.setPower(0);
                    sleep(600);
                    left_arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    right_arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                }
            }
            telemetry.addData("Left front Power", leftFront.getPower());
            telemetry.addData("Left back Power", leftRear.getPower());
            telemetry.addData("Right back power", rightRear.getPower());
            telemetry.addData("right front power", rightFront.getPower());
            telemetry.addData("Left arm power", left_arm.getPower());
            telemetry.addData("Right arm power", right_arm.getPower());
            telemetry.addData("LeftArm", left_arm.getCurrentPosition());
            telemetry.addData("Right Arm", right_arm.getCurrentPosition());
            telemetry.addData("arm2 servo direction", arm2_servo.getDirection());
            telemetry.addData("arm2 servo position ", arm2_servo.getPosition());
            telemetry.addData("hand servo direction", hand_servo.getDirection());
            telemetry.addData("hand servo position", hand_servo.getPosition());
            telemetry.addData("isMainArmForward", isMainArmDirectionForward);
            telemetry.addData("larm current pos", left_arm.getCurrentPosition());
            telemetry.addData("rarm current pos", right_arm.getCurrentPosition());

            telemetry.addData("larm target pos", left_arm.getTargetPosition());
            telemetry.addData("rarm target pos", right_arm.getTargetPosition());

            telemetry.update();


            rotX = rotX * 1.1;  // this thing is here to counteract imperfect strafing

            // basically math to make sure all them have the same power
            double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
            double frontLeftPower = (rotY + rotX + rx) / denominator;
            double backLeftPower = (rotY - rotX + rx) / denominator;
            double frontRightPower = (rotY - rotX - rx) / denominator;
            double backRightPower = (rotY + rotX - rx) / denominator;

            leftFront.setPower(frontLeftPower);
            leftRear.setPower(backLeftPower);
            rightFront.setPower(frontRightPower);
            rightRear.setPower(backRightPower);
        }
    }
}

