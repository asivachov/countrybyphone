package lv.neotech.homework.phone.service;

public class UnableDetectCountryException extends RuntimeException {

    private static final String MESSAGE_TEXT = "Cannot detect country for this phone. Try to check country code.";

    public UnableDetectCountryException() {
        super(MESSAGE_TEXT);
    }
}
