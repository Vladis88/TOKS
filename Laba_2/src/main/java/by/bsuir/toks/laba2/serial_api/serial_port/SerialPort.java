package by.bsuir.toks.laba2.serial_api.serial_port;

//import java.util.Optional;

public interface SerialPort {

//    default int[] getAllBaudRates() {
//        return new int[] { 110, 300, 600,
//                1_200, 4_800, 9_600,
//                14_400, 19_200, 38_400, 57_600,
//                115_200, 128_000, 256_000
//        };
//    }

    String getSerialPortName();
    int getSerialPortBaudRate();

//    Optional<byte[]> read();
//    Optional<byte[]> read(int bytesCount);
    void write(byte[] bytesForWrite);
    boolean open();
    boolean close();
    void setBaudRate(int baudRate);
//    void setParameters(int baudRate, int dataBits, int stopBits, int parity);
}
