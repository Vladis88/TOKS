package by.bsuir.toks.laba2.serial_api.async_serial_port_pair;

import by.bsuir.toks.laba2.serial_api.async_serial_port_pair.exception.CannotCloseAsyncPortPairException;
import by.bsuir.toks.laba2.serial_api.serial_port.SerialPort;
import by.bsuir.toks.laba2.serial_api.serial_port.exceptions.CannotOpenPortException;

import static by.bsuir.toks.laba2.serial_api.byte_stuffing.ByteStaffingService.doStaffing;


public final class AsyncSerialPortPair {
    public static final int CLIENT = 69; // 01000101
    public static final int SERVER = 13; // 00001101

    private final SerialPort clientPort;
    private final SerialPort serverPort;

    public AsyncSerialPortPair(SerialPort clientPort, SerialPort serverPort) {
        this.clientPort = clientPort;
        this.serverPort = serverPort;
        initialize();
    }

    private void initialize() {
        if (clientPort.open() || serverPort.open())
            throw new CannotOpenPortException();
    }

    public void setBaudRate(int baudRate, int port) {
        if (port == (CLIENT | SERVER)) {
            clientPort.setBaudRate(baudRate);
            serverPort.setBaudRate(baudRate);
        } else {
            SerialPort serialPort = port == CLIENT ? clientPort : serverPort;
            serialPort.setBaudRate(baudRate);
        }
    }

    public void sendMessage(String message, int receiver) {
        SerialPort serialPort = receiver == SERVER ? clientPort : serverPort;
        serialPort.write(doStaffing(message));
    }

    public void close() throws CannotCloseAsyncPortPairException {
        if (clientPort.close() || serverPort.close()) {
            throw new CannotCloseAsyncPortPairException();
        }
    }

    public int getSerialPortBaudRate(int port) {
        return port == CLIENT ? clientPort.getSerialPortBaudRate() :
                serverPort.getSerialPortBaudRate();
    }

    public String getSerialPortName(int port) {
        return port == CLIENT ? clientPort.getSerialPortName() :
                serverPort.getSerialPortName();
    }

}
