package org.firstinspires.ftc.teamcode.Autonomus;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import Library4997.MasqExternal.Direction;
import Library4997.MasqExternal.MasqExternal;
import Library4997.MasqWrappers.MasqLinearOpMode;

/**
 * Created by Archish on 11/9/17.
 */
@Autonomous(name = "RED VUMARK", group = "Autonomus")
public class RedVuMark extends MasqLinearOpMode implements Constants {
    public void runLinearOpMode() throws InterruptedException {
        robot.mapHardware(hardwareMap);
        robot.vuforia.initVuforia(hardwareMap);
        robot.initializeAutonomous();
        dash.create(INIT_MESSAGE);
        dash.update();
        waitForStart();
        robot.initializeServos();
        robot.vuforia.activateVuMark();
        robot.waitForVuMark();
        String vuMark = robot.vuforia.getVuMark();
        dash.create(vuMark);
        dash.update();
        int addedAngle = runJewel();
        runVuMark(vuMark, addedAngle);
    }
    public int runJewel () {
        int addedAngle;
        robot.jewelArm.setPosition(JEWEL_OUT);
        MasqExternal.sleep(2000);
        if (robot.jewelColor.isBlue()) {
            robot.turn(20, Direction.RIGHT);
            addedAngle = 20;
        }
        else {
            robot.turn(20, Direction.LEFT);
            addedAngle = -20;
        }
        robot.jewelArm.setPosition(JEWEL_IN);
        return addedAngle;
    }
    public void runVuMark(String vuMark, int addedDistance) {
        if (MasqExternal.VuMark.isCenter(vuMark)){robot.turn(40 + addedDistance, Direction.LEFT);}
        else if (MasqExternal.VuMark.isLeft(vuMark)){robot.turn(40 + addedDistance, Direction.LEFT);}
        else if (MasqExternal.VuMark.isRight(vuMark)){robot.turn(10 + addedDistance, Direction.LEFT);}
        else robot.turn(40 + addedDistance, Direction.LEFT);
        robot.drive(90, POWER_LOW);
    }

 }