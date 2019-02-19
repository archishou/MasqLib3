package org.firstinspires.ftc.teamcode.Robots.Falcon.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robots.Falcon.Falcon;

import java.util.ArrayList;
import java.util.List;

import Library4997.MasqSensors.MasqClock;
import Library4997.MasqWrappers.MasqLinearOpMode;

/**
 * Created by Archishmaan Peyyety on 10/27/18.
 * Project: MasqLib
 */
@TeleOp(name = "MECH", group = "NFS")
public class MECH extends MasqLinearOpMode implements Constants {
    private boolean prevB = false;
    private List<Double> times = new ArrayList<>();
    private Falcon falcon = new Falcon();
    @Override
    public void runLinearOpMode()  {
        falcon.setStartOpenCV(false);
        falcon.mapHardware(hardwareMap);
        falcon.initializeTeleop();
        falcon.hang.setClosedLoop(true);
        falcon.rotator.setLimitSwitch(falcon.magSwitch);
        falcon.driveTrain.setClosedLoop(true);
        falcon.dumper.setPosition(DUMPER_IN);
        while (!opModeIsActive()) {
            dash.create(falcon.magSwitch.getState());
            dash.update();
        }
        waitForStart();
        MasqClock masqClock = new MasqClock();
        while (opModeIsActive()) {
            falcon.MECH(controller1);
            if (controller2.b() && !prevB) {
                times.add(masqClock.seconds());
                masqClock.reset();
            }

            if (controller1.rightBumper()) falcon.lift.setPower(-1);
            else if (controller1.rightTriggerPressed()) falcon.lift.setPower(1);
            else {
                falcon.lift.setPower(0);
                falcon.lift.setBreakMode();
            }

            if (controller1.leftBumper()) falcon.collector.setPower(.5);
            else if (controller1.leftTriggerPressed()) falcon.collector.setPower(-.5);
            else falcon.collector.setPower(0);

            if (controller2.b()) falcon.dumper.setPosition(DUMPER_OUT);
            else falcon.dumper.setPosition(DUMPER_IN);

            if (controller2.leftStickY() < 0 && !falcon.limitTop.isPressed()) falcon.hang.setPower(-1);
            else if (controller2.leftStickY() > 0 && !falcon.limitBottom.isPressed()) falcon.hang.setPower(1);
            else {
                falcon.hang.setPower(0);
                falcon.hang.setBreakMode();
            }

            falcon.rotator.DriverControl(controller2);
            dash.create("1: ", falcon.driveTrain.leftDrive.motor1.getCurrentPosition());
            dash.create("2: ", falcon.driveTrain.leftDrive.motor2.getCurrentPosition());
            dash.update();
            controller1.update();
            prevB = controller2.b();
        }
    }
}
