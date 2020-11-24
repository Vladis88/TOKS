package by.bsuir.toks.laba2.serial_api.serial_port;

import jssc.SerialPortException;

import java.util.Arrays;

import static by.bsuir.toks.laba2.serial_api.byte_stuffing.ByteStaffingService.doUnStaffing;

public class SerialPortImpl implements SerialPort {
    public static final int BAUD_RATE_110 = 110;
    public static final int BAUD_RATE_128000 = 128_000;
    public static final int BAUD_RATE_256000 = 256_000;
    private static final String RECEIVED_MESSAGE ="Message [PORT: %s | BAUD_RATE: %d]: ";
    private static final String STAFFED_RECEIVED_MESSAGE = "Staffed message [PORT: %s | BAUD_RATE: %d]: ";
    private static final String MESSAGE_TEMPLATE = "%s" + "\n";

    private boolean isOpened;
    private final jssc.SerialPort serialPort;
    private int serialPortBaudRate = SerialPortImpl.BAUD_RATE_128000;

    public SerialPortImpl(String serialPortName) {
        serialPort = new jssc.SerialPort(serialPortName);
        isOpened = false;
    }


    @Override
    public String getSerialPortName() {
        return serialPort.getPortName();
    }

    public int getSerialPortBaudRate() {
        return serialPortBaudRate;
    }

    public boolean open() {
        try {
            isOpened = serialPort.openPort();
            serialPort.setFlowControlMode(jssc.SerialPort.FLOWCONTROL_RTSCTS_IN |
                    jssc.SerialPort.FLOWCONTROL_RTSCTS_OUT);
            serialPort.addEventListener(event -> {
                if (event.isRXCHAR() && event.getEventValue() > 0) {
                    try {
                        String receivedMessage = serialPort.readString(event.getEventValue());
                        System.out.printf(STAFFED_RECEIVED_MESSAGE, serialPort.getPortName(), serialPortBaudRate);
                        System.out.printf(MESSAGE_TEMPLATE, receivedMessage);
                        System.out.printf(RECEIVED_MESSAGE, serialPort.getPortName(), serialPortBaudRate);
                        System.out.printf(MESSAGE_TEMPLATE, Arrays.toString(doUnStaffing(receivedMessage.getBytes())));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, jssc.SerialPort.MASK_RXCHAR);
            setBaudRate(serialPortBaudRate);
            return !isOpened;
        } catch (SerialPortException e) {
            e.printStackTrace();
            return true;
        }
    }

    public boolean close() {
        try {
            return !isOpened && !serialPort.closePort();
        } catch (SerialPortException e) {
            e.printStackTrace();
            return true;
        }
    }

    public void setBaudRate(int baudRate) {
        try {
            serialPort.setParams(baudRate,
                    jssc.SerialPort.DATABITS_8,
                    jssc.SerialPort.STOPBITS_1,
                    jssc.SerialPort.PARITY_NONE);
            serialPortBaudRate = baudRate;
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    public void write(byte[] bytesForWrite) {
        try {
            serialPort.writeBytes(bytesForWrite);
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

//        public Optional<byte[]> read(int bytesCount) {
//        try {
//            return Optional.of(serialPort.readBytes(bytesCount));
//        } catch (SerialPortException e) {
//            e.printStackTrace();
//            return Optional.empty();
//        }
//    }

//        public Optional<byte[]> read() {
//        try {
//            return Optional.of(serialPort.readBytes());
//        } catch (SerialPortException e) {
//            e.printStackTrace();
//            return Optional.empty();
//        }
//    }

//    public void setParameters(int baudRate, int dataBits, int stopBits, int parity) {
//        try {
//            serialPort.setParams(baudRate, dataBits, stopBits, parity);
//        } catch (SerialPortException e) {
//            e.printStackTrace();
//        }
//    }
}
