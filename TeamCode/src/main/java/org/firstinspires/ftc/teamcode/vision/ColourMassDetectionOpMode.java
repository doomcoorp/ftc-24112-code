package org.firstinspires.ftc.teamcode.vision;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.opencv.core.Scalar;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Autonomous
public class ColourMassDetectionOpMode extends OpMode {
	private VisionPortal visionPortal;
	private ColourMassDetectionProcessor colourMassDetectionProcessor;
	private DcMotor leftDrive = null;
	private DcMotor rightDrive = null;

	private DcMotor left_arm;
	private DcMotor right_arm;
	private Servo arm2_servo;
	private Servo hand_servo;

	private ElapsedTime runtime = new ElapsedTime();

	// Calculate the COUNTS_PER_INCH for your specific drive train.
	// Go to your motor vendor website to determine your motor's COUNTS_PER_MOTOR_REV
	// For external drive gearing, set DRIVE_GEAR_REDUCTION as needed.
	// For example, use a value of 2.0 for a 12-tooth spur gear driving a 24-tooth spur gear.
	// This is gearing DOWN for less speed and more torque.
	// For gearing UP, use a gear ratio less than 1.0. Note this will affect the direction of wheel rotation.
	static final double COUNTS_PER_MOTOR_REV = 560;     // https://docs.revrobotics.com/duo-control/sensors/encoders/motor-based-encoders //1440 ;    // eg: TETRIX Motor Encoder
	static final double DRIVE_GEAR_REDUCTION = 1.0;     // No External Gearing.
	static final double WHEEL_DIAMETER_INCHES = 3.6;     //4.0 ;     // For figuring circumference
	static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
			(WHEEL_DIAMETER_INCHES * 3.1415);
	static final double DRIVE_SPEED = 0.4;
	static final double TURN_SPEED = 0.5;

	static final int isRedField = -1;        // this allows to mirror the mode for the blue field = -1

@Override
	public void init() {
		// the current range set by lower and upper is the full range
		// HSV takes the form: (HUE, SATURATION, VALUE)
		// which means to select our colour, only need to change HUE
		// the domains are: ([0, 180], [0, 255], [0, 255])
		// this is tuned to detect red, so you will need to experiment to fine tune it for your robot
		// and experiment to fine tune it for blue
		Scalar lower = new Scalar(150, 100, 100); // the lower hsv threshold for your detection
		Scalar upper = new Scalar(180, 255, 255); // the upper hsv threshold for your detection
		double minArea = 100; // the minimum area for the detection to consider for your prop

		colourMassDetectionProcessor = new ColourMassDetectionProcessor(
				lower,
				upper,
				() -> minArea,
				() -> 213,
				() -> 426
		);
		visionPortal = new VisionPortal.Builder()
				.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1")) // the camera on your robot is named "Webcam 1" by default
				.addProcessor(colourMassDetectionProcessor)
				.build();

		// you may also want to take a look at some of the examples for instructions on
		// how to have a switchable camera (switch back and forth between two cameras)
		// or how to manually edit the exposure and gain, to account for different lighting conditions
		// these may be extra features for you to work on to ensure that your robot performs
		// consistently, even in different environments
	}
	/**
	 * User-defined init_loop method
	 * <p>
	 * This method will be called repeatedly during the period between when
	 * the init button is pressed and when the play button is pressed (or the
	 * OpMode is stopped).
	 * <p>
	 * This method is optional. By default, this method takes no action.
	 */
	@Override
	public void init_loop() {
		telemetry.addData("Currently Recorded Position", colourMassDetectionProcessor.getRecordedPropPosition());
		telemetry.addData("Camera State", visionPortal.getCameraState());
		telemetry.addData("Currently Detected Mass Center", "x: " + colourMassDetectionProcessor.getLargestContourX() + ", y: " + colourMassDetectionProcessor.getLargestContourY());
		telemetry.addData("Currently Detected Mass Area", colourMassDetectionProcessor.getLargestContourArea());
	}
	
	/**
	 * User-defined start method
	 * <p>
	 * This method will be called once, when the play button is pressed.
	 * <p>
	 * This method is optional. By default, this method takes no action.
	 * <p>
	 * Example usage: Starting another thread.
	 */
	@Override
	public void start() {
		// shuts down the camera once the match starts, we dont need to look any more
		if (visionPortal.getCameraState() == VisionPortal.CameraState.STREAMING) {
			visionPortal.stopLiveView();
			visionPortal.stopStreaming();
		}
		
		// gets the recorded prop position
		ColourMassDetectionProcessor.PropPositions recordedPropPosition = colourMassDetectionProcessor.getRecordedPropPosition();
		
		// now we can use recordedPropPosition to determine where the prop is! if we never saw a prop, your recorded position will be UNFOUND.
		// if it is UNFOUND, you can manually set it to any of the other positions to guess
		if (recordedPropPosition == ColourMassDetectionProcessor.PropPositions.UNFOUND) {
			recordedPropPosition = ColourMassDetectionProcessor.PropPositions.MIDDLE;
		}
		
		// now we can use recordedPropPosition in our auto code to modify where we place the purple and yellow pixels
		switch (recordedPropPosition) {
			case LEFT:
				break;
			case UNFOUND:
				break;
			case MIDDLE:
				leftDrive  = hardwareMap.get(DcMotor.class, "left_drive");
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

				// Send telemetry message to indicate successful Encoder reset
				telemetry.addData("Starting at",  "%7d :%7d",
						leftDrive.getCurrentPosition(),
						rightDrive.getCurrentPosition());
				telemetry.update();

				// preload the yellow and purple pixels

				// Wait for the game to start (driver presses PLAY)
				//waitForStart();

				// Step through each leg of the path,
				// Note: Reverse movement is obtained by setting a negative distance (not speed)

				// move towards spike marks and place pixels
				encoderDrive(DRIVE_SPEED,  24,  24, 5.0);  // Forward to spike mark
				encoderDrive(TURN_SPEED,   isRedField * 2, isRedField * -2, 2.0);  // turn slightly to the left to avoid prop
				placePurplePickYellowPixel();
				//encoderDrive(TURN_SPEED,   -1, 1, 2.0);  // turn back to realign

				// manouver to backdrop avoiding spike marks and place pixel
				encoderDrive(TURN_SPEED,   isRedField * -6, isRedField * 6, 4.0);  // Turn facing back/driver station
				encoderDrive(DRIVE_SPEED, -12, -12, 2.0);  // Reverse 10 Inches to avoid spike marks on turn
				encoderDrive(TURN_SPEED,   isRedField * -12, isRedField * 12, 4.0);  // Turn facing back oppside side
				encoderDrive(DRIVE_SPEED, -19, -19, 5.0);  // Reverse 19 Inches

				//encoderDrive(DRIVE_SPEED, -10, -10, 2.0);  // Reverse 10 Inches to avoid spike marks on turn
				//encoderDrive(TURN_SPEED,   -11, 11, 4.0);  // Turn 90 degrees facing back
				//encoderDrive(DRIVE_SPEED, -19, -19, 5.0);  // Reverse 19 Inches
				//encoderDrive(TURN_SPEED,   -11, 11, 4.0);  // Turn 90 degrees towards center
				//encoderDrive(DRIVE_SPEED, -10, -10, 5.0);  // Reverse to align with center april tag

				encoderDrive(TURN_SPEED,   isRedField * 6, isRedField * -6, 4.0);  // Turn 90 degrees towards backdrop
				encoderDrive(DRIVE_SPEED, -12, -12, 5.0);  // Reverse
				placePixelOnBackdrop(1);

				// go to park
				encoderDrive(TURN_SPEED,   isRedField * 11, isRedField * -11, 4.0);  // Turn 90 degrees towards driver station
				encoderDrive(DRIVE_SPEED, -20, -20, 5.0);  // Reverse
				encoderDrive(TURN_SPEED,   isRedField * -11, isRedField * 11, 4.0);  // Turn 90 degrees towards back to park
				encoderDrive(DRIVE_SPEED, -15, -15, 5.0);  // Reverse 24 Inches with 4 Sec timeout


				telemetry.addData("Path", "Complete");
				telemetry.update();
				sleep(1000);  // pause to display final telemetry message
				break;
			case RIGHT:
				// code to do if we saw the prop on the right
				break;
		}
	}
	
	/**
	 * User-defined loop method
	 * <p>
	 * This method will be called repeatedly during the period between when
	 * the play button is pressed and when the OpMode is stopped.
	 */
	@Override
	public void loop() {

	}

	@Override
	public void stop() {
			// this closes down the portal when we stop the code, its good practice!
			colourMassDetectionProcessor.close();
			visionPortal.close();

	}
}
