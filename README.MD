# Leap Motion Hand Drone Controller

## Project Description

### Intro

Our software was created to read hand muscle movement from a LeapMotion Device and be able to fly a drone according to that information.

### How it works!

In order to communicate our hand movements to the drone reciever, we sent information through an arduino device to the drone transmitter to change its ppm. Effectively the hand movements are interpreted in a manner that changes the channels directly on the transmitter which can communicate to the drones reciver. 

#### ControllerListener

Here, we open the LeapMotion controller object and read the information sent by LeapMotionTX object.

#### LeapMotionTX

Once the hands are detected by the LeapMotion device, each movement is interpreted as thrust, pitch, roll and yaw. These motions are returned as degrees, between -45 to 45 for pitch, roll and yaw, and 0 to 90 for thrust. We set these bounds in order to allow for accurate and stable flying of the drone. Once the hand movement is returned as its corresponding degree, we translate these degrees to drone channels that read from 1000-2000 for conversion to Arduino.

#### SerialPortWriter

This is where we communicate to the Arduino! Each hand channel information is sent to the connected Arduino COM port whenever updated. 

#### LeapMotionDroneControllerV2.ino

The last step is to send the PPM from information read through the serial COM port. 

### Demo

Check out the video below to see it work in action!

(input video)

### How to Fly!

#### Step 1: Set transmitter input

#### Step 2: Connect

It's time to connect everything together! You'll need the leapmotion device and arduino connected to your computer or laptop by USB port. The arduino should also connect to your transmitter via a 2pin Screw terminal Jack audio connector. 

#### Step 3: Configure drone

#### Step 4: Run code

Now that everything is set up, simply run the code to get started!

#### Step 5: Fly!

Holding your hands above the leapmotion device, you can now fly your drone! Make sure you have a safety switch that can turn your propellors off and on. Once the switch is flipped and the propellors start going, use your left hand to take off, controlling the thrust by angling your hand up and down. The right hand controls the drones directions, angle your hand up and down to control pitch (moving the drone forwards/backwards) or angle your hand from side to side to control roll (moving the drone right/left). 

## Contributors:
Matthew Kaye and Saina Koukpari
