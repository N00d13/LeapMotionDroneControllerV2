package main.model;

import com.leapmotion.leap.*;

import java.lang.Integer;

//Object that runs each frame the controller is connected
public class ControllerListener extends Listener {
    private Hand leftHand;
    private Hand rightHand;
    private SerialPortWriter portWriter;

    //Constructor
    public ControllerListener(SerialPortWriter portWriter) {
        this.portWriter = portWriter;
    }

    public void onInit(Controller controller) {
        System.out.println("Controller initialized");
    }

    public void onConnect(Controller controller) {
        System.out.println("Controller is connected");
    }

    public void onDisconnect(Controller controller) {
        System.out.println("Controller is disconnected");
    }

    public void onExit(Controller controller){
        System.out.println("Controller is disconnected");
    }

    public void onFrame(Controller controller) {
        Frame frame = controller.frame(); //Gets the most recent frame
        HandList hands = frame.hands();
        assignHands(hands);

        int rollDegrees = degreeConversion(100) ;//degreeConversion(getRightRollDegrees());
        int pitchDegrees = degreeConversion(getRightPitchDegrees());
        int yawDegrees = degreeConversion(getRightYawDegrees());
        boolean leftHandArmed = getLeftRollArming();
        int leftHandThrust = degreeConversion(getLeftThrustingPitch());

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

        //send position to transmitter
        ControllerTX.setAction(rollDegrees, pitchDegrees, yawDegrees, leftHandThrust, leftHandArmed);
    }

    // Assigns the left and right hand on each frame to its respective variable
    private void assignHands(HandList hands) {
        if (hands.get(0).isRight()) {
            rightHand = hands.get(0);
        } else {
            leftHand = hands.get(0);
        }

        if (hands.get(1).isRight()) {
            rightHand = hands.get(1);
        } else {
            leftHand = hands.get(1);
        }
    }

    // Returns the roll degrees between -180 and 180 for the right hand
    // Rolling right is positive and left is negative
    // Returns -1 if right hand isn't available
    private float getRightRollDegrees() {
        if (rightHand.isRight()) {
            float rollVector = rightHand.palmNormal().roll();
            float rollDegrees = rollVector * (float) (180/Math.PI);
            return (-1 * rollDegrees);
        }
        return -1;
    }

    // Returns the pitch degrees between -90 and 90 for right hand
    // A flat hand is 0 degrees
    // Pitching up give a positive pitch up to 90
    // Pitch down gives a negative pitch up to -90
    // Returns -1 if right hand isn't available
    private float getRightPitchDegrees() {
        if (rightHand.isRight()) {
            float pitchVector = rightHand.palmNormal().pitch();
            float pitchDegrees = pitchVector * (float) (180/Math.PI);
            pitchDegrees = pitchConversion(pitchDegrees);
            return pitchDegrees;
        }
        return -1;
    }

    // Returns the yaw degrees for right hand
    // Hand facing forward is 0 degrees
    // Yaw right gives value between 0 to 180 (drone faces right)
    // Yaw facing left gives value between -180 to 0
    private float getRightYawDegrees(){
        if (rightHand.isRight()) {
            float yawVector = rightHand.palmNormal().yaw();
            return yawVector * (float) (180 / Math.PI);
        }
        return -1;
    }

    // Returns the pitch degrees between 0 and 90 for left hand
    // A flat hand is 0 degrees
    // Pitching up give a positive pitch up to 90
    // Pitch down gives a negative pitch up to 0
    // Returns -1 if left hand isn't available
    private float getLeftThrustingPitch() {
        if (leftHand.isLeft()) {
            float pitchVector = leftHand.palmNormal().pitch();
            float pitchDegrees = pitchVector * (float) (180/Math.PI);

            pitchDegrees = pitchConversion(pitchDegrees);

            if (pitchDegrees < 0) {
                pitchDegrees = 0;
            }
            return pitchDegrees;
        }
        return -1;
    }

    // Returns false if drone shouldn't be armed and true if it should be
    // If the left hand isn't over the Leap Motion or isn't flat within += 40 degrees then returns false
    // If the left hand is within += 40 degrees of being flat then returns true
    private boolean getLeftRollArming() {
        if (leftHand.isLeft()) {
            float rollVector = leftHand.palmNormal().roll();
            float rollDegrees = rollVector * (float) (180/Math.PI);
            if (rollDegrees > -40 && rollDegrees < 40) {
                return true;
            }
        }
        return false;
    }

    //Converts the pitch to rotate the degrees by 90
    private float pitchConversion(float pitchDegrees) {
        if (pitchDegrees + 90 > 180) {
            pitchDegrees += (90 - 180);
            return pitchDegrees;
        } else {
            pitchDegrees += 90;
            return pitchDegrees;
        }
    }

    //Converts degree to bits
    private int degreeConversion(float degree){
        return (int) Math.floor(degree*(125/180));
    }

    //Make sure all numbers are 4 digits long
    private int readSize(int input){
        String tempNum =Integer.toString(input);

        while (tempNum.length() < 4){
            if (tempNum.charAt(0) == '-'){
                continue;
            }
            else{
                tempNum = String.format("%01s" , tempNum);
            }
        }
        return Integer.parseInt(tempNum);
    }
}
