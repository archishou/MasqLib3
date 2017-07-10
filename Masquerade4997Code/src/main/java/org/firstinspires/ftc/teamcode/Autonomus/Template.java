package org.firstinspires.ftc.teamcode.Autonomus;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import Library4997.MasqWrappers.MasqLinearOpMode;

/**
 * This is a basic template copy and paste this class for any auto,
 * refactor the file name to match the auto class title
 */

@Autonomous(name = "Template-Auto", group = "Template")
@Disabled
public class Template extends MasqLinearOpMode implements Constants {
    private int delay = 0;
    public void runLinearOpMode() throws InterruptedException {
        while (!opModeIsActive()) {
            dash.create(robot.imu);
            dash.update();
        }
        waitForStart();
    }
}