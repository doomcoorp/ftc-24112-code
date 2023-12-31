package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Disabled

@Autonomous(name="Red Row 4", group="Robot")
public class OCVAutoF4 extends AutonomousBase {
    @Override
    public void runOpMode() {
        isRedField = 1;     // on the red side (column F)
        dropYellow = true;  // on the backdrop side, i.e. go to the backdrop and drop the yellow pixel
        super.runOpMode();  // run the autonomous base mode
    }
}

