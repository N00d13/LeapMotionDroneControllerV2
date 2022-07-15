package main.model;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

// Sends hand channel information to the Arduino COM port whenever updated.
public class SerialPortWriter implements SerialPortEventListener {
    private SerialPort sp; // Port writer object for Arduino COM port

    // EFFECT: If new information is received in the serial COM port then prints the byte count.
    public void serialEvent(SerialPortEvent event) {
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

    // MODIFIES: this.
    // EFFECT: Initializes the serial COM port.
    public void connect(String ComAddress, int baudRate, int dataBits, int stopBits, int parity) {
        try {
            sp = new SerialPort(ComAddress);
            sp.openPort();
            sp.setParams(baudRate, dataBits, stopBits, parity);
            sp.addEventListener(new SerialPortWriter());

        } catch (SerialPortException e) {
            System.out.println("Connect Port Exception: ");
            e.printStackTrace();
        }
    }

    // MODIFIES: this
    // EFFECT: Disconnects the serial COM port when not in use.
    //Disconnects COM port after not in use
    public void disconnect() {
        try {
            sp.closePort();
        } catch (SerialPortException e) {
            System.out.println("Disconnect Port Exception: ");
            e.printStackTrace();
        }
    }

    // MODIFIES: this
    // EFFECT: Writes information to serial COM port
    public void write(String text) {
        try {
            sp.writeBytes(text.getBytes());
        } catch (SerialPortException e) {
            System.out.println("Write Port Exception: ");
            e.printStackTrace();
        }
    }


}
