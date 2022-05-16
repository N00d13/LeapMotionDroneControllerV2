package main.ui;

import com.leapmotion.leap.Controller;
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
        SerialPortWriter portWriter = new SerialPortWriter(); //Creates Serial Port
        portWriter.connect("COM6", 38400, 8, 1, 0); //Initializes serial port TODO: Change COM port to correct Arduino COM port

        listener = new ControllerListener(portWriter); //Creates controller listener

        controller = new Controller(); //Sets up controller
        controller.addListener(listener); //adds listener to controller
    }

    private void welcomeStatement() {
        System.out.println("Hello, welcome to the leap motion hand controller app.");
        System.out.println("Connect your leap motion when you are ready for awesomeness");
        String nextInput = "";
        while (!controller.isConnected() && nextInput != "s") {
            if (controller.isConnected()) {
                System.out.println("press \"s\" to start the program");
                nextInput = input.nextLine();
            } else {
                System.out.println("Your controller isn't connected, press \"s\" when it is.");
                nextInput = input.nextLine();
            }
        }
        listener.onConnect(controller);

        System.out.println("Program is starting...");
        runProgram();
    }

    private void runProgram(){
        listener.onFrame(controller);
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        controller.removeListener(listener);
    }

}
