package by.bsuir.toks.lab1.viewserialport;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class MyPort {
    private final SerialPort port;
    private boolean opened;
    private int portBaudRate = SerialPort.BAUDRATE_9600;

    public MyPort(String namePort) {
        port = new SerialPort(namePort);
        opened = false;
    }

    public void write(byte[] bytes) {
        try {
            port.writeBytes(bytes);
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    public boolean isOpen() {
        try {
            opened = port.openPort();
            //настраиваем мониторинг за определенными событиями порта
            port.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |
                    SerialPort.FLOWCONTROL_RTSCTS_OUT);
            port.addEventListener(new PortReader(), SerialPort.MASK_RXCHAR);
            setBaudRate(portBaudRate);
            return !opened;
        } catch (SerialPortException e) {
            e.printStackTrace();
            return true;
        }
    }

    public void close() {
        try {
            if (opened) {
                port.closePort();
            }
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    public void setBaudRate(int baudRate) {
        setParams(baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
        portBaudRate = baudRate;
    }

    public void setParams(int baudRate, int dataBits, int stopBits, int parity) {
        try {
            port.setParams(baudRate, dataBits, stopBits, parity);
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    public String getPortBaudRate() {
        return String.valueOf(portBaudRate);
    }

    public String getPortName() {
        return port.getPortName();
    }

    private class PortReader implements SerialPortEventListener {

        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    String receivedData = port.readString(event.getEventValue());
                    System.out.println("\tReceived response: " + receivedData);
                } catch (SerialPortException ex) {
                    System.out.println("Error in receiving string from COM-port: " + ex);
                }
            }
        }
    }

}