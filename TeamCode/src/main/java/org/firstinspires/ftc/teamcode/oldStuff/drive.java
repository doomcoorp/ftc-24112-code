package org.firstinspires.ftc.teamcode.drive;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

    @TeleOp(name = "drive (Blocks to Java)")
    public class drive extends LinearOpMode {

        private DcMotor left_arm;
        private DcMotor right_arm;


        /**
         * This function is executed when this OpMode is selected from the Driver Station.
         */
        @Override
        public void runOpMode() {
            double powerTrainPower;
            int isMainArmDirectionForward;

            left_arm = hardwareMap.get(DcMotor.class, "left_arm");
            right_arm = hardwareMap.get(DcMotor.class, "right_arm");

            // Reverse one of the drive motors.
            // You will have to determine which motor to reverse for your robot.
            // In this example, the right motor was reversed so that positive
            // applied power makes it move the robot in the forward direction.

            // Reverse one of the main arm motors
            left_arm.setDirection(DcMotor.Direction.REVERSE);
            // Start with the hand open and small arm retracted
            // Set the zeropowerbehavior of the main arm motors to brake and stay in place, otherwise the weight of the whole arm will make it continue to move
            left_arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            right_arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            // Set isMainArmDirectionForward variable to 1. This means that it starts in a forward direction. The program will check and change it as needed
            isMainArmDirectionForward = 1;
            waitForStart();
            while (opModeIsActive()) {
                // Put loop blocks here.
                // Set power of drive motors based on gamepad sticks.
                // The Y axis of a joystick ranges from -1 in its topmost position to +1 in its bottommost position.
                // We negate this value so that the topmost position corresponds to maximum forward power.

                left_arm.setPower(gamepad1.left_trigger/2);
                right_arm.setPower(gamepad1.left_trigger/2);
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
                    left_arm.setPower(0);
                    right_arm.setPower(0);
                    left_arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    right_arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                }
                // If you push the X button open the hand

                telemetry.addData("Left arm power", left_arm.getPower());
                telemetry.addData("Right arm power", right_arm.getPower());
                telemetry.addData("LeftArm", left_arm.getCurrentPosition());
                telemetry.addData("Right Arm", right_arm.getCurrentPosition());
                telemetry.addData("isMainArmForward", isMainArmDirectionForward);
                telemetry.update();
            }
        }
    }

