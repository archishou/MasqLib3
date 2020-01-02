package Library4997;

import com.qualcomm.robotcore.hardware.HardwareMap;

import Library4997.MasqMotors.MasqMotor;
import Library4997.MasqResources.MasqHelpers.MasqHardware;
import Library4997.MasqSensors.MasqAdafruitIMU;
import Library4997.MasqWrappers.DashBoard;

import static Library4997.MasqResources.MasqUtils.adjustAngle;
import static Library4997.MasqResources.MasqUtils.sleep;

/**
 * Created by Archishmaan Peyyety on 8/9/18.
 * Project: MasqLib
 */

public abstract class MasqPositionTracker implements MasqHardware {
    private MasqMotor xSystem, ySystem;
    public MasqAdafruitIMU imu;
    private double prevHeading;
    private double globalX = 0, globalY = 0, prevX = 0, prevY = 0, xRadius = 0, yRadius = 0;

    public enum DeadWheelPosition {
        BOTH_CENTER, BOTH_PERPENDICULAR
    }

    public MasqPositionTracker(MasqMotor xSystem, MasqMotor ySystem, HardwareMap hardwareMap) {
        this.xSystem = xSystem;
        this.ySystem = ySystem;
        imu = new MasqAdafruitIMU("imu", hardwareMap);
        prevHeading = imu.getAbsoluteHeading();
        reset();
    }
    public MasqPositionTracker(MasqMotor xSystem, MasqMotor ySystem, String imuName, HardwareMap hardwareMap) {
        this.xSystem = xSystem;
        this.ySystem = ySystem;
        imu = new MasqAdafruitIMU(imuName, hardwareMap);
        reset();
    }
    public double getHeading () {
        return imu.getRelativeYaw();
    }
    public void updateSystem (DeadWheelPosition position) {
        switch (position) {
            case BOTH_CENTER: bothCenter(); break;
            case BOTH_PERPENDICULAR: bothPerpendicular(); break;
            default: break;
        }
    }

    public void reset() {
        xSystem.resetEncoder();
        ySystem.resetEncoder();
        imu.reset();
    }

    private void bothCenter() {
        double deltaX = (getXPosition() - prevX);
        double deltaY = (getYPosition() - prevY);
        double heading = Math.toRadians(getHeading());
        double x = deltaX * Math.cos(heading) - deltaY * Math.sin(heading);
        double y = deltaX * Math.sin(heading) + deltaY * Math.cos(heading);
        globalX += x;
        globalY += y;
        prevY = getXPosition();
        prevX = getYPosition();
    }

    private void bothPerpendicular() {
        double omega = Math.toRadians(getDHeading());
        double angularComponentX = xRadius * omega;
        double angularComponentY = yRadius * omega;
        DashBoard.getDash().create("angularComponentX: ", angularComponentX);
        DashBoard.getDash().create("angularComponentY: ", angularComponentY);
        DashBoard.getDash().create("getX: ", getXPosition());
        DashBoard.getDash().create("getY: ", getYPosition());
        double deltaX = (getXPosition() - prevX) - angularComponentX;
        double deltaY = (getYPosition() - prevY) - angularComponentY;
        prevX = getXPosition();
        prevY = getYPosition();
        DashBoard.getDash().create("deltaX: ", deltaX);
        DashBoard.getDash().create("deltaY: ", deltaY);
        double heading = Math.toRadians(getHeading());
        double x = deltaX * Math.cos(heading) - deltaY * Math.sin(heading);
        double y = deltaX * Math.sin(heading) + deltaY * Math.cos(heading);
        globalX += x;
        globalY += y;
    }

    public double getDHeading() {
        double change = imu.getAbsoluteHeading() - prevHeading;
        sleep(10);
        prevHeading = imu.getAbsoluteHeading();
        return adjustAngle(change);
    }

    public double getGlobalX() {
        return globalX * ((2 * Math.PI) / 1440);
    }
    public double getGlobalY() {
        return globalY * ((2 * Math.PI) / 1440);
    }

    public abstract double getXPosition();
    public abstract double getYPosition();

    public void setXRadius(double xRadius) {
        this.xRadius = xRadius;
    }

    public void setYRadius(double yRadius) {
        this.yRadius = yRadius;
    }

    @Override
    public String getName() {
        return "Tracker";
    }

    @Override
    public String[] getDash() {
        return new String[] {
            getName() +
            "GlobalX: " + globalX,
            "GlobalY: " + globalY,
            "Heading: " + getHeading(),
            "Heading Change: " + getDHeading()
        };
    }
}
