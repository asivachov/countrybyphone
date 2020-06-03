package lv.neotech.homework.ws.phone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CountryByPhoneDetectorTest {

    @Autowired
    CountryByPhoneDetector countryByPhoneDetector;

    @Test
    public void testIfNullReturnedForMultipleZeroes() {
        String country = countryByPhoneDetector.getCountryByPhone("000000000000");

        assertNull(country);
    }

    @Test
    public void testIfNullReturnedForUnexistingCountryCode() {
        String country = countryByPhoneDetector.getCountryByPhone("+21429648790");

        assertNull(country);
    }

    @Test
    public void testIfRightValueReturnedForLatvianPhoneWithoutPlus() {
        String country = countryByPhoneDetector.getCountryByPhone("37129648790");

        assertEquals(country.toLowerCase(), "latvia");
    }

    @Test
    public void testIfRightValueReturnedForLatvianPhoneWithPlus() {
        String country = countryByPhoneDetector.getCountryByPhone("+37129648790");

        assertEquals(country.toLowerCase(), "latvia");
    }

    @Test
    public void testIfRightValueReturnedForLatvianPhoneWithSingleZero() {
        String country = countryByPhoneDetector.getCountryByPhone("037129648790");

        assertEquals(country.toLowerCase(), "latvia");
    }

    @Test
    public void testIfRightValueReturnedForLatvianPhoneWithTwoZeroes() {
        String country = countryByPhoneDetector.getCountryByPhone("0037129648790");

        assertEquals(country.toLowerCase(), "latvia");
    }

    @Test
    public void testIfRightValueReturnedForLatvianPhoneWithSpaces() {
        String country = countryByPhoneDetector.getCountryByPhone("+ 371 2 9 648 790");

        assertEquals(country.toLowerCase(), "latvia");
    }

    @Test
    public void testIfRightValueReturnedForShortLatvianNumber() {
        String country = countryByPhoneDetector.getCountryByPhone("+371296");

        assertEquals(country.toLowerCase(), "latvia");
    }

    @Test
    public void testIfRightValueReturnedForShortLatvianNumberWithManyExtraSymbols() {
        String country = countryByPhoneDetector.getCountryByPhone("+371            296");

        assertEquals(country.toLowerCase(), "latvia");
    }

    @Test
    public void testIfRightMultipleValuesReturnedForCnAndSePhone() {
        String country = countryByPhoneDetector.getCountryByPhone("+599 32329648790");

        assertEquals(country.toLowerCase(), "caribbean netherlands / sint eustatius");
    }

    @Test
    public void testIfBahamasNotOverlayedWithUsAndCa() {
        String country = countryByPhoneDetector.getCountryByPhone("+1242329648790");

        assertEquals(country.toLowerCase(), "bahamas");
    }

    @Test
    public void testIfRightValueReturnedForUsAndCanada() {
        String country = countryByPhoneDetector.getCountryByPhone("+1800500");

        assertEquals(country.toLowerCase(), "canada / united states");
    }

    @Test(expected = PhoneNumberFormatException.class)
    public void testWrongPhoneNumberExceptionForNull() {
        countryByPhoneDetector.getCountryByPhone(null);
    }

    @Test(expected = PhoneNumberFormatException.class)
    public void testWrongPhoneNumberExceptionForEmpty() {
        countryByPhoneDetector.getCountryByPhone("");
    }

    @Test(expected = PhoneNumberFormatException.class)
    public void testWrongPhoneNumberExceptionForMultipleSpaces() {
        countryByPhoneDetector.getCountryByPhone("          ");
    }

    @Test(expected = PhoneNumberFormatException.class)
    public void testWrongPhoneNumberExceptionForLetter() {
        countryByPhoneDetector.getCountryByPhone("+3712964879A");
    }

    @Test(expected = PhoneNumberFormatException.class)
    public void testWrongPhoneNumberExceptionForMultiplePluses() {
        countryByPhoneDetector.getCountryByPhone("++3712964879");
    }

}
