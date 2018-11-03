package org.firstinspires.ftc.teamcode.Autonomus;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Robots.Falcon.Falcon;

import Library4997.MasqResources.MasqHelpers.Direction;
import Library4997.MasqWrappers.MasqLinearOpMode;

/**
 * Created by Archishmaan Peyyety on 10/20/18.
 * Project: MasqLib
 */
@Autonomous(name = "MainAutoV1", group = "NFS")
public class MainAutoV1 extends MasqLinearOpMode implements Constants {
    private Falcon falcon = new Falcon();
    private double rightBound = 95, centerBound = 125, majorBound = 170;
    @Override
    public void runLinearOpMode() throws InterruptedException {
        falcon.mapHardware(hardwareMap);
        falcon.initializeAutonomous();
        falcon.hangLatch.setPosition(AUTON_HANG_IN);
        falcon.adjuster.setPosition(ADJUSTER_OUT);
        while (!opModeIsActive()) {
            dash.create(falcon.imu.getAbsoluteHeading());
            dash.update();
        }
        waitForStart();
        falcon.hangLatch.setPosition(AUTON_HANG_OUT);
        falcon.sleep(1);
        falcon.imu.reset();
        double startAngle = falcon.imu.getRelativeYaw();
        falcon.turnRelative(60, Direction.LEFT);
        falcon.turnTillGold(0.3, Direction.LEFT);
        double endAngle = falcon.imu.getRelativeYaw();
        double dHeading = endAngle - startAngle;
        dash.create(dHeading);
        dash.update();

        /*if (dHeading > centerBound) falcon.turnRelative(20, Direction.RIGHT);
        else falcon.turnRelative(30, Direction.RIGHT);*/


        if (dHeading < rightBound) {
            falcon.turnRelative(20, Direction.RIGHT);
            dash.create("Right");
            dash.update();
            falcon.drive(25, 0.8, Direction.BACKWARD);
            falcon.turnRelative(110, Direction.RIGHT, 3);
            falcon.drive(10);
            falcon.collector.setPower(-.5);
            sleep(5);
            falcon.drive(60, Direction.BACKWARD);
        }
        else if (dHeading >= rightBound && dHeading < centerBound) {
            falcon.turnRelative(30, Direction.RIGHT);
            dash.create("Center");
            dash.update();
            falcon.drive(25, 0.8, Direction.BACKWARD);
            falcon.turnRelative(170, Direction.LEFT, 3);
            falcon.collector.setPower(-.5);
            sleep(5);
        }
        else if (dHeading >= centerBound && dHeading <= majorBound) {
            falcon.turnRelative(10, Direction.RIGHT);
            dash.create("Left");
            dash.update();
            falcon.drive(25, 0.8, Direction.BACKWARD);
            falcon.turnRelative(110, Direction.LEFT, 3);
            falcon.drive(10);
            falcon.collector.setPower(-.5);
            sleep(5);
            falcon.drive(60, Direction.BACKWARD);
        }
        else if (dHeading >= majorBound) {
            falcon.turnAbsolute(90, Direction.LEFT);
            falcon.drive(25, 0.8, Direction.BACKWARD);
            falcon.turnRelative(170, Direction.LEFT, 3);
            falcon.collector.setPower(-.5);
            sleep(5);
        }


    }
    @Override
    public void stopLinearOpMode() {
        falcon.goldAlignDetector.disable();
    }
}
