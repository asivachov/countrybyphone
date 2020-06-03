package lv.neotech.homework.ws.phone;

public class UnableDetectCountryException extends RuntimeException {

    private static final String MESSAGE_TEXT = "Cannot detect country for this phone. Try to check it's country code.";

    public UnableDetectCountryException() {
        super(MESSAGE_TEXT);
    }
}
