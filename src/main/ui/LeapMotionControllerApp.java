package main.ui;

import com.leapmotion.leap.Controller;
import jssc.SerialPortList;
import main.model.ControllerListener;
import main.model.SerialPortWriter;
import java.io.IOException;

// LeapMotion hand controller application.
public class LeapMotionControllerApp {
    private ControllerListener listener; // Listener object for controller object.
    private Controller controller; // LeapMotion controller object.

    // EFFECT: Runs the application.
    public LeapMotionControllerApp() {
        runLeapMotionControllerApp();
    }

    // EFFECT: Initializes application and states welcome statement.
    private void runLeapMotionControllerApp() {
        init();
        welcomeStatement();
    }

    // MODIFIES: this.
    // EFFECT: Sets up the listener and controller object with the correct serial COM port.
    private void init() {
        String port = getPort(); // Find current connected port to use

        SerialPortWriter portWriter = new SerialPortWriter();
        portWriter.connect(port, 38400, 8, 1, 0);

        listener = new ControllerListener(portWriter);

        controller = new Controller();
        controller.addListener(listener);
    }

    // EFFECT: States the welcome statements and waits for the controller to be connected.
    private void welcomeStatement() {
        System.out.println("Hello, welcome to the leap motion hand controller app.");
        System.out.println("Connect your leap motion when you are ready for awesomeness");

        if (!controller.isConnected()){
            System.out.println("Your controller isn't connected, please connect your controller or restart the program");
            while ( !controller.isConnected()) {
                // Polling until controller is connected
            }
        }
        runProgram();
    }

    // EFFECT: Runs the program to read information from the controller.
    private void runProgram(){
        listener.onFrame(controller);
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        controller.removeListener(listener);
    }

    // REQUIRES: Arduino controller has to be connected.
    // EFFECT: Returns the port name associated with the Arduino COM port.
    private String getPort(){
        String ourPort = "";
        for (String port : SerialPortList.getPortNames()){
            ourPort = port;
        }
        return ourPort;
    }
}
