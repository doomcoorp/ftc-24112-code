package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Disabled

import org.opencv.core.Scalar;

@Autonomous(name="Red Row 2", group="Robot")
public class OCVAutoF2 extends AutonomousBase {
    @Override
    public void runOpMode() {
        isRedField = 1;     // on the red side (column F)
        dropYellow = false; // on the public side, i.e. don't go to the backdrop
        lower = new Scalar(150, 100, 100);
        upper = new Scalar(180, 255, 255);
        super.runOpMode();  // run the autonomous base mode
    }
}

