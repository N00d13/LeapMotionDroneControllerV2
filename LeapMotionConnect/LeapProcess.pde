// Process LeapMotion data to Arduino

import processing.serial.*;
import com.onformative.leap.LeapMotionP5;
import com.leapmotion.leap.Hand;
LeapMotionP5 leap;
Serial myPort;

//initialize quadcopter movements
float leftRoll;
float leftThrust;
float rightRoll;
float rightPitch;

//initialize hand positions
float leftX;
float leftY;
float rightX;
float rightY;

//Find the serial port that Arduino is connected to and set up Serial object to listen to the port
void setup() {
    size(500, 500);
    String portName = Serial.list()[0]; //change the 0 to a 1, 2 etc. to match port
    myPort = new Serial(this, portName, 9600); //change portName
    leap = new LeapMotion(this);
}

//Send leap output over the serial port
void draw(){
    //Get hands and set positions
    int hands = 0;
    for (Hand hand : leap.getHandList()) {
        if (hands < 2){
            PVector handPosition = leap.getPosition(hand);
            if (hand.isRight()){
                setRightHandPos(handPosition.x, handPosition,y);
                rightRoll = getRoll(hand);
                rightPitch = getPitch(hand);
            }
            if (hand.isLeft()){
                setLeftHandPos(handPosition.x, handPosition.y);
                leftRoll = getRoll(hand);
                leftThrust = getPitch(hand);
            }
        }

        hands++;

        //write to port -> needs to be changed to match arduino side and go to correct port
        myPort.write(leftRoll);
        myPort.write(leftThrust);
        myPort.write(rightRoll);
        myPort.write(rightPitch);
    }
}

void setLeftHandPos(float x, float y){
	leftX = x;
	leftY = y;
}

void setRightHandPos(float x, float y){
	rightX = x;
	rightY = y;
}

