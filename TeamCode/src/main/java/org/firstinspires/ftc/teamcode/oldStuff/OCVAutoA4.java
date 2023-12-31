package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Disabled

@Autonomous(name="Blue Row 4", group="Robot")
public class OCVAutoA4 extends AutonomousBase {
    @Override
    public void runOpMode() {
        // tell red or blue team
        isRedField = -1;    // on the blue side (column A)
        dropYellow = true;  // on the backdrop side, i.e. go to the backdrop and drop the yellow pixel
        super.runOpMode();  // run the autonomous base mode
    }

}

