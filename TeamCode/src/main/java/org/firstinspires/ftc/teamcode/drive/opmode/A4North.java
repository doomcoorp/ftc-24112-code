package org.firstinspires.ftc.teamcode.drive.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="A4 Team Prop at North", group="Robot")

public class A4North extends Andytest1 {
    @Override
    public void runOpMode() {
        // init the preconditions
        isRedField = -1; // on the blue side
        turnClockWise = -1; // Team Prop on left
        dropYellow = true; // start from row 4
        
        // prepare the automation
        super.runOpMode();
    }
}