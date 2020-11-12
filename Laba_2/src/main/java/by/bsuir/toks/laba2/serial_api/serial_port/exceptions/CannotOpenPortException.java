package by.bsuir.toks.laba2.serial_api.serial_port.exceptions;


public class CannotOpenPortException extends RuntimeException {
    public CannotOpenPortException() {
        super("Cannot open port pair");
    }
}
