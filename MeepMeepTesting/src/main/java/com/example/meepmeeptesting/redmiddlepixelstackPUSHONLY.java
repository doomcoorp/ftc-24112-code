package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedDark;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

;

public class redmiddlepixelstackPUSHONLY {
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
                            drive.trajectorySequenceBuilder(new Pose2d(-36, -61, Math.toRadians(270)))
                                    .lineToLinearHeading(new Pose2d(-36,-16, Math.toRadians(270)))





                                    // move back
                            //wait for lower arm
                            .waitSeconds(1)
                            //open claw distance required
                            .back(2)
                                    .lineToLinearHeading(new Pose2d(-36,-10, Math.toRadians(270)))

                                    .lineToLinearHeading(new Pose2d(-52,-10, Math.toRadians(270)))
                                    .lineToLinearHeading(new Pose2d(-52,-59, Math.toRadians(270)))
                                    .lineToLinearHeading(new Pose2d(40,-59, Math.toRadians(270)))
                                    .lineToLinearHeading(new Pose2d(40,-12, Math.toRadians(270)))
                                    .lineToLinearHeading(new Pose2d(61,-12, Math.toRadians(270)))


                                    .waitSeconds(30)
                            // lower arm 2
                            // lower arm 2
                            .addDisplacementMarker(44, () -> {

                            })
                            // open claw
                            .addDisplacementMarker(46, () -> {
                            })
                            // close claw, raise arm 2
                            .addDisplacementMarker(50, () -> {

                            })
                            // raise big arm to backdrop
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

