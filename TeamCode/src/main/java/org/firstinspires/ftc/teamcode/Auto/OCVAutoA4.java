package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.opencv.core.Scalar;

@Autonomous(name="Blue Row 4", group="Robot")
public class OCVAutoA4 extends AutonomousBase {
    @Override
    public void runOpMode() {
        // tell red or blue team
        isRedField = -1;    // on the blue side (column A)
        dropYellow = true;  // on the backdrop side, i.e. go to the backdrop and drop the yellow pixel
        lower = new Scalar(90, 100, 100);
        upper = new Scalar(130, 255, 255);
        super.runOpMode();  // run the autonomous base mode
    }

}

