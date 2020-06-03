package lv.neotech.homework.ws.phone;

public class PhoneNumberFormatException extends RuntimeException {

    private static final String MESSAGE_TEXT = "Wrong format. Only numbers, spaces and leading + are allowed.";

    public PhoneNumberFormatException() {
        super(MESSAGE_TEXT);
    }
}
