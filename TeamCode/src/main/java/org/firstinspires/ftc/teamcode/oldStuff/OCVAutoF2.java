package org.firstinspires.ftc.teamcode.oldStuff;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;


import org.opencv.core.Scalar;
@Disabled

@Autonomous(name="Red Row 2", group="Robot")
public class OCVAutoF2 extends org.firstinspires.ftc.teamcode.Auto.AutonomousBase {
    @Override
    public void runOpMode() {
        isRedField = 1;     // on the red side (column F)
        dropYellow = false; // on the public side, i.e. don't go to the backdrop
        Scalar lower = new Scalar(150, 100, 100);
        Scalar upper = new Scalar(180, 255, 255);
        super.runOpMode();  // run the autonomous base mode
    }
}

