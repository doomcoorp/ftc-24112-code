package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedDark;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

;

public class redrightclosetobackdropPUSHONLY {
    public static void main(String[] args) {
        // Declare a MeepMeep instance
        // With a field size of 800 pixels
        MeepMeep meepMeep = new MeepMeep(900);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Required: Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(41.065033847087705, 41.065033847087705, 3.009366035461426, 2.832071299799152, 13.3)
                // Option: Set theme. Default = ColorSchemeRedDark()
                .setColorScheme(new ColorSchemeRedDark())
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(12, -61, Math.toRadians(270)))
                                .strafeLeft(5)
                                .lineToLinearHeading(new Pose2d(12,-36, Math.toRadians(0)))
                                // move back
                                .lineToLinearHeading(new Pose2d(12,-35, Math.toRadians(0)))
                                //wait for lower arm
                                .waitSeconds(2)
                                //open claw distance required
                                .back(1)
                                .waitSeconds(0.5)
                                .back(2)
                                .waitSeconds(0.3)
                                .back(3)
                                .lineToLinearHeading(new Pose2d(47,-41, Math.toRadians(180)))
                                .waitSeconds(5)
                                .strafeLeft(18)
                                .back(13)
                                .waitSeconds(30)
                                // lower arm 2
                                .addDisplacementMarker(31, () -> {
                                })
                                // open claw
                                .addDisplacementMarker(31, () -> {

                                })
                                // close claw, raise arm 2
                                .addDisplacementMarker(33,() -> {
                                })
                                .addDisplacementMarker(35, () -> {
                                })
                                .addTemporalMarker(9, () -> {
                                })

                                .addTemporalMarker(11, () -> {
                                })
                                // lower arm and claw
                                .addTemporalMarker(15, () -> {
                                })
                                .build()
                );

        // Set field image
        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                // Background opacity from 0-1
                .setBackgroundAlpha(1f)
                .addEntity(myBot)
                .start();
    }
}

