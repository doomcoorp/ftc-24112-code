package org.firstinspires.ftc.teamcode.drive.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="F4 Team Prop at North", group="Robot")

public class F4North extends Andytest1 {
    @Override
    public void runOpMode() {
        // init the preconditions
        isRedField = 1; // on the red side
        turnClockWise = 1; // Team Prop on right
        dropYellow = true; // start from row 4
        
        // prepare the automation
        super.runOpMode();
    }
}