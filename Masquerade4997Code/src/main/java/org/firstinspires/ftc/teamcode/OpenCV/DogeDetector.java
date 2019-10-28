package org.firstinspires.ftc.teamcode.OpenCV;

import com.disnodeteam.dogecv.detectors.skystone.SkystoneDetector;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvWebcam;

import Library4997.MasqResources.MasqMath.MasqVector;
/**
 * Created by Keval Kataria on 10/27/2019
 */
public class DogeDetector {
    private OpenCvCamera phoneCamera;
    private OpenCvWebcam webcam;
    private SkystoneDetector detector;
    private Cam cam;

    public enum Cam{
        PHONE, WEBCAM
    }

    public DogeDetector(Cam cam, HardwareMap hwMap){
        this.cam = cam;
        int cameraMonitorViewId = hwMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hwMap.appContext.getPackageName());
        if(cam.equals(Cam.PHONE)){
            phoneCamera = new OpenCvInternalCamera(OpenCvInternalCamera.CameraDirection.FRONT, cameraMonitorViewId);
            detector = new SkystoneDetector();
            phoneCamera.setPipeline(detector);
        }
        else if(cam.equals(Cam.WEBCAM)){
            webcam = new OpenCvWebcam(hwMap.get(WebcamName.class, "webcam"), cameraMonitorViewId);
            detector = new SkystoneDetector();
            webcam.setPipeline(detector);
        }
    }

    public void start(){
        if(cam.equals(Cam.PHONE)){
            phoneCamera.openCameraDevice();
            phoneCamera.startStreaming(320,240, OpenCvCameraRotation.UPRIGHT);
        }
        else if(cam.equals(Cam.WEBCAM)){
            webcam.openCameraDevice();
            webcam.startStreaming(320,240, OpenCvCameraRotation.UPRIGHT);
        }
    }

    public void stop(){
        if(cam.equals(Cam.PHONE)){
            phoneCamera.stopStreaming();
            phoneCamera.closeCameraDevice();
        }
        else if(cam.equals(Cam.WEBCAM)){
            webcam.stopStreaming();
            webcam.closeCameraDevice();
        }
    }

    public void pauseViewPort(){
        if(cam.equals(Cam.PHONE)) phoneCamera.pauseViewport();
        else if(cam.equals(Cam.WEBCAM)) webcam.pauseViewport();
    }

    public void resumeViewPort(){
        if(cam.equals(Cam.PHONE)) phoneCamera.resumeViewport();
        else if(cam.equals(Cam.WEBCAM)) webcam.resumeViewport();
    }

    public SkystonePosition getStoneSkystonePosition(){
        double x = getStoneX();
        if(x < 100) return SkystonePosition.LEFT;
        else if(x >= 100 && x <= 150) return SkystonePosition.MIDDLE;
        else if(x > 150) return SkystonePosition.RIGHT;
        return null;
    }

    public MasqVector getStoneSkystoneVector(){
        SkystonePosition position = getStoneSkystonePosition();
        if(position.equals(SkystonePosition.LEFT)) return new MasqVector(-8,29);
        else if(position.equals(SkystonePosition.MIDDLE)) return new MasqVector(0,20);
        else if(position.equals(SkystonePosition.RIGHT)) return new MasqVector(8,29);
        return null;
    }

    public double getStoneX(){
        return detector.getScreenPosition().x;
    }

    public double getStoneY(){
        return detector.getScreenPosition().y;
    }

    public boolean isStoneDetected(){
        return detector.foundRectangle() != null;
    }


    public enum SkystonePosition {
        LEFT, MIDDLE, RIGHT
    }
}