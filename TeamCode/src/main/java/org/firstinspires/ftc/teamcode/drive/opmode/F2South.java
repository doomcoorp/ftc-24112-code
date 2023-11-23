package org.firstinspires.ftc.teamcode.drive.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="F2 Team Prop at South", group="Robot")

public class F2South extends Andytest1 {
    @Override
    public void runOpMode() {
        // init the preconditions
        isRedField = 1; // on the red side
        turnClockWise = -1; // Team Prop on left
        dropYellow = false; // start from row 2
        
        // prepare the automation
        super.runOpMode();
    }
}