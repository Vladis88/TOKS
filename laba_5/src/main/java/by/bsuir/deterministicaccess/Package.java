package by.bsuir.deterministicaccess;

public class Package {
    private byte control;
    private byte destination;
    private byte status;
    private byte data;

    Package() {
        this.control = 0;
        this.destination = 0;
        this.status = 0;
    }

    public void setControl(byte control) {
        this.control = control;
    }

    public void setDestination(byte destination) {
        this.destination = destination;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public void setData(byte data) {
        this.data = data;
    }

    public byte getControl() {
        return control;
    }

    public byte getDestination() {
        return destination;
    }

    public byte getStatus() {
        return status;
    }

    public byte getData() {
        return data;
    }

    public void freeData() {
        data = 0;
    }
}