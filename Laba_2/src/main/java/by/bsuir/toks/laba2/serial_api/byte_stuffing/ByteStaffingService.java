package by.bsuir.toks.laba2.serial_api.byte_stuffing;

import java.nio.charset.StandardCharsets;

public class ByteStaffingService {
    public static final char FLAG_SEQUENCE = '~';
    public static final char ESCAPE_SEQUENCE = '\\';

    private ByteStaffingService() {
        throw new IllegalStateException("Byte staffing service class");
    }

    public static byte[] doStaffing(String message) {
//        StringBuilder string = new StringBuilder();
        StringBuilder staffedMessage = new StringBuilder();
        message = FLAG_SEQUENCE + message + FLAG_SEQUENCE;

        for (int i = 0; i < message.length(); i++) {
            if (message.charAt(i) == FLAG_SEQUENCE && i != 0 && i != message.length() - 1) {
                staffedMessage.append(new char[]{ESCAPE_SEQUENCE, message.charAt(i)});
            } else if (message.charAt(i) == ESCAPE_SEQUENCE) {
                staffedMessage.append(new char[]{ESCAPE_SEQUENCE, message.charAt(i)});
            } else {
                staffedMessage.append(message.charAt(i));
            }
        }
       /* for (byte i : staffedMessage.toString().getBytes()) {
            string.append(Integer.toBinaryString(i));
        }
        System.out.println("\t\t\t"+string);*/
        return staffedMessage.toString().getBytes(StandardCharsets.UTF_8);
    }

    public static byte[] doUnStaffing(byte[] message) {
        String currentMessage = new String(message, StandardCharsets.UTF_8);
        StringBuilder unStaffedMessage = new StringBuilder();
        int i;
        for (i = 1; i < currentMessage.length() - 1; i++) {
            if (currentMessage.charAt(i) == FLAG_SEQUENCE || currentMessage.charAt(i) != ESCAPE_SEQUENCE) {
                unStaffedMessage.append(currentMessage.charAt(i));
            } else if (currentMessage.charAt(i) == ESCAPE_SEQUENCE && currentMessage.charAt(i + 1) == ESCAPE_SEQUENCE) {
                unStaffedMessage.append(ESCAPE_SEQUENCE);
                i++;
            }
        }

        return unStaffedMessage.toString().getBytes(StandardCharsets.UTF_8);
    }
}
