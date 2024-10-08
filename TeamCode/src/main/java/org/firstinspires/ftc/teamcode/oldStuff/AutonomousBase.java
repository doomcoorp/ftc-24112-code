/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import org.firstinspires.ftc.teamcode.oldStuff.ColourMassDetectionProcessor;
import org.firstinspires.ftc.vision.VisionPortal;
import org.opencv.core.Scalar;
import java.util.concurrent.TimeUnit;
/*
 * This OpMode illustrates the concept of driving a path based on encoder counts.
 * The code is structured as a LinearOpMode
 *
 * The code REQUIRES that you DO have encoders on the wheels,
 *   otherwise you would use: RobotAutoDriveByTime;
 *
 *  This code ALSO requires that the drive Motors have been configured such that a positive
 *  power command moves them forward, and causes the encoders to count UP.
 *
 *   The desired path in this example is:
 *   - Drive forward for 48 inches
 *   - Spin right for 12 Inches
 *   - Drive Backward for 24 inches
 *   - Stop and close the claw.
 *
 *  The code is written using a method called: encoderDrive(speed, leftInches, rightInches, timeoutS)
 *  that performs the actual movement.
 *  This method assumes that each movement is relative to the last stopping place.
 *  There are other ways to perform encoder based moves, but this method is probably the simplest.
 *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 */
@Disabled
@Autonomous(name="(Do not use) Autonomous Base", group="WAKAWKAWKAKAKAK")

public class AutonomousBase extends LinearOpMode {

    /* Declare OpMode members. */
    protected DcMotor         leftDrive   = null;
    protected DcMotor         rightDrive  = null;

    protected DcMotor left_arm;
    protected DcMotor right_arm;
    protected Servo arm2_servo;
    protected Servo hand_servo;


    protected ElapsedTime     runtime = new ElapsedTime();

    // Calculate the COUNTS_PER_INCH for your specific drive train.
    // Go to your motor vendor website to determine your motor's COUNTS_PER_MOTOR_REV
    // For external drive gearing, set DRIVE_GEAR_REDUCTION as needed.
    // For example, use a value of 2.0 for a 12-tooth spur gear driving a 24-tooth spur gear.
    // This is gearing DOWN for less speed and more torque.
    // For gearing UP, use a gear ratio less than 1.0. Note this will affect the direction of wheel rotation.
    static final double     COUNTS_PER_MOTOR_REV    = 560 ;     // https://docs.revrobotics.com/duo-control/sensors/encoders/motor-based-encoders //1440 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // No External Gearing.
    static final double     WHEEL_DIAMETER_INCHES   = 4.0; // 3.6 ;     //4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                                                      (WHEEL_DIAMETER_INCHES * 3.1415926);
    static final double     DRIVE_SPEED             = 0.3;
    static final double     TURN_SPEED              = 0.5; //0.5;

    protected int isRedField = 1; // this allows to mirror the mode for the blue field = -1
    protected int turnClockWise = 1;
    protected boolean dropYellow = false;

    private int     myExposure  ;
    private int     minExposure ;
    private int     maxExposure ;
    private int     myGain      ;
    private int     minGain ;
    private int     maxGain ;

    private boolean isRed;
    protected VisionPortal visionPortal;
    protected ColourMassDetectionProcessor colourMassDetectionProcessor;

    @Override
    public void runOpMode() {

        // Initialize the drive system variables.
        leftDrive  = hardwareMap.get(DcMotor.class, "left_drive");
        rightDrive = hardwareMap.get(DcMotor.class, "right_drive");

        right_arm = hardwareMap.get(DcMotor.class, "right_arm");
        left_arm = hardwareMap.get(DcMotor.class, "left_arm");
        hand_servo = hardwareMap.get(Servo.class, "hand_servo");
        arm2_servo = hardwareMap.get(Servo.class, "arm2_servo");


        // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
        // When run, this OpMode should start both motors driving forward. So adjust these two lines based on your first test drive.
        // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips
        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);

        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset


        // preload the yellow and purple pixels
        preloadPixels();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Step through each leg of the path,
        // Note: Reverse movement is obtained by setting a negative distance (not speed)

        // For Red:
        //     1 is F2 to right (clockwise).
        //     -1 is F2 to left (counterClockWise)
        //     0 is F2 at middle
        // For Blue:
        //     -1 is A2 to left (counterClockwise).
        //     1 is A2 to right (clockwise)
        //     0 is A2 at middle
        // for example: int turnClockWise = 0;

        // true if we are at A4 or F4
        // for example: boolean dropYellow = true;

        // For left or right,
        //     move forward 20 inches
        //     turn 45 degree with 8 inches
        //      move forward another 2 inches
        // For middle,
        //     move forward 20 inches
        //     turn 45 degree with 8 inches
        //      move forward another 2 inches

        // Start by detecting prop to determine where to go
        detectPropPosition();

        // Start moving
        int moveForwards = 18; // turn 90, forwards 8 inch, go back 8 inch,
        double turningInch = 8;
        int moveExtra = 2;
        int moveCenterClose = 2;
        boolean isCenter = (turnClockWise == 0);

        if (turnClockWise == 0)
        {
            moveForwards = 36; // 24; // Move two feet to reach spike mark
            turningInch = -12.6; // 90 degree turn
            moveExtra = -8; //6; // align the edge of the spike mark
            turnClockWise = 1 * isRedField; // turn clockWise
        }
        else if ((turnClockWise * isRedField == 1 && dropYellow)
            || (turnClockWise * isRedField == -1 && !dropYellow))// 28 inch, 90 degree, forward 8 inch, back 8 inch,
        {
            moveForwards += 13;
            moveExtra +=12;
        }

        double leftTurn = turningInch * turnClockWise;



        // go to spike mark
        encoderDrive(DRIVE_SPEED,  moveForwards,  moveForwards, 5.0);

        // turning
        if (!isCenter)
                encoderDrive(TURN_SPEED, leftTurn, leftTurn * -1, 5.0);

        // move forward a bit to get closer to edge of spike mark
        // for center position, align with edge of spike mark
        encoderDrive(DRIVE_SPEED,  moveExtra,  moveExtra, 5.0);

        if (isCenter) {
            //encoderDrive(TURN_SPEED, leftTurn * -1, leftTurn, 5.0);
            //encoderDrive(DRIVE_SPEED, moveCenterClose, moveCenterClose, 2.0);
           // encoderDrive(DRIVE_SPEED, moveCenterClose * -1, moveCenterClose * -1, 2.0);
                        // turning 90 degree back to face spike mark
            //encoderDrive(TURN_SPEED, leftTurn * -1, leftTurn, 5.0);
            //encoderDrive(DRIVE_SPEED, moveCenterClose, moveCenterClose, 2.0);
        }
        else if((turnClockWise * isRedField == 1 && dropYellow) ||
        (turnClockWise * isRedField == -1 && !dropYellow))
        {
            moveExtra -=2;
            encoderDrive(DRIVE_SPEED, moveExtra * -1, moveExtra * -1, 5.0);
        }

        // drop the purple and pick up yellow
        placePurplePickYellowPixel();

        // all the way back to original place
        if (isCenter && dropYellow) {
           // if (dropYellow)
            //    moveCenterClose += 1;
            //encoderDrive(DRIVE_SPEED, moveCenterClose * -1, moveCenterClose * -1, 2.0);
            encoderDrive(TURN_SPEED, leftTurn, leftTurn * -1, 5.0);
        }

        // back all the way to starting position
        if (dropYellow && !isCenter && turnClockWise * isRedField == -1)
            moveExtra +=2;


        if (!isCenter) {
            if(turnClockWise * isRedField == -1) {
                if (dropYellow) {
                    encoderDrive(DRIVE_SPEED, moveExtra * -1, moveExtra * -1, 2.0);
                    encoderDrive(TURN_SPEED, leftTurn * -1, leftTurn, 5.0);
                }
            }
            else if (!dropYellow)
            encoderDrive(TURN_SPEED, leftTurn * -1, leftTurn, 5.0);
        }

        if (!dropYellow && !isCenter && turnClockWise * isRedField == 1)
            encoderDrive(DRIVE_SPEED, moveForwards * -1, moveForwards * -1, 2.0);

        // in case we are in A4 or F4, let us go to backdrop
        if (dropYellow)
        {
            // move horitonal cooridate of the April Tag
            int alignApriTag = -2;
            int alignParking = 16;
            double rotate90 = 12.7 * isRedField;

            if (isRedField == -1)
                alignParking += 2;

            if (isCenter) {
                alignApriTag += 8; // distance from center to go to border
                alignParking += 14;
            }
            else {
                if (turnClockWise * isRedField == -1) {
                    // add extra length on horizontal coordinate
                    alignApriTag += 16;
                    alignParking+=16;

                    encoderDrive(DRIVE_SPEED, alignApriTag, alignApriTag, 3.0);

                    // Turn 90 degree
                    encoderDrive(TURN_SPEED, rotate90 * -1, rotate90, 2.0);
                }
                else
                {
                    alignApriTag -= 13;
                    alignParking+=6;

                    // Turn 90 degree
                    encoderDrive(TURN_SPEED, rotate90 * -1, rotate90, 2.0);
                    encoderDrive(DRIVE_SPEED, alignApriTag, alignApriTag, 3.0);
                    encoderDrive(TURN_SPEED, leftTurn * -1, leftTurn, 5.0);
                }
            }


            // go to one feet away from backdrop
            int goToBackDrop = -36;
            if (isCenter) {
                goToBackDrop -= moveExtra + 6;
            }
            else if (turnClockWise * isRedField == 1)
            {
                goToBackDrop += 10;
            }
            else {
                goToBackDrop = goToBackDrop - 2 * (turnClockWise * isRedField) + 1;
            }

            encoderDrive(DRIVE_SPEED, goToBackDrop, goToBackDrop, 5.0);

            // drop the yellow pixel
            placePixelOnBackdrop(1);

            // go to park
            encoderDrive(TURN_SPEED, rotate90, rotate90 * -1, 2.0); // Turn 90 degrees towards driver station
            encoderDrive(DRIVE_SPEED,  alignParking * -1,  alignParking * -1, 3.0); // hrizonal back to the border line
            encoderDrive(TURN_SPEED, rotate90 * -1, rotate90, 2.0); // Turn 90 degrees towards back to park
            encoderDrive(DRIVE_SPEED, -12, -12, 5.0);  // Reverse 24 Inches with 5 Sec timeout
        }
        else
        {
            // drop the yellow and wait for manual competition time
            placeDownPixels();
            arm2_servo.setPosition(1);
        }

        sleep(1000);  // pause to display final telemetry message.
    }

    /*
     *  Method to preload the yellow and purple pixels prior to the starting of the
     *  autonomous mode. This needs to be called in the initialization phase.
     *  TODO: need to make sure we can actually do the initialization this way
     *  Problem is that with no opmode running, the hand servo goes to position 0 and drops the pixels.
     */
    public void preloadPixels() {
        hand_servo.setPosition(-1);
        arm2_servo.setPosition(-1);
        // Allow some time to let the second arm lower in position then grab the pixel and finally raise the small arm again
        sleep(2000);
        hand_servo.setPosition(1);
        sleep(800);

        arm2_servo.setPosition(1);
    }

    /*
     *  Method to place both preloaded pixels down and pick up the yellow one
     *  before proceeding.
     */
    public void placePurplePickYellowPixel() {

        placeDownPixels();

        // go back 3.5 in (pixel width), so that you can pick up the yellow pixel
        encoderDrive(DRIVE_SPEED,  -3.4,  -3.4, 5.0);

        // close hand to pick up yellow pixel and raise arm 2
        hand_servo.setPosition(1);
        sleep(800);
        arm2_servo.setPosition(1);
        //encoderDrive(DRIVE_SPEED,  3.4,  3.4, 5.0);
    }

    /*
     * Lowers the small arm and opens the hand servo, leaving the small arm down
     */
    public void placeDownPixels() {
        // lower arm 2
        arm2_servo.setPosition(-1);
        sleep(1200); // needed to close the hand when ready

        // open hand and drop both pixels
        hand_servo.setPosition(-1);
        sleep(500);

    }
    /*
     *  Method to place a pixel on the backdrop
     *  int isMainArmDirectionForward is 0 or 1. this could be removed. we know the motors
     *  start in the arm forward position
     */
    public void placePixelOnBackdrop(int isMainArmDirectionForward) {
        // check if the arm direction is forward, if it is, reverse the motors
        // and set the isMainArmDirectionForward to 0 to indicate that is now reverse
        if (isMainArmDirectionForward == 1) {
            left_arm.setDirection(DcMotor.Direction.FORWARD);
            right_arm.setDirection(DcMotor.Direction.REVERSE);
            isMainArmDirectionForward = 0;
        }

        // set target positiom of both arms to the final dropping position
        left_arm.setTargetPosition(450); // 530 is theoretical final position. adjust based on motor power.
        right_arm.setTargetPosition(450); // the greater the power, the smaller the final position number.
        // set mode of arm motors to RUN_TO_POSITION
        left_arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        right_arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        // give power to the motors, not too fast
        left_arm.setPower(0.6);
        right_arm.setPower(0.6);
        // empty loop that wits until the arm is in the target position
        while (opModeIsActive() && left_arm.isBusy()) {
        }
        // Set motors power to 0
        left_arm.setPower(0);
        right_arm.setPower(0);

        sleep(600); // probably not needed. at the end of the loop the arms should be in position

        // open the hand servo to drop pixel
        hand_servo.setPosition(-1);

        // Rotate arm back to original starting position
        left_arm.setDirection(DcMotor.Direction.REVERSE);
        right_arm.setDirection(DcMotor.Direction.FORWARD);
        isMainArmDirectionForward = 1;
        left_arm.setTargetPosition(0);
        right_arm.setTargetPosition(0);
        left_arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        right_arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        left_arm.setPower(0.6);
        right_arm.setPower(0.6);
        sleep(300); // added in place of the while loop, so it starts moving before the arm is completely down
        /* test more, then this might be removed
        while (opModeIsActive() && left_arm.isBusy()) {
        }
        left_arm.setPower(0);
        right_arm.setPower(0);
         */
    }
    /*
     *  Method to perform a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the OpMode running.
     */
    public void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS) {
        int newLeftTarget;
        int newRightTarget;

        // Ensure that the OpMode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = leftDrive.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRightTarget = rightDrive.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            leftDrive.setTargetPosition(newLeftTarget);
            rightDrive.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            double absSpeed = Math.abs(speed);
            leftDrive.setPower(absSpeed);
            rightDrive.setPower(absSpeed);

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                   (runtime.seconds() < timeoutS) &&
                   (leftDrive.isBusy() && rightDrive.isBusy())) {

                // Display it for the driver.

            }

            // Stop all motion;
            leftDrive.setPower(0);
            rightDrive.setPower(0);

            // Turn off RUN_TO_POSITION
            leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(250);   // optional pause after each move.
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

    private void detectPropPosition() {
        getCameraSetting();
        myExposure = 30;
        myGain = 2;
        setManualExposure(myExposure, myGain);
        Scalar lower = new Scalar(90, 100, 100);
        Scalar upper = new Scalar(130, 255, 255);
        double minArea = 8000;
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

        while (cDetection < 5000) {
            cDetection++;
            recordedPropPosition = colourMassDetectionProcessor.getRecordedPropPosition();
            if (colourMassDetectionProcessor.getLargestContourX() != -1 && colourMassDetectionProcessor.getLargestContourY() != -1) {
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
        if (recordedPropPosition == ColourMassDetectionProcessor.PropPositions.RIGHT) {
            recordedPropPosition = ColourMassDetectionProcessor.PropPositions.MIDDLE;
        }
        if (recordedPropPosition == ColourMassDetectionProcessor.PropPositions.UNFOUND) {
            recordedPropPosition = ColourMassDetectionProcessor.PropPositions.RIGHT;
        }
        // stop camera
        visionPortal.stopLiveView();
        visionPortal.stopStreaming();

        recordedPropPosition = ColourMassDetectionProcessor.PropPositions.RIGHT;

        // run case based on prop position
        switch (recordedPropPosition) {
            case LEFT:
                telemetry.addLine("Position on LEFT");
                telemetry.update();
                turnClockWise = -1; // Team Prop on left
                break;
            case MIDDLE:
                telemetry.addLine("Position on CENTER");
                telemetry.update();
                turnClockWise = 0; // Team Prop on center
                break;
            case RIGHT:
                telemetry.addLine("Position on right");
                telemetry.update();
                turnClockWise = 1; // Team Prop on right
                break;
        }
        telemetry.update();
    }
}
