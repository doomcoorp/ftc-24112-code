package org.firstinspires.ftc.teamcode.vision

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.vision.VisionPortal

class awdadhgafdwuj {package org.firstinspires.ftc.teamcode.vision
    import org.firstinspires.ftc.teamcode.vision.EnhancedColourMassDetectionProcessor
    @Autonomous
    class EnhancedColourMassDetectionOpMode : OpMode() {
        private var visionPortal: VisionPortal? = null
        private var colourMassDetectionProcessor: EnhancedColourMassDetectionProcessor? = null

        /**
         * User-defined init method
         *
         *
         * This method will be called once, when the INIT button is pressed.
         */
        override fun init() {
            // the current range set by lower and upper is the full range
            // HSV takes the form: (HUE, SATURATION, VALUE)
            // which means to select our colour, only need to change HUE
            // the domains are: ([0, 180], [0, 255], [0, 255])
            // this is tuned to detect red, so you will need to experiment to fine tune it for your robot
            // and experiment to fine tune it for blue
            val lowerH = 150.0 // the lower hsv threshold for your detection
            val upperH = 180.0 // the upper hsv threshold for your detection
            val minArea = 100.0 // the minimum area for the detection to consider for your prop
            colourMassDetectionProcessor = EnhancedColourMassDetectionProcessor(
                lowerH,
                upperH,
                { minArea },  // these are lambda methods, in case we want to change them while the match is running, for us to tune them or something
                { 213 }
            )  // the left dividing line, in this case the left third of the frame
            { 426 } // the left dividing line, in this case the right third of the frame

            visionPortal = VisionPortal.Builder()
                .setCamera(
                    hardwareMap.get(
                        WebcamName::class.java,
                        "Webcam 1"
                    )
                ) // the camera on your robot is named "Webcam 1" by default
                .addProcessor(colourMassDetectionProcessor)
                .build()

            // you may also want to take a look at some of the examples for instructions on
            // how to have a switchable camera (switch back and forth between two cameras)
            // or how to manually edit the exposure and gain, to account for different lighting conditions
            // these may be extra features for you to work on to ensure that your robot performs
            // consistently, even in different environments
        }

        /**
         * User-defined init_loop method
         *
         *
         * This method will be called repeatedly during the period between when
         * the init button is pressed and when the play button is pressed (or the
         * OpMode is stopped).
         *
         *
         * This method is optional. By default, this method takes no action.
         */
        override fun init_loop() {}

        /**
         * User-defined start method
         *
         *
         * This method will be called once, when the play button is pressed.
         *
         *
         * This method is optional. By default, this method takes no action.
         *
         *
         * Example usage: Starting another thread.
         */
        override fun start() {
            // shuts down the camera once the match starts, we dont need to look any more
            if (visionPortal!!.cameraState == VisionPortal.CameraState.STREAMING) {
                visionPortal!!.stopLiveView()
                visionPortal!!.stopStreaming()
            }
            var recordedPropPosition: EnhancedColourMassDetectionProcessor.PropPositions =
                colourMassDetectionProcessor.getRecordedPropPosition()
            // now we can use recordedPropPosition to determine where the prop is! if we never saw a prop, your recorded position will be UNFOUND.
            // if it is UNFOUND, you can manually set it to any of the other positions to guess
            if (recordedPropPosition === EnhancedColourMassDetectionProcessor.PropPositions.UNFOUND) {
                recordedPropPosition = EnhancedColourMassDetectionProcessor.PropPositions.MIDDLE
            }
            when (recordedPropPosition) {
                LEFT -> {}
                UNFOUND, MIDDLE -> {}
                RIGHT -> {}
            }
        }

        /**
         * User-defined loop method
         *
         *
         * This method will be called repeatedly during the period between when
         * the play button is pressed and when the OpMode is stopped.
         */
        override fun loop() {}

        /**
         * User-defined stop method
         *
         *
         * This method will be called once, when this OpMode is stopped.
         *
         *
         * Your ability to control hardware from this method will be limited.
         *
         *
         * This method is optional. By default, this method takes no action.
         */
        override fun stop() {
            // this closes down the portal when we stop the code, its good practice!
            colourMassDetectionProcessor.close()
            visionPortal!!.close()
        }
    }

}