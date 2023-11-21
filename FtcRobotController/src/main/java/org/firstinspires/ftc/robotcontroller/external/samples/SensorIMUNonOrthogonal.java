

package org.firstinspires.ftc.robotcontroller.external.samples;

import static com.qualcomm.hardware.rev.RevHubOrientationOnRobot.xyzOrientation;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;


@TeleOp(name = "Sensor: IMU Non-Orthogonal", group = "Sensor")
@Disabled
public class SensorIMUNonOrthogonal extends LinearOpMode
{

    IMU imu;





    @Override public void runOpMode() throws InterruptedException {



        imu = hardwareMap.get(IMU.class, "imu");

        /* Define how the hub is mounted to the robot to get the correct Yaw, Pitch and Roll values.
         *
         * You can apply up to three axis rotations to orient your Hub according to how it's mounted on the robot.
         *
         * The starting point for these rotations is the "Default" Hub orientation, which is:
         * 1) Hub laying flat on a horizontal surface, with the Printed Logo facing UP
         * 2) Rotated such that the USB ports are facing forward on the robot.
         *
         * The order that the rotations are performed matters, so this sample shows doing them in the order X, Y, then Z.
         * For specifying non-orthogonal hub mounting orientations, we must temporarily use axes
         * defined relative to the Hub itself, instead of the usual Robot Coordinate System axes
         * used for the results the IMU gives us. In the starting orientation, the Hub axes are
         * aligned with the Robot Coordinate System:
         *
         * X Axis:  Starting at Center of Hub, pointing out towards I2C connectors
         * Y Axis:  Starting at Center of Hub, pointing out towards USB connectors
         * Z Axis:  Starting at Center of Hub, pointing Up through LOGO
         *
         * Positive rotation is defined by right-hand rule with thumb pointing in +ve direction on axis.
         *
         * Some examples.
         *
         * ----------------------------------------------------------------------------------------------------------------------------------
         * Example A) Assume that the hub is mounted on a sloped plate at the back of the robot, with the USB ports coming out the top of the hub.
         *  The plate is tilted UP 60 degrees from horizontal.
         *
         *  To get the "Default" hub into this configuration you would just need a single rotation.
         *  1) Rotate the Hub +60 degrees around the X axis to tilt up the front edge.
         *  2) No rotation around the Y or Z axes.
         *
         *  So the X,Y,Z rotations would be 60,0,0
         *
         * ----------------------------------------------------------------------------------------------------------------------------------
         * Example B) Assume that the hub is laying flat on the chassis, but it has been twisted 30 degrees towards the right front wheel to make
         *  the USB cable accessible.
         *
         *  To get the "Default" hub into this configuration you would just need a single rotation, but around a different axis.
         *  1) No rotation around the X or Y axes.
         *  1) Rotate the Hub -30 degrees (Clockwise) around the Z axis, since a positive angle would be Counter Clockwise.
         *
         *  So the X,Y,Z rotations would be 0,0,-30
         *
         * ----------------------------------------------------------------------------------------------------------------------------------
         *  Example C) Assume that the hub is mounted on a vertical plate on the right side of the robot, with the Logo facing out, and the
         *  Hub rotated so that the USB ports are facing down 30 degrees towards the back wheels of the robot.
         *
         *  To get the "Default" hub into this configuration will require several rotations.
         *  1) Rotate the hub +90 degrees around the X axis to get it standing upright with the logo pointing backwards on the robot
         *  2) Next, rotate the hub +90 around the Y axis to get it facing to the right.
         *  3) Finally rotate the hub +120 degrees around the Z axis to take the USB ports from vertical to sloping down 30 degrees and
         *     facing towards the back of the robot.
         *
         *  So the X,Y,Z rotations would be 90,90,120
         */

        // The next three lines define the desired axis rotations.
        // To Do: EDIT these values to match YOUR mounting configuration.
        double xRotation = 0;  // enter the desired X rotation angle here.
        double yRotation = 0;  // enter the desired Y rotation angle here.
        double zRotation = 0;  // enter the desired Z rotation angle here.

        Orientation hubRotation = xyzOrientation(xRotation, yRotation, zRotation);

        // Now initialize the IMU with this mounting orientation
        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(hubRotation);
        imu.initialize(new IMU.Parameters(orientationOnRobot));

        // Loop and update the dashboard
        while (!isStopRequested()) {
            telemetry.addData("Hub orientation", "X=%.1f,  Y=%.1f,  Z=%.1f \n", xRotation, yRotation, zRotation);

            // Check to see if heading reset is requested
            if (gamepad1.y) {
                telemetry.addData("Yaw", "Resetting\n");
                imu.resetYaw();
            } else {
                telemetry.addData("Yaw", "Press Y (triangle) on Gamepad to reset\n");
            }

            // Retrieve Rotational Angles and Velocities
            YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
            AngularVelocity angularVelocity = imu.getRobotAngularVelocity(AngleUnit.DEGREES);

            telemetry.addData("Yaw (Z)", "%.2f Deg. (Heading)", orientation.getYaw(AngleUnit.DEGREES));
            telemetry.addData("Pitch (X)", "%.2f Deg.", orientation.getPitch(AngleUnit.DEGREES));
            telemetry.addData("Roll (Y)", "%.2f Deg.\n", orientation.getRoll(AngleUnit.DEGREES));
            telemetry.addData("Yaw (Z) velocity", "%.2f Deg/Sec", angularVelocity.zRotationRate);
            telemetry.addData("Pitch (X) velocity", "%.2f Deg/Sec", angularVelocity.xRotationRate);
            telemetry.addData("Roll (Y) velocity", "%.2f Deg/Sec", angularVelocity.yRotationRate);
            telemetry.update();
        }
    }
}
