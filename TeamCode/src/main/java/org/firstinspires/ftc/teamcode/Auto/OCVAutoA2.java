package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Blue Row 2", group="Robot")
public class OCVAutoA2 extends AutonomousBase {
    @Override
    public void runOpMode() {
        isRedField = -1;    // on the blue side (column A)
        dropYellow = false; // on the public side, i.e. don't go to the backdrop
        super.runOpMode();  // run the autonomous base mode
    }
}

