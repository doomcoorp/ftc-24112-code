package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedDark;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.acmerobotics.roadrunner.trajectory.Trajectory;

import com.acmerobotics.roadrunner.drive.Drive;;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedDark;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
public class redmiddle {
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
                                    .lineToLinearHeading(new Pose2d(-36,-23, Math.toRadians(270)))
                                    // move back
                                    .strafeRight(5)
                                    .lineToLinearHeading(new Pose2d(-36,-35, Math.toRadians(90)))
                                    //wait for lower arm
                                    .waitSeconds(1)
                                    //open claw distance required
                                    .back(1)
                                    .waitSeconds(0.5)
                                    .back(2)
                                    .waitSeconds(0.3)
                                    .back(3)
                                    .strafeLeft(5)
                                    .lineToLinearHeading(new Pose2d(-36,-12, Math.toRadians(180)))
                                    .lineToLinearHeading(new Pose2d(61,-12, Math.toRadians(180)))

                                    // lower arm 2
                                    .addDisplacementMarker(53, () -> {
                                    })
                                    // open claw
                                    .addDisplacementMarker(55, () -> {

                                    })
                                    // close claw, raise arm 2
                                    .addDisplacementMarker(56,() -> {
                                    })
                                    .addDisplacementMarker(58, () -> {
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

