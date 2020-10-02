package by.bsuir.toks.lab1.main;

import by.bsuir.toks.lab1.service.Controller;
import by.bsuir.toks.lab1.viewserialport.MyPort;

public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller(new MyPort("COM1"), new MyPort("COM2"));
        controller.start();
    }
}
