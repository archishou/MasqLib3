package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import BasicLib4997.MasqLinearOpMode;
import BasicLib4997.MasqMotors.MasqRobot.Direction;

/**
 * This is a basic autonomous program to test the various autonomous functions.
 */

@Autonomous(name = "AutoTest", group = "Test")
public class AutoTest extends MasqLinearOpMode {

    public void runLinearOpMode() throws InterruptedException {
        while (!opModeIsActive()) {
            dash.create(robot.imu);
            dash.create(robot.ultra);
            dash.create(robot.leftColor);
            dash.update();
        }
        waitForStart();
        while (opModeIsActive()) {
            telemetry.addData("IMU", robot.imu.getHeading());
            telemetry.update();
        }
        robot.drive(0.7, 100, Direction.FORWARD, 1000);
       // robot.turn(45, Direction.RIGHT);
    }
}
