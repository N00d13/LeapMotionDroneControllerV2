package main.model;

import com.leapmotion.leap.*;
import main.model.ControllerTX;

import java.io.IOException;

//Object that runs each frame the controller is connected
public class ControllerListener extends Listener {
    private Hand leftHand;
    private Hand rightHand;
    SerialPortWriter portWriter;

    public void onConnect(Controller controller) {
        portWriter = new SerialPortWriter();
        System.out.println("Controller is connected");
    }

    public void onFrame(Controller controller) {
        Frame frame = controller.frame();
        HandList hands = frame.hands();
        assignHands(hands);

        float rollDegrees = degreeConversion(getRightRollDegrees());
        float pitchDegrees = degreeConversion(getRightPitchDegrees());
        float yawDegrees = degreeConversion(getRightYawDegrees());
        boolean leftHandArmed = getLeftRollArming();
        float leftHandThrust = degreeConversion(getLeftThrustingPitch());

        try {
            portWriter.WriteToPort(56, "pitch");
            portWriter.WriteToPort(56, "yaw");
            portWriter.WriteToPort(56, "pich");
            portWriter.WriteToPort(56, "pitch");
        } catch (IOException e) {
            //STUB
        }


        System.out.println("Frame id: " + frame.id() +
                            ", Right hand?: " + rightHand.isRight() +
                            ", Hand count: " + frame.hands().count() +
                            //", Roll Degrees: " + rollDegrees +
                            //", Pitch Degrees: " + pitchDegrees +
                            ", Left Hand armed: " + leftHandArmed +
                            ", Left Hand Thrust: " + leftHandThrust
                            );

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

    //Converts degrees to bits
    private int degreeConversion(float degree){
        return (int) Math.floor(degree*(125/180));
    }
}
