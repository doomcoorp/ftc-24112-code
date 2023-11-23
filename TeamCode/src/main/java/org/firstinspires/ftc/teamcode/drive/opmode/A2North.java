package org.firstinspires.ftc.teamcode.drive.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="A2 Team Prop at North", group="Robot")

public class A2North extends Andytest1 {
    @Override
    public void runOpMode() {
        // init the preconditions
        isRedField = -1; // on the blue side
        turnClockWise = -1; // Team Prop on left
        dropYellow = false; // start from row 2
        
        // prepare the automation
        super.runOpMode();
    }
}