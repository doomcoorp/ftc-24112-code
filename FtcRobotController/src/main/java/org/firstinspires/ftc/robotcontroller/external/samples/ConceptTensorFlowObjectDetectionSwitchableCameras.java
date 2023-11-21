

package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionPortal.CameraState;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;


@TeleOp(name = "Concept: TensorFlow Object Detection Switchable Cameras", group = "Concept")
@Disabled
public class ConceptTensorFlowObjectDetectionSwitchableCameras extends LinearOpMode {

    
    private WebcamName webcam1, webcam2;
    private boolean oldLeftBumper;
    private boolean oldRightBumper;

    
    private TfodProcessor tfod;

    
    private VisionPortal visionPortal;

    @Override
    public void runOpMode() {

        initTfod();


        telemetry.addData("DS preview on/off", "3 dots, Camera Stream");
        telemetry.addData(">", "Touch Play to start OpMode");
        telemetry.update();
        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {

                telemetryCameraSwitching();
                telemetryTfod();


                telemetry.update();


                if (gamepad1.dpad_down) {
                    visionPortal.stopStreaming();
                } else if (gamepad1.dpad_up) {
                    visionPortal.resumeStreaming();
                }

                doCameraSwitching();


                sleep(20);
            }
        }


        visionPortal.close();

    }

    
    private void initTfod() {


        tfod = new TfodProcessor.Builder().build();

        webcam1 = hardwareMap.get(WebcamName.class, "Webcam 1");
        webcam2 = hardwareMap.get(WebcamName.class, "Webcam 2");
        CameraName switchableCamera = ClassFactory.getInstance()
            .getCameraManager().nameForSwitchableCamera(webcam1, webcam2);


        visionPortal = new VisionPortal.Builder()
            .setCamera(switchableCamera)
            .addProcessor(tfod)
            .build();

    }

    
    private void telemetryCameraSwitching() {
        if (visionPortal.getActiveCamera().equals(webcam1)) {
            telemetry.addData("activeCamera", "Webcam 1");
            telemetry.addData("Press RightBumper", "to switch to Webcam 2");
        } else {
            telemetry.addData("activeCamera", "Webcam 2");
            telemetry.addData("Press LeftBumper", "to switch to Webcam 1");
        }
    }

    
    private void telemetryTfod() {

        List<Recognition> currentRecognitions = tfod.getRecognitions();
        telemetry.addData("# Objects Detected", currentRecognitions.size());


        for (Recognition recognition : currentRecognitions) {
            double x = (recognition.getLeft() + recognition.getRight()) / 2 ;
            double y = (recognition.getTop()  + recognition.getBottom()) / 2 ;

            telemetry.addData(""," ");
            telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100);
            telemetry.addData("- Position", "%.0f / %.0f", x, y);
            telemetry.addData("- Size", "%.0f x %.0f", recognition.getWidth(), recognition.getHeight());
        }

    }

    
    private void doCameraSwitching() {
        if (visionPortal.getCameraState() == CameraState.STREAMING) {


            boolean newLeftBumper = gamepad1.left_bumper;
            boolean newRightBumper = gamepad1.right_bumper;
            if (newLeftBumper && !oldLeftBumper) {
                visionPortal.setActiveCamera(webcam1);
            } else if (newRightBumper && !oldRightBumper) {
                visionPortal.setActiveCamera(webcam2);
            }
            oldLeftBumper = newLeftBumper;
            oldRightBumper = newRightBumper;
        }
    }

}
