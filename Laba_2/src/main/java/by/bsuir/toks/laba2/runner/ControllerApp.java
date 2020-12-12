package by.bsuir.toks.laba2.runner;

import by.bsuir.toks.laba2.serial_api.async_serial_port_pair.AsyncSerialPortPair;
import by.bsuir.toks.laba2.serial_api.async_serial_port_pair.exception.CannotCloseAsyncPortPairException;
import by.bsuir.toks.laba2.serial_api.serial_port.SerialPortImpl;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ControllerApp {
    private static final String USER_MENU = "\t\t\tMENU\n1 - Send message to server\n"
            + "2 - Send message to client\n"
            + "3 - Set up minimal baud rate\n"
            + "4 - Set up maximal baud rate\n"
            + "5 - Exit\n";
    private static final String INPUT_MENU_OPTION_INVITATION = "-> ";

    private static final String INPUT_MESSAGE_INVITATION = "Enter message [PORT: %s | BAUD_RATE: %d]: ";

    private static final String MINIMAL_BAUD_RATE_WAS_SUCCESSFULLY_INSTALLED = "Minimal baud rate was successfully installed";

    private static final String MAXIMAL_BAUD_RATE_WAS_SUCCESSFULLY_INSTALLED = "Maximal baud rate was successfully installed";

    private static final String WRONG_INPUT = "\tWrong input";

    private final AsyncSerialPortPair portPair;

    public ControllerApp(AsyncSerialPortPair portPair) {
        this.portPair = portPair;
    }

    public void start() {
        boolean isItTimeForQuit = false;
        Scanner scanner = new Scanner(System.in, "utf-8");
        while (!isItTimeForQuit) {
            System.out.print(USER_MENU);
            System.out.print(INPUT_MENU_OPTION_INVITATION);
            if (!scanner.hasNext("[1-5]")) {
                System.out.println(WRONG_INPUT);
                scanner.next();
                continue;
            }
            try {
                switch (scanner.nextInt()) {
                    case 1:
                        System.out.printf(INPUT_MESSAGE_INVITATION,
                                portPair.getSerialPortName(AsyncSerialPortPair.CLIENT),
                                portPair.getSerialPortBaudRate(AsyncSerialPortPair.CLIENT));

                        // you can add async computation using runAsync method from CompletableFuture class
                        portPair.sendMessage(scanner.next(), AsyncSerialPortPair.SERVER);
                        TimeUnit.MILLISECONDS.sleep(10);
                        break;
                    case 2:
                        System.out.printf(INPUT_MESSAGE_INVITATION,
                                portPair.getSerialPortName(AsyncSerialPortPair.SERVER),
                                portPair.getSerialPortBaudRate(AsyncSerialPortPair.SERVER));
                        portPair.sendMessage(scanner.next(), AsyncSerialPortPair.CLIENT);
                        TimeUnit.MILLISECONDS.sleep(10);
                        break;
                    case 3:
                        portPair.setBaudRate(SerialPortImpl.BAUD_RATE_110,
                                AsyncSerialPortPair.CLIENT | AsyncSerialPortPair.SERVER);
                        System.out.println(MINIMAL_BAUD_RATE_WAS_SUCCESSFULLY_INSTALLED);
                        break;
                    case 4:
                        portPair.setBaudRate(SerialPortImpl.BAUD_RATE_256000,
                                AsyncSerialPortPair.CLIENT | AsyncSerialPortPair.SERVER);
                        System.out.println(MAXIMAL_BAUD_RATE_WAS_SUCCESSFULLY_INSTALLED);
                        break;
                    case 5:
                        isItTimeForQuit = true;
                        break;
                    default:
                        System.out.println(WRONG_INPUT);
                        break;
                }
                portPair.close();
            } catch (InterruptedException | CannotCloseAsyncPortPairException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }
}
