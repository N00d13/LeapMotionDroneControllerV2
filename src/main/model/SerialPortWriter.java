package main.model;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class SerialPortWriter implements SerialPortEventListener {
    private SerialPort sp;

    //called every time new information received from port and print to user's screen
    public void serialEvent(SerialPortEvent event) {
        //checks if data is available
        if (event.isRXCHAR() && event.getEventValue() > 0) {
            try{
                int bytesCount = event.getEventValue();
                System.out.println("Byte count: ");
                System.out.println(sp.readString(bytesCount));

            } catch (SerialPortException e) {
                System.out.println("SerialEvent Port Exception: " );
                e.printStackTrace();
            }
        }
    }

    //Connects and sets up new COM port
    public void connect(String ComAddress, int baudRate, int dataBits, int stopBits, int parity) {
        try {
            sp = new SerialPort(ComAddress); //Sets up new serial port on correct COM port
            sp.openPort(); //Opens port
            sp.setParams(baudRate, dataBits, stopBits, parity);
            sp.addEventListener(new SerialPortWriter());

        } catch (SerialPortException e) {
            System.out.println("Connect Port Exception: ");
            e.printStackTrace();
        }
    }

    //Disconnects COM port after not in use
    public void disconnect() {
        try {
            sp.closePort();
        } catch (SerialPortException e) {
            System.out.println("Disconnect Port Exception: ");
            e.printStackTrace();
        }
    }

    //Write string text to serial port
    public void write(String text) {
        try {
            sp.writeBytes(text.getBytes());
        } catch (SerialPortException e) {
            System.out.println("Write Port Exception: ");
            e.printStackTrace();
        }
    }


}
