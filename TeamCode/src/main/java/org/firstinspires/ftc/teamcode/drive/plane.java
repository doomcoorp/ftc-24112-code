package org.firstinspires.ftc.teamcode.drive;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
@Config
@TeleOp(name="plane test 1")
public class plane extends LinearOpMode {
    private Servo servo1;
    private Servo servo2;

    @Override
    public void runOpMode() {
        servo1 = hardwareMap.get(Servo.class, "hand_servo");
        servo2 = hardwareMap.get(Servo.class, "arm2_servo" );
        planeServo();
        waitForStart();
    }

    public void planeServo() {
        servo1.setPosition(10);
        servo2.setPosition(-1);
        sleep(1000);
        servo1.setPosition(-1);
        servo2.setPosition(10);
    }
}