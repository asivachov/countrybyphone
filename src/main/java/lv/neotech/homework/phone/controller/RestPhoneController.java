package lv.neotech.homework.phone.controller;

import lv.neotech.homework.phone.dto.CountyResponseDto;
import lv.neotech.homework.phone.dto.ErrorResponseDto;
import lv.neotech.homework.phone.service.CountryByPhoneDetector;
import lv.neotech.homework.phone.service.PhoneNumberFormatException;
import lv.neotech.homework.phone.service.UnableDetectCountryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/**")
public class RestPhoneController {

    private static final int UNABLE_TO_DETECT_COUNTRY_CODE = 1;
    private static final int WRONG_PHONE_NUMBER_FORMAT_CODE = 2;
    private static final int UNKNOWN_INTERNAL_ERROR_CODE = 500;

    private final CountryByPhoneDetector countryByPhoneDetector;

    private static final Logger LOG = LoggerFactory.getLogger(RestPhoneController.class);

    @Autowired
    public RestPhoneController(CountryByPhoneDetector countryByPhoneDetector) {
        this.countryByPhoneDetector = countryByPhoneDetector;
    }

    @RequestMapping(value = "/detectCountry", params = "phone")
    public CountyResponseDto detect(@RequestParam("phone") String phone) throws PhoneNumberFormatException {
        String country = countryByPhoneDetector.getCountryByPhone(phone);

        if (country.isEmpty()) {
            throw new UnableDetectCountryException();
        }

        return new CountyResponseDto(country);
    }

    @ExceptionHandler(UnableDetectCountryException.class)
    @ResponseStatus(HttpStatus.OK)
    public ErrorResponseDto handleUnableDetectCountryException(UnableDetectCountryException e) {
        return new ErrorResponseDto(UNABLE_TO_DETECT_COUNTRY_CODE, e.getMessage());
    }

    @ExceptionHandler(PhoneNumberFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleWrongPhoneNumberException(PhoneNumberFormatException e) {
        return new ErrorResponseDto(WRONG_PHONE_NUMBER_FORMAT_CODE, e.getMessage());
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponseDto handleOtherExceptions(Exception e) {
        LOG.error("Unexpected error occurred", e);
        return new ErrorResponseDto(UNKNOWN_INTERNAL_ERROR_CODE,
                "Unknown internal error");
    }

}
