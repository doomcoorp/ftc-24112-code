

package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cCompassSensor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;


@TeleOp(name = "Sensor: MR compass", group = "Sensor")
@Disabled
public class SensorMRCompass extends LinearOpMode {

    ModernRoboticsI2cCompassSensor compass;
    ElapsedTime                    timer = new ElapsedTime();

    @Override public void runOpMode() {


        compass = hardwareMap.get(ModernRoboticsI2cCompassSensor.class, "compass");

        telemetry.log().setCapacity(20);
        telemetry.log().add("The compass sensor operates quite well out-of-the");
        telemetry.log().add("box, as shipped by the manufacturer. Precision can");
        telemetry.log().add("however be somewhat improved with calibration.");
        telemetry.log().add("");
        telemetry.log().add("To calibrate the compass once the opmode is");
        telemetry.log().add("started, make sure the compass is level, then");
        telemetry.log().add("press 'A' on the gamepad. Next, slowly rotate the ");
        telemetry.log().add("compass in a full 360 degree circle while keeping");
        telemetry.log().add("it level. When complete, press 'B'.");


        waitForStart();
        telemetry.log().clear();

        while (opModeIsActive()) {


            if (gamepad1.a && !compass.isCalibrating()) {

                telemetry.log().clear();
                telemetry.log().add("Calibration started");
                telemetry.log().add("Slowly rotate compass 360deg");
                telemetry.log().add("Press 'B' when complete");
                compass.setMode(CompassSensor.CompassMode.CALIBRATION_MODE);
                timer.reset();

                while (gamepad1.a && opModeIsActive()) {
                    doTelemetry();
                    idle();
                }
            }


            if (gamepad1.b && compass.isCalibrating()) {

                telemetry.log().clear();
                telemetry.log().add("Calibration complete");
                compass.setMode(CompassSensor.CompassMode.MEASUREMENT_MODE);

                if (compass.calibrationFailed()) {
                    telemetry.log().add("Calibration failed");
                    compass.writeCommand(ModernRoboticsI2cCompassSensor.Command.NORMAL);
                }

                while (gamepad1.a && opModeIsActive()) {
                    doTelemetry();
                    idle();
                }
            }

            doTelemetry();
        }
    }

    protected void doTelemetry() {

        if (compass.isCalibrating()) {

            telemetry.addData("compass", "calibrating %s", Math.round(timer.seconds())%2==0 ? "|.." : "..|");

        } else {



            telemetry.addData("heading", "%.1f", compass.getDirection());




            Acceleration accel = compass.getAcceleration();
            double accelMagnitude = Math.sqrt(accel.xAccel*accel.xAccel + accel.yAccel*accel.yAccel + accel.zAccel*accel.zAccel);
            telemetry.addData("accel", accel);
            telemetry.addData("accel magnitude", "%.3f", accelMagnitude);


            telemetry.addData("mag flux", compass.getMagneticFlux());
        }


        telemetry.addData("command", "%s", compass.readCommand());

        telemetry.update();
    }
}
