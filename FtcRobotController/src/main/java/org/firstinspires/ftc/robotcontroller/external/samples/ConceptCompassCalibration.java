

package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;



@TeleOp(name="Concept: Compass Calibration", group="Concept")
@Disabled
public class ConceptCompassCalibration extends LinearOpMode {

    
    public DcMotor leftDrive   = null;
    public DcMotor  rightDrive  = null;
    private ElapsedTime runtime = new ElapsedTime();
    CompassSensor       compass;

    final static double     MOTOR_POWER   = 0.2;
    static final long       HOLD_TIME_MS  = 3000;
    static final double     CAL_TIME_SEC  = 20;

    @Override
    public void runOpMode() {


        leftDrive  = hardwareMap.get(DcMotor.class, "left_drive");
        rightDrive = hardwareMap.get(DcMotor.class, "right_drive");




        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);


        compass = hardwareMap.get(CompassSensor.class, "compass");


        telemetry.addData("Status", "Ready to cal");
        telemetry.update();


        waitForStart();


        compass.setMode(CompassSensor.CompassMode.CALIBRATION_MODE);
        telemetry.addData("Compass", "Compass in calibration mode");
        telemetry.update();

        sleep(HOLD_TIME_MS);


        telemetry.addData("Compass", "Calibration mode. Turning the robot...");
        telemetry.update();
        leftDrive.setPower(MOTOR_POWER);
        rightDrive.setPower(-MOTOR_POWER);


        runtime.reset();
        while (opModeIsActive() && (runtime.time() < CAL_TIME_SEC)) {
            idle();
        }


        leftDrive.setPower(0);
        rightDrive.setPower(0);
        compass.setMode(CompassSensor.CompassMode.MEASUREMENT_MODE);
        telemetry.addData("Compass", "Returning to measurement mode");
        telemetry.update();

        sleep(HOLD_TIME_MS);


        if (compass.calibrationFailed())
            telemetry.addData("Compass", "Calibrate Failed. Try Again!");
        else
            telemetry.addData("Compass", "Calibrate Passed.");
        telemetry.update();
    }
}
