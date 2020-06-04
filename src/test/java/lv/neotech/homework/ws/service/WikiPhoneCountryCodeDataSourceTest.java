package lv.neotech.homework.ws.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WikiPhoneCountryCodeDataSourceTest {

    @Autowired
    WikiPhoneCountryCodeDataSource wikiPhoneCountryCodeDataSource;
    private Map<String, List<String>> map;

    @Before
    public void loadDataMap() throws IOException {
        if (map == null) {
            map = wikiPhoneCountryCodeDataSource.getCodeMap();
        }
    }

    @Test
    public void testIfLoadedMapContainsLatvianCode() {
        List<String> latvia = map.get("371");

        assertTrue(latvia.contains("Latvia"));
    }

    @Test
    public void testIfLoadedMapContainsUsAndCanadaCode() {
        List<String> countries = map.get("1");

        assertTrue(countries.contains("United States"));
        assertTrue(countries.contains("Canada"));
    }

    @Test
    public void testIfLoadedMapDoesNotMissCnOrSe() {
        List<String> countries = map.get("5993");

        assertTrue(countries.contains("Caribbean Netherlands"));
        assertTrue(countries.contains("Sint Eustatius"));
    }


}
