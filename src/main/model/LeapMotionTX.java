package main.model;

import com.leapmotion.leap.Hand;
import com.leapmotion.leap.HandList;

public class LeapMotionTX {
    private Hand leftHand;
    private Hand rightHand;

    public LeapMotionTX(){
        leftHand = null;
        rightHand= null;
    };

    // Assigns the left and right hand on each frame to its respective variable
    public void assignHands(HandList hands) {
        if (hands.get(0).isRight()) {
            this.rightHand = hands.get(0);
        } else {
            this.leftHand = hands.get(0);
        }

        if (hands.get(1).isRight()) {
            this.rightHand = hands.get(1);
        } else {
            this.leftHand = hands.get(1);
        }
    }

    // Returns the roll degrees between -90 and 90 for the right hand
    // Rolling right is positive and left is negative
    // Returns -1 if right hand isn't available
    public float getRightRollDegrees() {
        if (rightHand.isRight()) {
            float rollVector = rightHand.palmNormal().roll();
            float rollDegrees = rollVector * (float) (180/Math.PI);

            if (rollDegrees < -90) {
                rollDegrees = -90;
            } else if (rollDegrees > 90) {
                rollDegrees = 90;
            }

            System.out.println(-1 * rollDegrees);

            return (-1 * rollDegrees);
        }
        return -1;
    }

    // Returns the pitch degrees between -90 and 90 for right hand
    // A flat hand is 0 degrees
    // Pitching up give a positive pitch up to 90
    // Pitch down gives a negative pitch up to -90
    // Returns -1 if right hand isn't available
    public float getRightPitchDegrees() {
        if (rightHand.isRight()) {
            float pitchVector = rightHand.palmNormal().pitch();
            float pitchDegrees = pitchVector * (float) (180/Math.PI);
            pitchDegrees = pitchConversion(pitchDegrees);

            if (pitchDegrees < -60) {
                pitchDegrees = -60;
            } else if (pitchDegrees > 60) {
                pitchDegrees = 60;
            }

            return pitchDegrees;
        }
        return -1;
    }

    // Returns the yaw degrees for right hand
    // Hand facing forward is 0 degrees
    // Yaw right gives value between 0 to 180 (drone faces right)
    // Yaw facing left gives value between -180 to 0
    public float getRightYawDegrees(){
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
    public float getLeftThrustingPitch() {
        if (leftHand.isLeft()) {
            float pitchVector = leftHand.palmNormal().pitch();
            float thrustingDegrees = pitchVector * (float) (180/Math.PI);

            thrustingDegrees = pitchConversion(thrustingDegrees);

            if (thrustingDegrees < 0) {
                thrustingDegrees = 0;
            } else if (thrustingDegrees > 90) {
                thrustingDegrees = 90;
            }

            return thrustingDegrees;
        }
        return -1;
    }

    // Returns false if drone shouldn't be armed and true if it should be
    // If the left hand isn't over the Leap Motion or isn't flat within += 40 degrees then returns false
    // If the left hand is within += 40 degrees of being flat then returns true
    public boolean getLeftRollArming() {
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
    public float pitchConversion(float pitchDegrees) {
        if (pitchDegrees + 90 > 180) {
            pitchDegrees += (90 - 180);
            return pitchDegrees;
        } else {
            pitchDegrees += 90;
            return pitchDegrees;
        }
    }

    public int ChannelShift(float value, String channel){
        //channel shift for right roll
        if (channel == "RR"){
            value += 45.0;
            value *= 11.11; //Value of 100/9
            value += 1000;
            return (int) value;
        }
        //channel shift for right pitch
        else if (channel == "RP"){
            value += 45.0;
            value *= 11.11; //Value of 100/9
            value += 1000;
            return (int) value;
        }
        //channel shift for left thrust
        else if (channel == "LT"){
            value *= 11.11; //Value of 100/9
            value += 1000;
            return (int) value;
        }
        return -1;
    }
}
