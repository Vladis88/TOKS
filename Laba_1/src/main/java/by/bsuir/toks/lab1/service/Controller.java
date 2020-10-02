package by.bsuir.toks.lab1.service;

import by.bsuir.toks.lab1.viewserialport.MyPort;
import jssc.SerialPort;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Controller {
    private final MyPort port1;
    private final MyPort port2;

    public Controller(MyPort com1, MyPort com2) {
        this.port1 = com1;
        this.port2 = com2;
        init();
    }

    private void init() {
        if (port1.isOpen() || port2.isOpen()) {
            System.out.println("Cannot open port pair!");
            System.exit(-1);
        } else {
            System.out.println("Ports opened successfully!");
        }
    }

    public void start() {
        boolean exit = false;
        Scanner scanner = new Scanner(System.in);
        while (!exit) {
            System.out.println("\n\t\t\tMENU:\nSend Message To Port1 - 1\nSend Message To Port2 - 2\n" +
                    "Set Up Minimal Baud Rate - 3\nSet Up Maximal Baud Rate - 4\n" +
                    "Exit - 5");
            int choice = 0;
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Wrong input!");
                scanner.next();
            }
            switch (choice) {
                case 1:
                    System.out.println("Enter message (port: " + port1.getPortName() + ", baud rate: " + port1.getPortBaudRate() + ")");
                    String inputMsg = scanner.next();
                    port1.write(inputMsg.getBytes());
                    break;
                case 2:
                    System.out.println("Enter message (port: " + port2.getPortName() + ", baud rate: " + port2.getPortBaudRate() + ")");
                    port2.write(scanner.next().getBytes());
                    break;
                case 3:
                    port1.setBaudRate(SerialPort.BAUDRATE_110);
                    port2.setBaudRate(SerialPort.BAUDRATE_110);
                    System.out.println("The minimum baud rate was set!");
                    break;
                case 4:
                    port1.setBaudRate(SerialPort.BAUDRATE_256000);
                    port2.setBaudRate(SerialPort.BAUDRATE_256000);
                    System.out.println("The maximum baud rate was set!");
                    break;
                case 5:
                    exit = true;
                    port1.close();
                    port2.close();
                    break;
                default:
                    System.out.println("Wrong input choice!");
                    break;
            }
        }

    }
}