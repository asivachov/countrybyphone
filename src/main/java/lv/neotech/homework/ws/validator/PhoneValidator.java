package lv.neotech.homework.ws.validator;

import lv.neotech.homework.ws.phone.PhoneNumberFormatException;
import org.springframework.stereotype.Component;

@Component
public class PhoneValidator {

    public void validate(String phone) throws PhoneNumberFormatException {
        if (phone == null) {
            throw new PhoneNumberFormatException();
        }

        phone = phone.trim();

        if (phone.isEmpty()) {
            throw new PhoneNumberFormatException();
        }

        if (phone.startsWith("+")) {
            phone = phone.substring(1);
        }

        if (!phone.matches("^[0-9 ]+$")) {
            throw new PhoneNumberFormatException();
        }
    }

}
