package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.opencv.core.Scalar;

@Autonomous(name="Red Row 4", group="Robot")
public class OCVAutoF4 extends AutonomousBase {
    @Override
    public void runOpMode() {
        isRedField = 1;     // on the red side (column F)
        dropYellow = true;  // on the backdrop side, i.e. go to the backdrop and drop the yellow pixel
        lower = new Scalar(150, 100, 100);
        upper = new Scalar(180, 255, 255);
        super.runOpMode();  // run the autonomous base mode
    }
}

