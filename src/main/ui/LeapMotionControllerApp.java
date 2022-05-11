package main.ui;

import com.leapmotion.leap.Controller;
import main.model.ControllerListener;
import java.io.IOException;
import java.util.Scanner;

public class LeapMotionControllerApp {
    private Scanner input;
    private ControllerListener listener;
    private Controller controller;

    public LeapMotionControllerApp() {
        runLeapMotionControllerApp();
    }

    private void runLeapMotionControllerApp() {
        init();
        welcomeStatement();
    }

    private void init() {
        input = new Scanner(System.in);
        listener = new ControllerListener();
        controller = new Controller();
        controller.addListener(listener);
    }

    private void welcomeStatement() {
        System.out.println("Hello, welcome to the leap motion hand controller app.");
        System.out.println("Connect your leap motion when you are ready and then press \"s\"");
        String nextInput = input.nextLine();
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
