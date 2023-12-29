package org.firstinspires.ftc.teamcode.drive;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

    @TeleOp(name = "drive (Blocks to Java)")
    public class drive extends LinearOpMode {

        private DcMotor left_drive;
        private DcMotor left_arm;
        private Servo arm2_servo;
        private DcMotor right_arm;
        private DcMotor right_drive;
        private Servo hand_servo;
        private Servo droneservo;

        /**
         * This function is executed when this OpMode is selected from the Driver Station.
         */
        @Override
        public void runOpMode() {
            double powerTrainPower;
            int isMainArmDirectionForward;

            left_drive = hardwareMap.get(DcMotor.class, "left_drive");
            left_arm = hardwareMap.get(DcMotor.class, "left_arm");
            arm2_servo = hardwareMap.get(Servo.class, "arm2_servo");
            right_arm = hardwareMap.get(DcMotor.class, "right_arm");
            right_drive = hardwareMap.get(DcMotor.class, "right_drive");
            hand_servo = hardwareMap.get(Servo.class, "hand_servo");
            droneservo = hardwareMap.get(Servo.class, "drone servo");

            // Reverse one of the drive motors.
            // You will have to determine which motor to reverse for your robot.
            // In this example, the right motor was reversed so that positive
            // applied power makes it move the robot in the forward direction.
            left_drive.setDirection(DcMotor.Direction.REVERSE);
            // Reverse one of the main arm motors
            left_arm.setDirection(DcMotor.Direction.REVERSE);
            // Start with the hand open and small arm retracted
            arm2_servo.setPosition(1);
            // Set the zeropowerbehavior of the main arm motors to brake and stay in place, otherwise the weight of the whole arm will make it continue to move
            left_arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            right_arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            // Set isMainArmDirectionForward variable to 1. This means that it starts in a forward direction. The program will check and change it as needed
            isMainArmDirectionForward = 1;
            powerTrainPower = 0.7;
            waitForStart();
            while (opModeIsActive()) {
                // Put loop blocks here.
                // Set power of drive motors based on gamepad sticks.
                // The Y axis of a joystick ranges from -1 in its topmost position to +1 in its bottommost position.
                // We negate this value so that the topmost position corresponds to maximum forward power.
                left_drive.setPower(-(powerTrainPower * gamepad1.left_stick_y));
                right_drive.setPower(-(powerTrainPower * gamepad1.right_stick_y));
                if (gamepad1.dpad_up) {
                    left_drive.setPower(0.4);
                    right_drive.setPower(0.4);
                }
                if (gamepad1.dpad_down) {
                    left_drive.setPower(-0.4);
                    right_drive.setPower(-0.4);
                }
                // Set power of both main arms motors based on gamepad left trigger.
                // The Y axis of a joystick ranges from -1 in its topmost position to +1 in its bottommost position.
                // We negate this value so that the topmost position corresponds to maximum forward power.
                left_arm.setPower(gamepad1.left_trigger);
                right_arm.setPower(gamepad1.left_trigger);
                // Use the bumper buttons to reverse direction of main arm motors.
                if (gamepad1.left_bumper) {
                    // Raising direction
                    left_arm.setDirection(DcMotor.Direction.REVERSE);
                    right_arm.setDirection(DcMotor.Direction.FORWARD);
                    isMainArmDirectionForward = 1;
                }
                if (gamepad1.right_bumper) {
                    // Lowering direction
                    left_arm.setDirection(DcMotor.Direction.FORWARD);
                    right_arm.setDirection(DcMotor.Direction.REVERSE);
                    isMainArmDirectionForward = 0;
                }
                // If you push the A button lower the small arm and pick up pixel
                if (gamepad1.a) {
                    // Open the hand first, then lower the small arm
                    hand_servo.setPosition(-1);
                    arm2_servo.setPosition(-0.5);
                    // Allow some time to let the second arm lower in position then grab the pixel and finally raise the small arm again
                    sleep(800);
                    hand_servo.setPosition(1);
                    sleep(600);
                    arm2_servo.setPosition(1);
                }
                // If you push the B button raise the small arm
                if (gamepad1.b) {
                    if (isMainArmDirectionForward == 1) {
                        left_arm.setDirection(DcMotor.Direction.FORWARD);
                        right_arm.setDirection(DcMotor.Direction.REVERSE);
                        isMainArmDirectionForward = 0;
                    }
                    left_arm.setTargetPosition(480);
                    right_arm.setTargetPosition(480);
                    left_arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    right_arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    left_arm.setPower(0.6);
                    right_arm.setPower(0.6);
                    while (opModeIsActive() && left_arm.isBusy()) {
                    }
                    sleep(600);
                    hand_servo.setPosition(-1);
                    left_arm.setPower(0);
                    right_arm.setPower(0);
                    left_arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    right_arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                }
                // If you push the X button open the hand
                if (gamepad1.x) {
                    hand_servo.setPosition(-1);
                }
                // If you push the Y button close the hand
                if (gamepad1.y) {
                    hand_servo.setPosition(1);
                }
                // Drone servo controls
                if (gamepad1.dpad_left) {
                    droneservo.setPosition(1);
                }
                if (gamepad1.dpad_right) {
                    droneservo.setPosition(-1);
                    sleep(1000);
                    droneservo.setPosition(1);
                }
                telemetry.addData("Left Pow", left_drive.getPower());
                telemetry.addData("Right Pow", right_drive.getPower());
                telemetry.addData("Left arm power", left_arm.getPower());
                telemetry.addData("Right arm power", right_arm.getPower());
                telemetry.addData("LeftArm", left_arm.getCurrentPosition());
                telemetry.addData("Right Arm", right_arm.getCurrentPosition());
                telemetry.addData("arm2 servo direction", arm2_servo.getDirection());
                telemetry.addData("arm2 servo position ", arm2_servo.getPosition());
                telemetry.addData("hand servo direction", hand_servo.getDirection());
                telemetry.addData("hand servo position", hand_servo.getPosition());
                telemetry.addData("isMainArmForward", isMainArmDirectionForward);
                telemetry.update();
            }
        }
    }

