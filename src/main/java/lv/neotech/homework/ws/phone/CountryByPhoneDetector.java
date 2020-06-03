package lv.neotech.homework.ws.phone;

import lv.neotech.homework.ws.validator.PhoneValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class CountryByPhoneDetector {

    private static final String DELIMITER_FOR_MULTIPLE_COUNTRIES = " / ";
    private static final int MAX_CODE_LENGTH_BASED_ON_WIKI = 10;
    @Autowired
    PhoneValidator phoneValidator;
    @Autowired
    private WikiPhoneCountryCodeDataSource phoneCountryCodeDataSource;
    private Map<String, List<String>> countryByPhoneCodeMap;

    @PostConstruct
    private void loadData() throws IOException {
        countryByPhoneCodeMap = phoneCountryCodeDataSource.getCodeMap();
    }

    public String getCountryByPhone(String phone) throws PhoneNumberFormatException {
        phoneValidator.validate(phone);

        phone = phone.trim();

        String country = null;
        phone = removeExtraChars(phone);
        int codeLength = calcMaxCodeLength(phone);

        while ((codeLength > 0) && (country == null)) {
            String codeToCheck = phone.substring(0, codeLength);
            country = getCountryByCode(codeToCheck);

            codeLength--;
        }

        return country;
    }

    private String removeExtraChars(String phone) {
        phone = phone.replaceAll("[+ ]", "");
        return removeLeadingZeroes(phone);
    }

    private String removeLeadingZeroes(String phone) {
        if (phone.charAt(0) == '0') {
            phone = phone.substring(1);
            if (phone.charAt(0) == '0') {
                phone = phone.substring(1);
            }
        }
        return phone;
    }

    private int calcMaxCodeLength(String phone) {
        phone = removeExtraChars(phone);
        return (phone.length() > 10) ? MAX_CODE_LENGTH_BASED_ON_WIKI : phone.length() - 1;
    }

    private String getCountryByCode(String code) {
        List<String> countries = countryByPhoneCodeMap.get(code);

        return (countries == null) ? null : String.join(DELIMITER_FOR_MULTIPLE_COUNTRIES, countries);
    }

}
