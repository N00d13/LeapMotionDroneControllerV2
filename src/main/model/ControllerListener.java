package main.model;

import com.leapmotion.leap.*;

// Opens LeapMotion controller object and reads information sent by LeapMotionTX object.
public class ControllerListener extends Listener {

    private SerialPortWriter portWriter; // Object that writes to serial COM port.
    private LeapMotionTX leapMotionTX; // Object that reads LeapMotion controller information.

    private int rollDegrees; // Represents roll degrees of right hand.
    private int pitchDegrees; // Represents pitch degrees of right hand.
    private int leftHandThrust; // Represents thrust degrees of left hand.
    private int yawDegrees; // Represents yaw degrees of left hand.


    // MODIFIES: this.
    // EFFECTS: Initializes portWriter and leapMotionTX.
    public ControllerListener(SerialPortWriter portWriter) {
        this.portWriter = portWriter;
        leapMotionTX = new LeapMotionTX();
    }

    // MODIFIES: leapMotionTX, portWriter.
    // EFFECT: Reads hand information and writes it to the serial COM port.
    public void onFrame(Controller controller) {
        Frame frame = controller.frame(); //Gets the most recent frame
        HandList hands = frame.hands();
        leapMotionTX.assignHands(hands);

        if (leapMotionTX.HandsAvailable()) {
            AssignChannelValues(true);
        } else {
            AssignChannelValues(false);
        }

        System.out.println("Thrust Value: " + leftHandThrust + ", Pitch Value: " + pitchDegrees +
                            ", Roll Value: " + rollDegrees + ", Yaw Value: " + yawDegrees + ".");
        portWriter.write(pitchDegrees + "," + rollDegrees + "," + leftHandThrust + "," + yawDegrees);

        try {
            Thread.sleep(30); //Gives processing time for arduino to read information
        } catch (InterruptedException e) {
            System.out.println("onFrame Sleep Exception: ");
            e.printStackTrace();
        }
    }

    // EFFECT: Shift the information on each channel for Arduino to read.
    private void AssignChannelValues(boolean handsAvailable){
        if (handsAvailable) {
            rollDegrees = leapMotionTX.ChannelShift(leapMotionTX.getRightRollDegrees(), "RR");
            pitchDegrees = leapMotionTX.ChannelShift(leapMotionTX.getRightPitchDegrees(), "RP");
            leftHandThrust = leapMotionTX.ChannelShift(leapMotionTX.getLeftThrustingPitch(), "LT");
            yawDegrees = 1500;  // Use this if you want to include yaw: leapMotionTX.ChannelShift(leapMotionTX.getLeftYawDegrees(), "LY");
        } else {
            leftHandThrust = 1000;
            rollDegrees = 1500;
            pitchDegrees = 1500;
            yawDegrees = 1500;
        }
    }

    // EFFECT: States when controller object is initialized.
    public void onInit(Controller controller) {
        System.out.println("Controller initialized");

        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            System.out.println("onFrame Sleep Exception: ");
            e.printStackTrace();
        }
    }

    // EFFECT: States when physical controller is connected.
    public void onConnect(Controller controller) {
        System.out.println("Controller is connected");

        System.out.println("Program is starting...");
    }

    // EFFECT: States whether physical controller is disconnected.
    public void onDisconnect(Controller controller) {
        System.out.println("Controller is disconnected");
    }

    // EFFECT: States when Listener object is removed from controller object.
    public void onExit(Controller controller){
        System.out.println("Controller is disconnected");
    }

}