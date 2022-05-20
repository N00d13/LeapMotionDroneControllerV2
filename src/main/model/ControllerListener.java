package main.model;

import com.leapmotion.leap.*;

//Object that runs each frame the controller is connected
public class ControllerListener extends Listener {

    private SerialPortWriter portWriter;
    private LeapMotionTX leapMotionTX;

    //Constructor
    public ControllerListener(SerialPortWriter portWriter) {
        this.portWriter = portWriter;
        leapMotionTX = new LeapMotionTX();
    }

    public void onFrame(Controller controller) {
        Frame frame = controller.frame(); //Gets the most recent frame
        HandList hands = frame.hands();
        leapMotionTX.assignHands(hands);

        float rollDegrees = leapMotionTX.getRightRollDegrees();
        float pitchDegrees = leapMotionTX.getRightPitchDegrees();
        float yawDegrees = leapMotionTX.getRightYawDegrees();
        boolean leftHandArmed = leapMotionTX.getLeftRollArming();
        float leftHandThrust = leapMotionTX.getLeftThrustingPitch();

        portWriter.write("1300"); //Writes to serial port

        try {
            Thread.sleep(30); //Gives processing time for arduino
        } catch (InterruptedException e) {
            System.out.println("onFrame Sleep Exception: ");
            e.printStackTrace();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("onFrame Sleep Exception: ");
            e.printStackTrace();
        }

        portWriter.write("1800"); //Writes to serial port

        try {
            Thread.sleep(30); //Gives processing time for arduino
        } catch (InterruptedException e) {
            System.out.println("onFrame Sleep Exception: ");
            e.printStackTrace();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("onFrame Sleep Exception: ");
            e.printStackTrace();
        }
    }

    public void onInit(Controller controller) {
        System.out.println("Controller initialized");
    }

    public void onConnect(Controller controller) {
        System.out.println("Controller is connected");}

    public void onDisconnect(Controller controller) {
        System.out.println("Controller is disconnected");
    }

    public void onExit(Controller controller){
        System.out.println("Controller is disconnected");
    }

}
