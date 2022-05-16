package main.model;

public class ControllerTX {

    //Take input(from leap) to set position on transmitter
    public static void setAction(float rightRoll, float rightPitch, float rightYaw, float thrust, boolean armed){
        //set right actions
        float rx = 0; //temporary lol these values need to change to coordinates based on degrees
        float ry = 0;
        //set left actions -- joystick axis
        float lx = (float) Math.cos(thrust);
        float ly = 0; //whatever value we can consider false
        if (armed) {
            ly = 40; //whatever value we can consider true -> send to channel
        }

        //System.out.println("The Right JoyStick Position is: " + rx + ry);
        //System.out.println("The Left JoyStick Position is: " + lx + ly);

        //These results should be sent to transmitter
    }

}
