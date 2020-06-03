package lv.neotech.homework.ws.controller;

import lv.neotech.homework.ws.json.CountyResponse;
import lv.neotech.homework.ws.json.ErrorResponse;
import lv.neotech.homework.ws.phone.CountryByPhoneDetector;
import lv.neotech.homework.ws.phone.PhoneNumberFormatException;
import lv.neotech.homework.ws.phone.UnableDetectCountryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ws/**")
public class RestPhoneController {

    private static final int UNABLE_TO_DETECT_COUNTRY_CODE = 1;
    private static final int WRONG_PHONE_NUMBER_FORMAT_CODE = 2;
    private static final int UNKNOWN_INTERNAL_ERROR_CODE = 500;

    @Autowired
    CountryByPhoneDetector countryByPhoneDetector;

    @ExceptionHandler(UnableDetectCountryException.class)
    @ResponseStatus(HttpStatus.OK)
    public ErrorResponse handleUnableDetectCountryException(UnableDetectCountryException e) {
        return new ErrorResponse(UNABLE_TO_DETECT_COUNTRY_CODE, e.getMessage());
    }

    @ExceptionHandler(PhoneNumberFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleWrongPhoneNumberException(PhoneNumberFormatException e) {
        return new ErrorResponse(WRONG_PHONE_NUMBER_FORMAT_CODE, e.getMessage());
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse handleOtherExceptions(Exception e) {
        e.printStackTrace();
        return new ErrorResponse(UNKNOWN_INTERNAL_ERROR_CODE,
                "Unknown internal error");
    }

    @RequestMapping(value = "/detectCountry", params = "phone")
    public CountyResponse detect(@RequestParam("phone") String phone) throws PhoneNumberFormatException {
        String country = countryByPhoneDetector.getCountryByPhone(phone);

        if (country == null) {
            throw new UnableDetectCountryException();
        }

        return new CountyResponse(country);
    }

}
