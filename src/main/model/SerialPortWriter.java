package main.model;

import java.io.IOException;
import com.fazecast.jSerialComm.SerialPort;

public class SerialPortWriter {
    private SerialPort sp;

    public SerialPortWriter() {
        init();
    }

    private void init() {

        sp = SerialPort.getCommPort("COM6"); // port name TODO: must be changed
        sp.setComPortParameters(9600, 8, 1, 0); // default connection settings for Arduino
        sp.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0); // block until bytes can be written

        if (sp.openPort()) {
            System.out.println("Port is open");
        } else {
            System.out.println("Port failed to open");
            return;
        }
    }

    public void WriteToPort(int inputValue, String channelName) throws IOException{
        //int streamPlaceholder = GetStreamPlaceholder(channelName);
        //sp.getOutputStream().write(streamPlaceholder);
        //sp.getOutputStream().flush();
        //if (inputValue < 100){
        //  }
        sp.getOutputStream().write(inputValue);
        sp.getOutputStream().flush();
        System.out.println("Really Wrote to Port" + inputValue);
    }

    private int GetStreamPlaceholder (String channelName) {
        if (channelName == "pitch") {
            return -127;
        } else if (channelName == "roll") {
            return -126;
        } else if (channelName == "yaw") {
            return 126;
        } else if (channelName == "thrust") {
            return 127;
        } else {
            return 0; //Stub
        }
    }
}
