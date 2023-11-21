

package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;


@TeleOp(name = "Sensor: Digital touch", group = "Sensor")
@Disabled
public class SensorDigitalTouch extends LinearOpMode {
    

    DigitalChannel digitalTouch;

    @Override
    public void runOpMode() {


        digitalTouch = hardwareMap.get(DigitalChannel.class, "sensor_digital");


        digitalTouch.setMode(DigitalChannel.Mode.INPUT);


        waitForStart();



        while (opModeIsActive()) {



            if (digitalTouch.getState() == true) {
                telemetry.addData("Digital Touch", "Is Not Pressed");
            } else {
                telemetry.addData("Digital Touch", "Is Pressed");
            }

            telemetry.update();
        }
    }
}
