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

Make sure that your transmitter is running OpenTx and has a trainer port using a 3.5mm jack input. Check your channel configurations in your drone software but for this example we will use throttle, aileron, elevator and rudder for channels 1, 2, 3 and 4 respectively.

This program modifies the trainer port channel levels so we need to change the input for the throttle from the thumbsticks to it's respective trainer channel. 
Follow these steps:
  1. On your home screen press the "Menu" button.
  2. Press the "Page" button until you land on the "INPUTS" page. The page name is located on the top left on your transmitter screen.
  3. Long click the first channel and then click "Edit".
  4. Scroll down to "Source" and click the current source
  5. Scroll until you reach "TR1". This means trainer port channel 1.
  6. Click "TR1" to confirm your selection.
  7. Click your "Exit" button until your reach the "INPUTS" page again.
  8. Start again from step 3 for channel 2 and 3 but instead of "TR1", use "TR2" for channel 2 and "TR3" for channel 3.
  9. Channel 4 is supposed to be for yaw but we found that our drone flew better without it. If you would like to use yaw, go to step 10, otherwise skip to step 11.
  10. In our code, open the ControllerListener class and scroll down to the "AssignChannelValues" function. Change the "1500" value to the line commented out. Start from step 3 again but use "TR4" instead of "TR1" for the source. Once completed, skip to step 12.
  11. We found that using "TR4" for our yaw source caused the drone to slowly spin. To avoid this, change the yaw channel source to a random channel that is not in use, we chose channel 12. Start again from step 3 but use "CH12" (or your chosen channel) instead of "TR1".
  12. We found our elevator (channel 3) to be the opposite of our input. If you experience this, edit your elevator channel (channel 3) and change the "Weight" from "100" to "-100".
  13. Your transmitter inputs are now set up!

#### Step 2: Connect

It's time to connect everything together! Connect your Leap Motion device to your computer with a usb and download the drivers needed here:

https://developer.leapmotion.com/tracking-software-download
Note: If the link goes down, search "Leap Motion Driver" and you will be able to find the newly released one.

Purchase a "3.5mm mono male to 2 pin screw terminal" and connect the Male jack to your transmitter's trainer port. On the 2 pin screw terminal, connect the positive to pin 10 and negative to ground on your Arduino. Finally connect your arduino to your computer with a USB

The "3.5mm mono male to 2 pin screw terminal" can be bought here: https://www.amazon.ca/Poyiccot-Terminal-Headphone-Solderless-Converter/dp/B07PPPX53X/ref=sr_1_5?crid=KPHA68991PJ&keywords=mono+male+to+2+pin&qid=1662858327&sprefix=mono+male+to+2+pin%2Caps%2C110&sr=8-5

#### Step 3: Configure drone

Setup your drone with Betaflight (or other drone firmware) and keep angle mode on when flying with this program. Finally, connect your transmitter and drone receiver.

#### Step 4: Run code

Now that everything is set up, simply run the Main class in src to get started!

#### Step 5: Flying Controls!

Holding your hands above the Leap Motion device, you can now fly your drone! Make sure you have a safety switch that can turn your propellors off and on. Once the switch is flipped and the propellors start going, use your left hand to take off, controlling the thrust by angling your hand up and down. The right hand controls the drones directions, angle your hand up and down to control pitch (moving the drone forwards/backwards) or angle your hand from side to side to control roll (moving the drone right/left). 

## Contributors:
Matthew Kaye and Saina Koukpari
