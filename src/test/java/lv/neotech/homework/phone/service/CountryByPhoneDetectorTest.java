package lv.neotech.homework.phone.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CountryByPhoneDetectorTest {

    @Autowired
    CountryByPhoneDetector countryByPhoneDetector;

    @Test
    public void testIfEmptyReturnedForMultipleZeroes() {
        String country = countryByPhoneDetector.detect("000000000000");

        assertEquals("", country);
    }

    @Test
    public void testIfEmptyReturnedForNonExistingCountryCode() {
        String country = countryByPhoneDetector.detect("+21429648790");

        assertEquals("", country);
    }

    @Test
    public void testIfRightValueReturnedForLatvianPhoneWithoutPlus() {
        String country = countryByPhoneDetector.detect("37129648790");

        assertEquals(country.toLowerCase(), "latvia");
    }

    @Test
    public void testIfRightValueReturnedForLatvianPhoneWithPlus() {
        String country = countryByPhoneDetector.detect("+37129648790");

        assertEquals(country.toLowerCase(), "latvia");
    }

    @Test
    public void testIfRightValueReturnedForLatvianPhoneWithSingleZero() {
        String country = countryByPhoneDetector.detect("037129648790");

        assertEquals(country.toLowerCase(), "latvia");
    }

    @Test
    public void testIfRightValueReturnedForLatvianPhoneWithTwoZeroes() {
        String country = countryByPhoneDetector.detect("0037129648790");

        assertEquals(country.toLowerCase(), "latvia");
    }

    @Test
    public void testIfRightValueReturnedForLatvianPhoneWithSpaces() {
        String country = countryByPhoneDetector.detect("+ 371 2 9 648 790");

        assertEquals(country.toLowerCase(), "latvia");
    }

    @Test
    public void testIfRightValueReturnedForShortLatvianNumber() {
        String country = countryByPhoneDetector.detect("+371296");

        assertEquals(country.toLowerCase(), "latvia");
    }

    @Test
    public void testIfRightValueReturnedForShortLatvianNumberWithManyExtraSymbols() {
        String country = countryByPhoneDetector.detect("+371            296");

        assertEquals(country.toLowerCase(), "latvia");
    }

    @Test
    public void testIfRightMultipleValuesReturnedForCnAndSePhone() {
        String country = countryByPhoneDetector.detect("+599 32329648790");

        assertEquals(country.toLowerCase(), "caribbean netherlands / sint eustatius");
    }

    @Test
    public void testIfBahamasNotOverlayedWithUsAndCa() {
        String country = countryByPhoneDetector.detect("+1242329648790");

        assertEquals(country.toLowerCase(), "bahamas");
    }

    @Test
    public void testIfRightValueReturnedForUsAndCanada() {
        String country = countryByPhoneDetector.detect("+1800500");

        assertEquals(country.toLowerCase(), "canada / united states");
    }

    @Test(expected = PhoneNumberFormatException.class)
    public void testWrongPhoneNumberExceptionForNull() {
        countryByPhoneDetector.detect(null);
    }

    @Test(expected = PhoneNumberFormatException.class)
    public void testWrongPhoneNumberExceptionForEmpty() {
        countryByPhoneDetector.detect("");
    }

    @Test(expected = PhoneNumberFormatException.class)
    public void testWrongPhoneNumberExceptionForMultipleSpaces() {
        countryByPhoneDetector.detect("          ");
    }

    @Test(expected = PhoneNumberFormatException.class)
    public void testWrongPhoneNumberExceptionForLetter() {
        countryByPhoneDetector.detect("+3712964879A");
    }

    @Test(expected = PhoneNumberFormatException.class)
    public void testWrongPhoneNumberExceptionForMultiplePluses() {
        countryByPhoneDetector.detect("++3712964879");
    }

}
