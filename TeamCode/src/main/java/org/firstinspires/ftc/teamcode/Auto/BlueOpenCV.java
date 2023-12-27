package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.vision.ColourMassDetectionProcessor;
import org.firstinspires.ftc.vision.VisionPortal;
import org.opencv.core.Scalar;

@Autonomous(name = "BlueOpenCV", group = "Robot")
public class BlueOpenCV extends OpMode {
    private VisionPortal visionPortal;
    private ColourMassDetectionProcessor colourMassDetectionProcessor;
    @Override
    public void init() {

        Scalar lower = new Scalar(90, 100, 100);
        Scalar upper = new Scalar(130, 255, 255);
        double minArea = 8000;


        colourMassDetectionProcessor = new ColourMassDetectionProcessor(
                lower,
                upper,
                () -> minArea,
                () -> 213,
                () -> 426
        );
        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .addProcessor(colourMassDetectionProcessor)
                .build();

    }

    @Override
    public void init_loop() {

        double MEGABOMBA = colourMassDetectionProcessor.getLargestContourX();
        telemetry.addData("Currently Recorded Position", colourMassDetectionProcessor.getRecordedPropPosition());
        telemetry.addData("Camera State", visionPortal.getCameraState());
        telemetry.addData("Are:", colourMassDetectionProcessor.getLargestContourArea());
        telemetry.addData("Currently Detected Mass Center", "x: " + colourMassDetectionProcessor.getLargestContourX() + ", y: " + colourMassDetectionProcessor.getLargestContourY());
        }



    @Override
    public void start() {
       }


    @Override
    public void loop() {

    }


    @Override
public void stop() {
    colourMassDetectionProcessor.close();
    visionPortal.close();
}
}
