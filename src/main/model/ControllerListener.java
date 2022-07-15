package main.model;

import com.leapmotion.leap.*;

import java.util.Scanner;

//Object that runs each frame the controller is connected
public class ControllerListener extends Listener {

    private Scanner input;
    private SerialPortWriter portWriter;
    private LeapMotionTX leapMotionTX;

    private int rollDegrees;
    private int pitchDegrees;
    private int leftHandThrust;
    private int yawDegrees;

    private boolean doneSetup = false;

    //Constructor
    public ControllerListener(SerialPortWriter portWriter) {
        this.portWriter = portWriter;
        leapMotionTX = new LeapMotionTX();
        input = new Scanner(System.in);
    }

    public void onFrame(Controller controller) {
        Frame frame = controller.frame(); //Gets the most recent frame
        HandList hands = frame.hands();
        leapMotionTX.assignHands(hands);


        if (leapMotionTX.HandsAvailable()) {
            AssignChannelValues(true);
        } else {
            AssignChannelValues(false);
        }

        portWriter.write(pitchDegrees + "," + rollDegrees + "," + leftHandThrust + "," + yawDegrees); //Writes to serial port


        try {
            Thread.sleep(30); //Gives processing time for arduino
        } catch (InterruptedException e) {
            System.out.println("onFrame Sleep Exception: ");
            e.printStackTrace();
        }
    }

    private void AssignChannelValues(boolean handsAvailable){
        if (handsAvailable) {
            rollDegrees = leapMotionTX.ChannelShift(leapMotionTX.getRightRollDegrees(), "RR");
            pitchDegrees = leapMotionTX.ChannelShift(leapMotionTX.getRightPitchDegrees(), "RP");
            leftHandThrust = leapMotionTX.ChannelShift(leapMotionTX.getLeftThrustingPitch(), "LT");
            yawDegrees = 1500;   // Use this if want to include yaw: leapMotionTX.ChannelShift(leapMotionTX.getLeftYawDegrees(), "LY");
        } else {
            leftHandThrust = 1000;
            rollDegrees = 1500;
            pitchDegrees = 1500;
            yawDegrees = 1500;
        }
    }

    public void onInit(Controller controller) {
        System.out.println("Controller initialized");

        try {
            Thread.sleep(30); //Gives processing time for controller to connect
        } catch (InterruptedException e) {
            System.out.println("onFrame Sleep Exception: ");
            e.printStackTrace();
        }
    }

    public void onConnect(Controller controller) {
        System.out.println("Controller is connected");

        System.out.println("Program is starting...");
    }


    public void onDisconnect(Controller controller) {
        System.out.println("Controller is disconnected");
    }

    public void onExit(Controller controller){
        System.out.println("Controller is disconnected");
    }


}
