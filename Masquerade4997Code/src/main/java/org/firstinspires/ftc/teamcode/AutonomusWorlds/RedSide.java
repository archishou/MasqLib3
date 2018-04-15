package org.firstinspires.ftc.teamcode.AutonomusWorlds;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import Library4997.MasqUtilities.Direction;
import Library4997.MasqUtilities.MasqUtils;
import Library4997.MasqUtilities.StopCondition;
import Library4997.MasqWrappers.MasqLinearOpMode;
import SubSystems4997.SubSystems.Flipper;
import SubSystems4997.SubSystems.Gripper;
import SubSystems4997.SubSystems.Gripper.Grip;

/**
 * Created by Archish on 3/8/18.
 */
@Autonomous(name = "RedSide", group = "B")
public class RedSide extends MasqLinearOpMode implements Constants {
    double startTicks = 0, endTicks = 0;
    boolean secondCollection = false;
    public void runLinearOpMode() throws InterruptedException {
        robot.mapHardware(hardwareMap);
        robot.lineDetector.setMargin(20);
        robot.vuforia.initVuforia(hardwareMap);
        robot.redRotator.setPosition(ROTATOR_RED_CENTER);
        robot.initializeAutonomous();
        robot.initializeServos();
        robot.intake.motor1.setStalledAction(new Runnable() {
            @Override
            public void run() {
                robot.intake.setPower(OUTAKE);
            }
        });
        robot.intake.motor1.setUnStalledAction(new Runnable() {
            @Override
            public void run() {
                robot.intake.setPower(INTAKE);
            }
        });
        while (!opModeIsActive()) {
            dash.create("Double Block: ", robot.lineDetector.stop());
            dash.create("Line Block: ", robot.doubleBlock.stop());
            dash.update();
        }
        waitForStart();
        robot.intake.setPower(INTAKE);
        robot.intake.motor1.enableStallDetection();
        robot.vuforia.flash(true);
        robot.sleep(robot.getDelay());
        robot.vuforia.activateVuMark();
        String vuMark = "Left";
        runJewel();
        robot.vuforia.flash(false);
        robot.driveTrain.setClosedLoop(false);
        runVuMark(vuMark);
    }
    public void runJewel() {
        robot.jewelArmRed.setPosition(JEWEL_RED_OUT);
        robot.sleep(1000);
        if (robot.jewelColorRed.isRed()) robot.redRotator.setPosition(ROTATOR_RED_SEEN);
        else robot.redRotator.setPosition(ROTATOR_RED_NOT_SEEN);
        robot.sleep(250);
        robot.jewelArmRed.setPosition(JEWEL_RED_IN);
        robot.sleep(100);
    }
    public String readVuMark () {
        robot.waitForVuMark();
        return robot.vuforia.getVuMark();
    }
    public void runVuMark(String vuMark) {
        robot.intake.setPower(INTAKE);
        robot.redRotator.setPosition(ROTATOR_RED_CENTER);
        if (MasqUtils.VuMark.isCenter(vuMark)) {
            robot.drive(22, POWER_OPTIMAL, Direction.BACKWARD, 3);
            scoreRightFirstDump();
        }
        else if (MasqUtils.VuMark.isLeft(vuMark)) {
            robot.drive(30, POWER_OPTIMAL, Direction.BACKWARD, 3); //FINAL
            leftMulti();
        }
        else if (MasqUtils.VuMark.isRight(vuMark)) {
            robot.drive(10, POWER_OPTIMAL, Direction.BACKWARD, 2);
            scoreLeftFirstDump();
        }
        else if (MasqUtils.VuMark.isUnKnown(vuMark)) {
            robot.drive(8, POWER_OPTIMAL, Direction.BACKWARD, 2);
            scoreLeftFirstDump();
        }
    }
    public void scoreRightFirstDump() {
        robot.turnAbsolute(90, Direction.RIGHT);
        robot.yWheel.resetEncoder();
        robot.setYTarget(robot.yWheel.getPosition());
        robot.stop(new StopCondition() {
            @Override
            public boolean stop() {
                return secondBlock();
            }
        }, -90, POWER_LOW, Direction.FORWARD);
        robot.turnAbsolute(80, Direction.RIGHT);
        robot.strafeToY(POWER_OPTIMAL);
        robot.drive(30, POWER_OPTIMAL, Direction.BACKWARD);
        robot.flipper.setFlipperPosition(Flipper.Position.OUT);
        robot.gripper.setGripperPosition(Grip.OUT);
        robot.drive(4, POWER_OPTIMAL, Direction.BACKWARD);
        robot.drive(5);
        robot.flipper.setFlipperPosition(Flipper.Position.IN);
        robot.turnAbsolute(90, Direction.RIGHT);
        startTicks = Math.abs(robot.driveTrain.getCurrentPosition());
        robot.stop(new StopCondition() {
            @Override
            public boolean stop() {
                return robot.singleBlock.stop();
            }
        }, -90, POWER_LOW, Direction.FORWARD);
        robot.drive(10, POWER_OPTIMAL, Direction.BACKWARD);
        robot.turnAbsolute(110, Direction.RIGHT);
        robot.stop(new StopCondition() {
            @Override
            public boolean stop() {
                return secondBlock();
            }
        }, -90, POWER_LOW, Direction.FORWARD);
        endTicks = Math.abs(robot.driveTrain.getCurrentPosition());
        MasqUtils.sleep(750);
        if (secondBlock()) {
            secondCollection = true;
            robot.drive(10, POWER_OPTIMAL, Direction.BACKWARD);
            robot.turnAbsolute(130, Direction.RIGHT);
            robot.drive(10, POWER_HIGH, Direction.FORWARD);
            robot.drive(10, POWER_HIGH, Direction.BACKWARD);
            robot.gripper.setGripperPosition(Grip.CLAMP);
            robot.intake.setPower(OUTAKE);
        }
        robot.turnAbsolute(90, Direction.RIGHT);
        super.runSimultaneously(new Runnable() {
            @Override
            public void run() {
                if (secondCollection) {
                    robot.strafeToY(POWER_OPTIMAL);
                    robot.drive(50, POWER_HIGH, Direction.BACKWARD);
                }
                robot.drive(45, POWER_HIGH, Direction.BACKWARD);
            }
        }, new Runnable() {
            @Override
            public void run() {
                robot.gripper.setGripperPosition(Grip.CLAMP);
                robot.flipper.setFlipperPosition(Flipper.Position.OUT);
            }
        });
        super.runSimultaneously(new Runnable() {
            @Override
            public void run() {
                robot.turnRelative(20, Direction.LEFT);
                MasqUtils.sleep(250);
                robot.drive(10, POWER_OPTIMAL, Direction.BACKWARD);
                robot.drive(10, POWER_OPTIMAL, Direction.FORWARD);
            }
        }, new Runnable() {
            @Override
            public void run() {
                robot.sleep(1000);
                robot.gripper.setGripperPosition(Grip.OUT);
                robot.flipper.setFlipperPosition(Flipper.Position.IN);
                robot.sleep(500);
            }
        });
        robot.intake.setPower(INTAKE);
    }
    public void leftMulti() {
        robot.turnAbsolute(90, Direction.RIGHT);
        robot.yWheel.resetEncoder();
        robot.setYTarget(robot.yWheel.getPosition());
        robot.stop(new StopCondition() {
            @Override
            public boolean stop() {
                return secondBlock();
            }
        }, -90, POWER_LOW, Direction.FORWARD);
        robot.turnAbsolute(80, Direction.RIGHT);
        robot.strafeToY(POWER_OPTIMAL);
        robot.gripper.setGripperPosition(Grip.CLAMP);
        robot.sleep(250);
        robot.flipper.setFlipperPosition(Flipper.Position.OUT);
        robot.drive(50, POWER_OPTIMAL, Direction.BACKWARD, 3);
        robot.turnAbsolute(70, Direction.RIGHT);
        robot.gripper.setGripperPosition(Grip.OUT);
        robot.drive(4, POWER_OPTIMAL, Direction.BACKWARD);
        robot.drive(5);
        robot.flipper.setFlipperPosition(Flipper.Position.IN);
        robot.turnAbsolute(70, Direction.RIGHT);
        startTicks = Math.abs(robot.driveTrain.getCurrentPosition());
        robot.stop(new StopCondition() {
            @Override
            public boolean stop() {
                return robot.singleBlock.stop();
            }
        }, -70, POWER_LOW, Direction.FORWARD);
        robot.drive(10, POWER_OPTIMAL, Direction.BACKWARD);
        robot.turnAbsolute(80, Direction.RIGHT);
        robot.stop(new StopCondition() {
            @Override
            public boolean stop() {
                return secondBlock();
            }
        }, -70, POWER_LOW, Direction.FORWARD);
        endTicks = Math.abs(robot.driveTrain.getCurrentPosition());
        MasqUtils.sleep(750);
        if (secondBlock()) {
            secondCollection = true;
            robot.drive(10, POWER_OPTIMAL, Direction.BACKWARD);
            robot.turnAbsolute(130, Direction.RIGHT);
            robot.drive(10, POWER_HIGH, Direction.FORWARD);
            robot.drive(10, POWER_HIGH, Direction.BACKWARD);
            robot.gripper.setGripperPosition(Grip.CLAMP);
            robot.intake.setPower(OUTAKE);
        }
        robot.turnAbsolute(80, Direction.RIGHT);
        super.runSimultaneously(new Runnable() {
            @Override
            public void run() {
                if (secondCollection) {
                    robot.strafeToY(POWER_OPTIMAL);
                    robot.drive(50, POWER_HIGH, Direction.BACKWARD);
                }
                robot.drive(45, POWER_HIGH, Direction.BACKWARD);
            }
        }, new Runnable() {
            @Override
            public void run() {
                robot.gripper.setGripperPosition(Grip.CLAMP);
                robot.flipper.setFlipperPosition(Flipper.Position.OUT);
            }
        });
        super.runSimultaneously(new Runnable() {
            @Override
            public void run() {
                MasqUtils.sleep(250);
                robot.drive(10, POWER_OPTIMAL, Direction.BACKWARD);
                robot.drive(10, POWER_OPTIMAL, Direction.FORWARD);
            }
        }, new Runnable() {
            @Override
            public void run() {
                robot.sleep(1000);
                robot.gripper.setGripperPosition(Grip.OUT);
                robot.flipper.setFlipperPosition(Flipper.Position.IN);
                robot.sleep(500);
            }
        });
        robot.intake.setPower(INTAKE);
    }
    public void scoreLeftFirstDump() {
        robot.turnAbsolute(60, Direction.RIGHT);
        robot.flipper.setFlipperPosition(Flipper.Position.OUT);
        robot.gripper.setGripperPosition(Grip.OUT);
        robot.drive(4, POWER_OPTIMAL, Direction.BACKWARD, .75);
        robot.drive(5);
        robot.flipper.setFlipperPosition(Flipper.Position.IN);
        robot.intake.motor1.setStallDetection(true);
        robot.intake.setPower(INTAKE);
        robot.turnAbsolute(90, Direction.RIGHT);
        robot.stop(new StopCondition() {
            @Override
            public boolean stop() {
                return robot.singleBlock.stop();
            }
        }, -90, POWER_LOW, Direction.FORWARD);
        robot.drive(10, POWER_OPTIMAL, Direction.BACKWARD);
        startTicks = Math.abs(robot.driveTrain.getCurrentPosition());
        robot.stop(new StopCondition() {
            @Override
            public boolean stop() {
                return secondBlock();
            }
        }, -90, POWER_LOW, Direction.FORWARD);
        endTicks = Math.abs(robot.driveTrain.getCurrentPosition());
        MasqUtils.sleep(750);
        if (secondBlock()) {
            secondCollection = true;
            robot.drive(10, POWER_OPTIMAL, Direction.BACKWARD);
            robot.turnAbsolute(130, Direction.RIGHT);
            robot.drive(10, POWER_HIGH, Direction.FORWARD);
            robot.drive(10, POWER_OPTIMAL, Direction.BACKWARD);
        }
        robot.sleep(100);
        robot.gripper.setGripperPosition(Gripper.Grip.CLAMP);
        robot.turnAbsolute(90, Direction.RIGHT);
        robot.drive(((endTicks - startTicks) / MasqUtils.CLICKS_PER_INCH), POWER_HIGH, Direction.BACKWARD);
        robot.gripper.setGripperPosition(Grip.CLAMP);
        robot.turnAbsolute(75, Direction.RIGHT);
        super.runSimultaneously(new Runnable() {
            @Override
            public void run() {
                if (secondCollection) {
                    robot.drive(50, POWER_HIGH, Direction.BACKWARD);
                }
                robot.drive(45, POWER_HIGH, Direction.BACKWARD);
            }
        }, new Runnable() {
            @Override
            public void run() {
                robot.gripper.setGripperPosition(Grip.CLAMP);
                robot.flipper.setFlipperPosition(Flipper.Position.OUT);
            }
        });
        super.runSimultaneously(new Runnable() {
            @Override
            public void run() {
                robot.drive(10, POWER_OPTIMAL, Direction.BACKWARD);
                robot.drive(10, POWER_OPTIMAL, Direction.FORWARD);
            }
        }, new Runnable() {
            @Override
            public void run() {
                robot.sleep(1000);
                robot.gripper.setGripperPosition(Grip.OUT);
                robot.flipper.setFlipperPosition(Flipper.Position.IN);
            }
        });
        robot.intake.setPower(INTAKE);
        if (!secondCollection) {
            robot.turnAbsolute(110, Direction.RIGHT);
            robot.drive(20);
            robot.drive(20, POWER_OPTIMAL, Direction.BACKWARD);
            robot.gripper.setGripperPosition(Grip.CLAMP);
            robot.flipper.setFlipperPosition(Flipper.Position.OUT);
            robot.turnAbsolute(80, Direction.RIGHT);
            robot.gripper.setGripperPosition(Grip.OUT);
            robot.drive(20, POWER_OPTIMAL, Direction.BACKWARD);
            robot.drive(75);
        }
        robot.gripper.setGripperPosition(Grip.OUT);
        robot.flipper.setFlipperPosition(Flipper.Position.IN);
        robot.sleep(500);
    }
    boolean secondBlock () {
        return robot.doubleBlock.stop();
    }
}