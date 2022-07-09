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

        System.out.println(leapMotionTX.HandsAvailable());

        if (leapMotionTX.HandsAvailable()) {
            AssignChannelValues(true);
        } else {
            AssignChannelValues(false);
        }

        portWriter.write(pitchDegrees + "," + rollDegrees + "," + leftHandThrust + "," + yawDegrees); //Writes to serial port

        System.out.println("Wrote to Transmitter");

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
            yawDegrees = leapMotionTX.ChannelShift(leapMotionTX.getLeftYawDegrees(), "LY");
        } else {
            rollDegrees = 1200;
            pitchDegrees = 1500;
            leftHandThrust = 1500;
            yawDegrees = 1500;
        }
    }

    public void onInit(Controller controller) {
        System.out.println("Controller initialized");
    }

    public void onConnect(Controller controller) {
        System.out.println("Controller is connected");

        //AskCalibration();

        System.out.println("Program is starting");


    }

    private void AskCalibration(){


        System.out.println("Do you need to calibrate the controller for Liftoff?   y/n");

        String nextInput;
        nextInput = input.nextLine();

        if (nextInput.equals("y")) {
            StartLiftoffCalibration();
        } else {
            System.out.println("Cancelled Calibration");
        }
    }

    private void StartLiftoffCalibration() {
        System.out.println("Starting Calibration...");
        System.out.println("Press \"s\" when you click \"start calibration\" in Liftoff");


        String nextInput;
        nextInput = input.nextLine();


        //System.out.println(nextInput);
        if (nextInput.equals("s")) {
            CalibrateRotateSticks();
        } else {
            System.out.println("Cancelled Calibration");
        }


    }

    private void CalibrateRotateSticks() {
        System.out.println("Writing to port");
        portWriter.write(1200 + "," + 1300 + "," + 1500 + "," + 1800); //Writes to serial port
        System.out.println("Wrote to Port");
        try {
            Thread.sleep(30); //Gives processing time for arduino
        } catch (InterruptedException e) {
            System.out.println("onFrame Sleep Exception: ");
            e.printStackTrace();
        }
    }


    public void onDisconnect(Controller controller) {
        System.out.println("Controller is disconnected");
    }

    public void onExit(Controller controller){
        System.out.println("Controller is disconnected");
    }


}
