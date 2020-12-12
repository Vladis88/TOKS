package by.bsuir.toks.laba2.runner;

import by.bsuir.toks.laba2.serial_api.async_serial_port_pair.AsyncSerialPortPair;
import by.bsuir.toks.laba2.serial_api.serial_port.SerialPortImpl;

public class Main {
    public static void main(String[] args) {
        ControllerApp controllerApp = new ControllerApp(new AsyncSerialPortPair(new SerialPortImpl("COM1"),
                new SerialPortImpl("COM2")));
        try {
            controllerApp.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }

        System.out.println("Program was successfully finished");

        System.exit(0);
    }
}
