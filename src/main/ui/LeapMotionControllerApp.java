package main.ui;

import com.leapmotion.leap.Controller;
import jssc.SerialPortList;
import main.model.ControllerListener;
import main.model.SerialPortWriter;

import java.io.IOException;
import java.util.Scanner;

public class LeapMotionControllerApp {
    private ControllerListener listener;
    private Controller controller;
    private Scanner input;

    public LeapMotionControllerApp() {
        runLeapMotionControllerApp();
    }

    private void runLeapMotionControllerApp() {
        init();
        welcomeStatement();
    }

    private void init() {
        input = new Scanner(System.in);

        String port = getPort();

        SerialPortWriter portWriter = new SerialPortWriter(); //Creates Serial Port
        portWriter.connect(port, 38400, 8, 1, 0); //Initializes serial port TODO: Change COM port to correct Arduino COM port

        listener = new ControllerListener(portWriter); //Creates controller listener

        controller = new Controller(); //Sets up controller
        controller.addListener(listener); //adds listener to controller
    }

    private void welcomeStatement() {
        System.out.println("Hello, welcome to the leap motion hand controller app.");
        System.out.println("Connect your leap motion when you are ready for awesomeness");

        if (!controller.isConnected()){
            System.out.println("Your controller isn't connected, please connect your controller or restart the program");
            while ( !controller.isConnected()) {
                // Wait until controller is connected
            }
        }

        runProgram();
    }

    //Runs the program
    private void runProgram(){
        listener.onFrame(controller);
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        controller.removeListener(listener);
    }


    // Gets the port name of the arduino and returns it as a string
    private String getPort(){
        String ourPort = "";
        for (String port : SerialPortList.getPortNames()){
            ourPort = port;
        }
        return ourPort;
    }

}
