package main.model;

import com.leapmotion.leap.Hand;
import com.leapmotion.leap.HandList;

// Calculates the normal vector of the left and right hand and returns the angle corresponding to the required channels (thrust, pitch, roll, yaw).
public class LeapMotionTX {
    private Hand leftHand; //Hand object representing the left hand
    private Hand rightHand; // Hand object representing the right hand

    // MODIFIES: this
    // EFFECT: Assigns the left and right-hand values to null.
    public LeapMotionTX(){
        leftHand = null;
        rightHand= null;
    };

    // MODIFIES: this
    // EFFECT: Assigns the left and right hand on each frame to its respective variable.
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

    // EFFECT: Returns true if both hands are available, otherwise returns false.
    public boolean HandsAvailable() {
        if ((leftHand != null && rightHand != null) && (leftHand.isValid() && rightHand.isValid())) {
            return true;
        }
        return false;
    }

    // EFFECT: Returns the roll degrees between -90 and 90 for the right hand.
    //         Rolling right is positive and left is negative.
    //         Returns -1 if right hand isn't available.
    public float getRightRollDegrees() {
        if (rightHand.isRight()) {
            float rollVector = rightHand.palmNormal().roll();
            float rollDegrees = rollVector * (float) (180/Math.PI);
            rollDegrees *= -1;

            // Set upper and lower bounds to maintain proper roll when flying
            if (rollDegrees < -45 || (rollDegrees > 100 && rollDegrees < 150)) {
                rollDegrees = -45;
            } else if (rollDegrees > 45) {
                rollDegrees = 45;
            }

            return (rollDegrees);
        }
        return -1;
    }

    // EFFECT: Returns the pitch degrees between -90 and 90 for right hand.
    //         A flat hand is 0 degrees.
    //         Pitching up give a positive pitch up to 90 and pitch down gives a negative pitch up to -90.
    //         Returns -1 if right hand isn't available.
    public float getRightPitchDegrees() {
        if (rightHand.isRight()) {
            float pitchVector = rightHand.palmNormal().pitch();
            float pitchDegrees = pitchVector * (float) (180/Math.PI);
            pitchDegrees = pitchConversion(pitchDegrees);

            // Set upper and lower bounds to maintain proper pitch when flying
            if (pitchDegrees < -45) {
                pitchDegrees = -45;
            } else if (pitchDegrees > 45) {
                pitchDegrees = 45;
            }

            return pitchDegrees;
        }
        return -1;
    }

    // EFFECT: Returns the yaw degrees for the left hand.
    //         Hand facing forward is 0 degrees.
    //         Yaw right gives value between 0 to 180 (drone faces right).
    //         Yaw facing left gives value between -180 to 0.
    public float getLeftYawDegrees(){
        if (leftHand.isLeft()) {
            float yawVector = leftHand.palmNormal().roll();
            float yawDegrees = yawVector * (float) (180/Math.PI);

            // Set upper and lower bounds to maintain proper yaw when flying
            if (yawDegrees < -45 || (yawDegrees > 100 && yawDegrees < 150)) {
                yawDegrees = -45;
            } else if (yawDegrees > 45) {
                yawDegrees = 45;
            }

            return (-1 * yawDegrees);
        }
        return -1;
    }

    // EFFECT: Returns the thrust degrees between 0 and 90 for left hand.
    //         A flat hand is 0 degrees.
    //         Pitching up give a positive thrust up to 90.
    //         Pitch down gives a lower bound thrust of 0.
    //         Returns -1 if left hand isn't available.
    public float getLeftThrustingPitch() {
        if (leftHand.isLeft()) {
            float pitchVector = leftHand.palmNormal().pitch();
            float thrustingDegrees = pitchVector * (float) (180/Math.PI);
            thrustingDegrees = pitchConversion(thrustingDegrees);

            // Set upper and lower bounds to maintain proper thrust when flying
            if (thrustingDegrees < 0) {
                thrustingDegrees = 0;
            } else if (thrustingDegrees > 90) {
                thrustingDegrees = 90;
            }

            return thrustingDegrees;
        }
        return -1;
    }

    // EFFECT: Converts the pitch to rotate the degrees by 90 for conversion to Arduino.
    public float pitchConversion(float pitchDegrees) {
        if (pitchDegrees + 90 > 180) {
            pitchDegrees += (90 - 180);
            return pitchDegrees;
        } else {
            pitchDegrees += 90;
            return pitchDegrees;
        }
    }

    // EFFECT: Returns a shifted channel level for conversion to Arduino.
    //         Converts degrees from hand motions to drone channels that read from 1000-2000, with respect to bounds.
    //         If the channel name inputted doesn't exist then returns -1.
    public int ChannelShift(float value, String channel){
        // Channel shift for right roll
        if (channel == "RR"){
            value += 45.0;
            value *= 11.11; //Value of 100/9 -> (channel lower bound 1000)/(degrees 90)
            value += 1000;
            return (int) value;
        }
        // Channel shift for right pitch
        else if (channel == "RP"){
            value += 45.0;
            value *= 11.11; // Value of 100/9
            value += 1000;
            return (int) value;
        }
        // Channel shift for left thrust
        else if (channel == "LT"){
            value *= 11.11; // Value of 100/9
            value += 1000;
//            if (value > 1200){
//                value = 1200;
//            }
            return (int) value;
        }
        // Channel shift for left yaw
        else if (channel == "LY"){
            value += 45.0;
            value *= 11.11; // Value of 100/9
            value += 1000;
            return (int) value;
        }
        return -1;
    }
}
