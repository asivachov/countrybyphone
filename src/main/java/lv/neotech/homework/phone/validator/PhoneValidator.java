package lv.neotech.homework.phone.validator;

import lv.neotech.homework.phone.service.PhoneNumberFormatException;
import org.springframework.stereotype.Component;

@Component
public class PhoneValidator {

    private static final String PHONE_IS_EMPTY = "Phone is empty";
    private static final String PHONE_CONTAINS_WRONG_SYMBOLS = "Phone contains wrong symbols";

    public void validate(String phone) throws PhoneNumberFormatException {
        if (phone == null) {
            throw new PhoneNumberFormatException(PHONE_IS_EMPTY);
        }

        phone = phone.trim();

        if (phone.isEmpty()) {
            throw new PhoneNumberFormatException(PHONE_IS_EMPTY);
        }

        if (phone.startsWith("+")) {
            phone = phone.substring(1);
        }

        if (!phone.matches("^[0-9 ]+$")) {
            throw new PhoneNumberFormatException(PHONE_CONTAINS_WRONG_SYMBOLS);
        }
    }

}
