package lv.neotech.homework.phone.service;

public class PhoneNumberFormatException extends RuntimeException {

    private static final String MESSAGE_TEXT = "Wrong format: %s. Only numbers, spaces and leading + are allowed.";

    public PhoneNumberFormatException(String error) {
        super(String.format(MESSAGE_TEXT, error));
    }
}
