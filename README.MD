# Leap Motion Hand Drone Controller

## Project Description

### Intro

Our software was created to read hand movements from an infrared camera (Leap Motion Controller) and be able to fly a drone according to that information. Going into the project, we didn't want to change any physical components on the drone so we had to create a new system to modify the channel output values on our transmitter. Using this newly developed method, it allows anyone running OpenTx firmware on their transmitter to fly their drone with an Arduino and Leap Motion controller.

### How it works!

In order to communicate our hand movements to the drone's receiver, we first send the normal vector rotations of our right and left hand to a java program using an API provided by Leap Motion. With this information, we calculate the degrees forward (pitch) and side (roll) of each hand for it's corresponding drone channel values. The left hand pitch up and down relates to thrust power to raise and lower the drone. The right-hand pitch relates to the pitch movement of the drone, and the right-hand roll relates to the roll movement of the drone.

We then send each channel value on one text line seperated by commas through a serial com port to a program written in Arduino. Once the Arduino program reads the incoming text line, it sends a PPM signal with the updated channel values to the trainer port of a transmitter. Finally, the transmitter sends the new channel levels to the receiver on the drone. 

Fun fact: The program sends new channel values every 30 milliseconds, that's 33 times a second!

#### ControllerListener

Initializes the Leap Motion controller object and waits for two hands to be present over the Leap Motion controller. ControllerListener is also responsible for reading channel information calculated by LeapMotionTX and sends them through the serial port connected to the Arduino.

#### LeapMotionTX

Once the hands are detected by the Leap Motion device, each movement is interpreted as thrust, pitch, roll and yaw. These motions are returned as degrees, between -45 to 45 for pitch, roll and yaw, and 0 to 90 for thrust. We set these bounds in order to allow for accurate and stable flying of the drone. Once the hand movement is returned as its corresponding degree, we translate these degrees to drone channels that read from 1000-2000 for conversion to Arduino.

#### SerialPortWriter

This is where we communicate to the Arduino! Each hand channel information is sent to the connected Arduino COM port every 30 milliseconds. 

#### LeapMotionDroneControllerV2.ino

Reads the incoming channels values from the serial port and using a PPM signal, sends them through the trainer port on a transmiter.

### Demo

Check out the video below to see it work in action!

https://www.youtube.com/watch?v=Db-agA13ZMc&ab_channel=Crunchy_N00d13FPV

### How to Fly!

#### Step 1: Set transmitter input

#### Step 2: Connect

It's time to connect everything together! You'll need the Leap Motion device and Arduino connected to your computer or laptop by USB port. The Arduino should also connect to your transmitter via a 2pin Screw terminal Jack audio connector. 

#### Step 3: Configure drone

#### Step 4: Run code

Now that everything is set up, simply run the code to get started!

#### Step 5: Fly!

Holding your hands above the Leap Motion device, you can now fly your drone! Make sure you have a safety switch that can turn your propellors off and on. Once the switch is flipped and the propellors start going, use your left hand to take off, controlling the thrust by angling your hand up and down. The right hand controls the drones directions, angle your hand up and down to control pitch (moving the drone forwards/backwards) or angle your hand from side to side to control roll (moving the drone right/left). 

## Contributors:
Matthew Kaye and Saina Koukpari
