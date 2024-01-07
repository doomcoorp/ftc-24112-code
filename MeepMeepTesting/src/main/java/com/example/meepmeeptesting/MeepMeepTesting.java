package com.example.meepmeeptesting;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.acmerobotics.roadrunner.trajectory.Trajectory;

import com.acmerobotics.roadrunner.drive.Drive;;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedDark;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;



public class MeepMeepTesting {
    public static void main(String[] args) {
        // Declare a MeepMeep instance
        // With a field size of 800 pixels
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Required: Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 13.3)
                // Option: Set theme. Default = ColorSchemeRedDark()
                .setColorScheme(new ColorSchemeRedDark())
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(12, -61, Math.toRadians(270)))
                                .back(38)
                                // move back
                                .lineToLinearHeading(new Pose2d(12,-35, Math.toRadians(90)))
                                //wait for lower arm
                                .waitSeconds(1)
                                //open claw distance required
                                .back(1)
                                .waitSeconds(0.5)
                                .back(2)
                                .waitSeconds(0.3)
                                .splineToSplineHeading(new Pose2d(37, -35, Math.toRadians(180)), Math.toRadians(0))
                                .waitSeconds(0)
                                .back(1)
                                .waitSeconds(2)
                                .strafeLeft(1)
                                .waitSeconds(2)
                                .strafeLeft(23)
                                .back(17)
                                .waitSeconds(30)
                                // lower arm 2
                                .addDisplacementMarker(48, () -> {
                                })
                                // open claw
                                .addDisplacementMarker(50, () -> {

                                })
                                // close claw, raise arm 2
                                .addDisplacementMarker(51,() -> {
                                })
                                .addDisplacementMarker(53, () -> {
                                })
                                // raise big arm to backdrop
                                .addTemporalMarker(10.4, () -> {

                                })


                                // open claw
                                .addTemporalMarker(12.7, () -> {
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
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}