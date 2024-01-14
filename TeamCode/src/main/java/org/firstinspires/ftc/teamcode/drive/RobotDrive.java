package org.firstinspires.ftc.teamcode.drive;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;



// most of the major changes that i did were that you have to manually close/ open claw, and manually lower/raise small arm and big arm
// that way you can move around after doing whatever
// so if your not aligned perfectly horzontially/vertically you can still move around and correct your course so you dont have to reset whatever and try it again
// i can change it back later if driver wants

@TeleOp(name="Robot Drive")
public class RobotDrive extends LinearOpMode {
    protected DcMotor left_arm;
    protected DcMotor right_arm;
    protected Servo arm2_servo;
    protected Servo hand_servo;
    protected Servo drone_servo;
    protected Servo dronestop_servo;
    protected ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() throws InterruptedException {
        //declare ismainforward variable. it still does the same thing as in the past
        int isMainArmDirectionForward;
        // Declare motors and servos
        DcMotor leftFront = hardwareMap.dcMotor.get("leftFront");
        DcMotor leftRear = hardwareMap.dcMotor.get("leftRear");
        DcMotor rightFront = hardwareMap.dcMotor.get("rightFront");
        DcMotor rightRear = hardwareMap.dcMotor.get("rightRear");

        DcMotor right_arm = hardwareMap.dcMotor.get("right_arm");
        DcMotor left_arm = hardwareMap.dcMotor.get("left_arm");

        arm2_servo = hardwareMap.get(Servo.class, "arm2_servo");
        hand_servo = hardwareMap.get(Servo.class, "hand_servo");
        drone_servo = hardwareMap.get(Servo.class, "drone_servo");
        dronestop_servo = hardwareMap.get(Servo.class, "dronestop");

        // Reverse motors - this works our motors are kind of weird, you shouldn't worry about it. moving works fine now, it didn't before
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightRear.setDirection(DcMotorSimple.Direction.REVERSE);
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);

        //Set servo position, reset arm encoders
        left_arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right_arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left_arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right_arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        arm2_servo.setDirection(Servo.Direction.FORWARD);
        arm2_servo.setPosition(1);
        hand_servo.setPosition(0.2);

        //Reverse the left arm motor, set zero power behavior to brake
        left_arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        right_arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        // Set arm position
        isMainArmDirectionForward = 1;
        left_arm.setDirection(DcMotor.Direction.REVERSE);
        right_arm.setDirection(DcMotor.Direction.FORWARD);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;

            // "Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio,
            // but only if at least one is out of the range [-1, 1]"
            // - from gm0
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;


            // Set all wheels power
            leftFront.setPower(frontLeftPower);
            leftRear.setPower(backLeftPower);
            rightFront.setPower(frontRightPower);
            rightRear.setPower(backRightPower);

            // Set arm power
            // this used to not work, but now it does. I dont know why
            left_arm.setPower(gamepad1.left_trigger);
            right_arm.setPower(gamepad1.left_trigger);

            // Change to lowering / raising
            if (gamepad1.y) {
                left_arm.setDirection(DcMotor.Direction.REVERSE);
                right_arm.setDirection(DcMotor.Direction.FORWARD);
                isMainArmDirectionForward = 0;
            }
            if (gamepad1.a) {
                left_arm.setDirection(DcMotor.Direction.FORWARD);
                right_arm.setDirection(DcMotor.Direction.REVERSE);
                isMainArmDirectionForward = 1;
            }

                // you can ignore this, this kind of was a test to see if we could put stuff onto the backdrop from the front
            if (gamepad1.dpad_right) {
                    arm2_servo.setPosition(0.8);
            }

            //DRONE LAUNCH
            if (gamepad1.dpad_up) {
                // issue 14: raise the drone stopper
                dronestop_servo.setPosition(-1);
                // release the drone
                drone_servo.setPosition(-1);
                sleep(600);
                // issue 15: Make sure drone servo resets after shooting #15
                drone_servo.setPosition(1);
            }
            // resets drone position, otherwise it wont move
            if (gamepad1.dpad_down) {
                drone_servo.setPosition(1);
                dronestop_servo.setPosition(1);
            }

            // OPEN/CLOSE CLAW
            if (gamepad1.right_bumper) {
                    hand_servo.setPosition(0.2);
                }

            if (gamepad1.left_bumper) {
                hand_servo.setPosition(0.5);
            }


            // LOWER/RAISE SMALL ARM
            if (gamepad1.x) {
                arm2_servo.setPosition(1);
            }
            if (gamepad1.b) {
                arm2_servo.setPosition(0);
            }
            /* temporarily remove this piece of code, it's too unreliable
            // another big arm test, copied from old code. both going up and down work
            if (gamepad1.a) {
                if(isMainArmDirectionForward == 1) {
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
                    isMainArmDirectionForward = 0;
                }
                else {
                    left_arm.setDirection(DcMotor.Direction.FORWARD);
                    right_arm.setDirection(DcMotor.Direction.REVERSE);
                    left_arm.setTargetPosition(0);
                    right_arm.setTargetPosition(0);
                    left_arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    right_arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    left_arm.setPower(0.75);
                    right_arm.setPower(0.75);
                    while (opModeIsActive() && left_arm.isBusy()) {
                    }
                    left_arm.setPower(0);
                    right_arm.setPower(0);
                    sleep(600);
                    left_arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    right_arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    isMainArmDirectionForward = 1;
                }
            }
            */

            //telemetry
            telemetry.addData("Left front Power", leftFront.getPower());
            telemetry.addData("Left back Power", leftRear.getPower());
            telemetry.addData("Right back power", rightRear.getPower());
            telemetry.addData("right front power", rightFront.getPower());


            telemetry.addData("LeftArm pos", left_arm.getCurrentPosition());
            telemetry.addData("Right Arm pos ", right_arm.getCurrentPosition());

            telemetry.addData("arm2 servo direction", arm2_servo.getDirection());
            telemetry.addData("arm2 servo position ", arm2_servo.getPosition());

            telemetry.addData("hand servo direction", hand_servo.getDirection());
            telemetry.addData("hand servo position", hand_servo.getPosition());

            telemetry.addData("isMainArmForward", isMainArmDirectionForward);

            telemetry.addData("Left arm power", left_arm.getPower());
            telemetry.addData("Right arm power", right_arm.getPower());
            telemetry.addData("larm target pos", left_arm.getTargetPosition());
            telemetry.addData("rarm target pos", right_arm.getTargetPosition());

            telemetry.addData("larm direction", left_arm.getDirection());
            telemetry.addData("rarm direction", right_arm.getDirection());

            telemetry.addData("arm2", arm2_servo.getDirection());


            telemetry.update();



        }
    }
}

