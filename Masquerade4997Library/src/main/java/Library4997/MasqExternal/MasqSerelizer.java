package Library4997.MasqExternal;

import org.firstinspires.ftc.robotcontroller.internal.FtcOpModeRegister;
import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Archish on 10/15/17.
 */

public class MasqSerelizer implements Runnable {
    private FtcRobotControllerActivity robotControllerActivity = new FtcRobotControllerActivity();
    private FileOutputStream fileOutputStream;
    private OutputStreamWriter outputStreamWriter;
    private boolean close = false;
    private List<MasqHardware> hardwareList = new ArrayList<>(5);
    private MasqHardware hardwareOne, hardwareTwo, hardwareThree, hardwareFour, hardwareFive;
    public MasqSerelizer (){}
    public void startFileWriting(){
        new Thread(this).start();
    }
    public void setHardwareTracking(MasqHardware hardwareOne){
        this.hardwareOne = hardwareOne;
        hardwareList = Arrays.asList(this.hardwareOne);
    }
    public void setHardwareTracking(MasqHardware hardwareOne, MasqHardware hardwareTwo){
        this.hardwareOne = hardwareOne;
        this.hardwareTwo = hardwareTwo;
        hardwareList = Arrays.asList(this.hardwareOne, this.hardwareTwo);
    }
    public void setHardwareTracking(MasqHardware hardwareOne, MasqHardware hardwareTwo, MasqHardware hardwareThree){
        this.hardwareOne = hardwareOne;
        this.hardwareTwo = hardwareTwo;
        this.hardwareThree = hardwareThree;
        hardwareList = Arrays.asList(this.hardwareOne, this.hardwareTwo, this.hardwareThree);
    }
    public void setHardwareTracking(MasqHardware hardwareOne, MasqHardware hardwareTwo, MasqHardware hardwareThree, MasqHardware hardwareFour){
        this.hardwareOne = hardwareOne;
        this.hardwareTwo = hardwareTwo;
        this.hardwareThree = hardwareThree;
        this.hardwareFour = hardwareFour;
        hardwareList = Arrays.asList(this.hardwareOne, this.hardwareTwo, this.hardwareThree, this.hardwareFour);
    }
    public void setHardwareTracking(MasqHardware hardwareOne, MasqHardware hardwareTwo, MasqHardware hardwareThree, MasqHardware hardwareFour, MasqHardware hardwareFive){
        this.hardwareOne = hardwareOne;
        this.hardwareTwo = hardwareTwo;
        this.hardwareThree = hardwareThree;
        this.hardwareFour = hardwareFour;
        this.hardwareFive = hardwareFive;
        hardwareList = Arrays.asList(this.hardwareOne, this.hardwareTwo, this.hardwareThree, this.hardwareFour, this.hardwareFive);
    }
    private void createFiles(){
        for (MasqHardware hardware: hardwareList){
            try {
                fileOutputStream = robotControllerActivity.getFileOutput(hardware.getName());
                outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    private void update(){
        for (MasqHardware hardware: hardwareList) {
            int dashLength = hardware.getDash().length;
            for (int i = 0; i < dashLength; i++) {
                try {outputStreamWriter.write(hardware.getName()+ hardware.getDash()[i]);}
                catch (IOException e) {e.printStackTrace();}
            }
        }
    }
    private String[] generateString() {
        List<String> read = new ArrayList<>();
        return (String[]) read.toArray();
    }
    public String[] readFromFile(){
        return generateString();
    }
    @Override
    public void run() {
        boolean close = false;
        while (!close) {
            update();
            close = this.close;
            sleep();
        }
    }
    public void close() {
        close = true;
        try {outputStreamWriter.flush();outputStreamWriter.close();}
        catch (IOException e) {e.printStackTrace();}
    }
    private void sleep(){
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}