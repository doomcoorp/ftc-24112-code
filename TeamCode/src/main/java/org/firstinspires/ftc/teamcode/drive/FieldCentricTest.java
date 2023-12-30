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
    protected Servo drone_servo;
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
        drone_servo = hardwareMap.get(Servo.class, "drone_servo");
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
        left_arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right_arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        arm2_servo.setPosition(-1);
        hand_servo.setPosition(1);
        //reverse left arm, set left and right to brake
        left_arm.setDirection(DcMotor.Direction.REVERSE);
        left_arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        right_arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        // Set arm position
        isMainArmDirectionForward = 1;


        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio,
            // but only if at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;


            // Set wheel power
            leftFront.setPower(frontLeftPower);
            leftRear.setPower(backLeftPower);
            rightFront.setPower(frontRightPower);
            rightRear.setPower(backRightPower);

            //LOWER / RAISE BIG ARM
            if(gamepad1.left_trigger != 1) {
                left_arm.setDirection(DcMotor.Direction.REVERSE);
                right_arm.setDirection(DcMotor.Direction.FORWARD);
                left_arm.setPower(gamepad1.left_trigger);
                right_arm.setPower(gamepad1.left_trigger);
            }
            if(gamepad1.right_trigger != 0) {
                left_arm.setDirection(DcMotor.Direction.FORWARD);
                right_arm.setDirection(DcMotor.Direction.REVERSE);
                left_arm.setPower(gamepad1.right_trigger);
                right_arm.setPower(gamepad1.right_trigger);
            }

            //DRONE LAUNCH
            if (gamepad1.dpad_up) {
                drone_servo.setPosition(-1);
            }
            if (gamepad1.dpad_down) {
                drone_servo.setPosition(1);
            }



            // OPEN/CLOSE CLAW
            if (gamepad1.right_bumper) {
                    hand_servo.setPosition(-1);
                }

            if (gamepad1.left_bumper) {
                hand_servo.setPosition(1);
            }


            // LOWER ARM 2 / RAISE
            if (gamepad1.x) {
                arm2_servo.setPosition(1);
            }
            if (gamepad1.b) {
                arm2_servo.setPosition(-1);
            }


            telemetry.addData("Left front Power", leftFront.getPower());
            telemetry.addData("Left back Power", leftRear.getPower());
            telemetry.addData("Right back power", rightRear.getPower());
            telemetry.addData("right front power", rightFront.getPower());
            telemetry.addData("Left arm power", left_arm.getPower());
            telemetry.addData("Right arm power", right_arm.getPower());
            telemetry.addData("LeftArm pos", left_arm.getCurrentPosition());
            telemetry.addData("Right Arm pos ", right_arm.getCurrentPosition());
            telemetry.addData("arm2 servo direction", arm2_servo.getDirection());
            telemetry.addData("arm2 servo position ", arm2_servo.getPosition());
            telemetry.addData("hand servo direction", hand_servo.getDirection());
            telemetry.addData("hand servo position", hand_servo.getPosition());
            telemetry.addData("isMainArmForward", isMainArmDirectionForward);

            telemetry.addData("larm target pos", left_arm.getTargetPosition());
            telemetry.addData("rarm target pos", right_arm.getTargetPosition());

            telemetry.update();



        }
    }
}

