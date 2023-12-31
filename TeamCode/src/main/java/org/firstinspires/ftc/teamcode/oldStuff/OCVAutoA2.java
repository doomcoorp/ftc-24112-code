package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Disabled

import org.opencv.core.Scalar;

@Autonomous(name="Blue Row 2", group="Robot")
public class    OCVAutoA2 extends AutonomousBase {

    @Override
    public void runOpMode() {
        isRedField = -1;    // on the blue side (column A)
        dropYellow = false; // on the public side, i.e. don't go to the backdrop
        lower = new Scalar(90, 100, 100);
        upper = new Scalar(130, 255, 255);
        super.runOpMode();  // run the autonomous base mode
    }
}

