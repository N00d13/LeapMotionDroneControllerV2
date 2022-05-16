package main.model;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class SerialPortWrite implements SerialPortEventListener {
    private SerialPort sp;

    public void serialEvent(SerialPortEvent event) {
        //checks if data is available
        if (event.isRXCHAR() && event.getEventValue() > 0) {
            try{
                int bytesCount = event.getEventValue();
                System.out.println("Byte count: ");
                System.out.println(sp.readString(bytesCount));

            } catch (SerialPortException e) {
                System.out.println("Serial Port Exception: " );
                e.printStackTrace();
            }
        }
    }


}
