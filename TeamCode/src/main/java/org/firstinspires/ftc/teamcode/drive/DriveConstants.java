package org.firstinspires.ftc.teamcode.drive;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;


@Config
public class DriveConstants {

    
    public static final double TICKS_PER_REV = 537.6;
    public static final double MAX_RPM = 312.5;

    
    public static final boolean RUN_USING_ENCODER = true;
    public static PIDFCoefficients MOTOR_VELO_PID = new PIDFCoefficients(0, 0, 0,
            getMotorVelocityF(MAX_RPM / 60 * TICKS_PER_REV));

    
    public static double WHEEL_RADIUS = 1.4763;
    public static double GEAR_RATIO = 1;
    public static double TRACK_WIDTH = 15;

    
    public static double kV = 1.0 / rpmToVelocity(MAX_RPM);
    public static double kA = 0;
    public static double kStatic = 0;

    
    public static double MAX_VEL = 41.065033847087705;
    public static double MAX_ACCEL = 41.065033847087705;
    public static double MAX_ANG_VEL = Math.toRadians(130.71406249999998);
    public static double MAX_ANG_ACCEL = Math.toRadians(130.71406249999998);

    
    public static RevHubOrientationOnRobot.LogoFacingDirection LOGO_FACING_DIR =
            RevHubOrientationOnRobot.LogoFacingDirection.UP;
    public static RevHubOrientationOnRobot.UsbFacingDirection USB_FACING_DIR =
            RevHubOrientationOnRobot.UsbFacingDirection.FORWARD;


    public static double encoderTicksToInches(double ticks) {
        return WHEEL_RADIUS * 2 * Math.PI * GEAR_RATIO * ticks / TICKS_PER_REV;
    }

    public static double rpmToVelocity(double rpm) {
        return rpm * GEAR_RATIO * 2 * Math.PI * WHEEL_RADIUS / 60.0;
    }

    public static double getMotorVelocityF(double ticksPerSecond) {

        return 32767 / ticksPerSecond;
    }
}
