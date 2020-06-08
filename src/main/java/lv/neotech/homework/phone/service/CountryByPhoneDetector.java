package lv.neotech.homework.phone.service;

import lv.neotech.homework.phone.validator.PhoneValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class CountryByPhoneDetector {

    private static final String DELIMITER_FOR_MULTIPLE_COUNTRIES = " / ";
    private static final int MAX_CODE_LENGTH_BASED_ON_WIKI = 10;
    private final PhoneValidator phoneValidator;
    private final WikiPhoneCodesDataService phoneCodesDataService;
    private Map<String, List<String>> countryByPhoneCodeMap;

    @Autowired
    public CountryByPhoneDetector(PhoneValidator phoneValidator, WikiPhoneCodesDataService phoneCodesDataService) {
        this.phoneValidator = phoneValidator;
        this.phoneCodesDataService = phoneCodesDataService;
    }

    @PostConstruct
    private void loadData() throws IOException {
        countryByPhoneCodeMap = phoneCodesDataService.getCodeMap();
    }

    public String detect(String phone) throws PhoneNumberFormatException {
        String country = "";

        phoneValidator.validate(phone);
        phone = stripCharsForCountrySearch(phone);

        int codeLength = calcMaxCodeLength(phone);

        while ((codeLength > 0) && (country.isEmpty())) {
            String codeToCheck = phone.substring(0, codeLength);
            country = getCountryByCode(codeToCheck);

            codeLength--;
        }

        return country;
    }

    private String stripCharsForCountrySearch(String phone) {
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
        phone = stripCharsForCountrySearch(phone);
        return (phone.length() > 10) ? MAX_CODE_LENGTH_BASED_ON_WIKI : phone.length() - 1;
    }

    private String getCountryByCode(String code) {
        List<String> countries = countryByPhoneCodeMap.get(code);

        return (countries == null) ? "" : String.join(DELIMITER_FOR_MULTIPLE_COUNTRIES, countries);
    }

}
