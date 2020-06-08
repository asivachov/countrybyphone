package lv.neotech.homework.phone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApiTest {

    @Autowired
    private MockMvc mvc;

    private static final String DETECT_COUNTRY_PATH_MASK = "/api/detectCountry?phone=%s";

    @Test
    public void testDetectPhoneForEmptyValue() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(formatRequestPath("")).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(2));
    }

    @Test
    public void testDetectPhoneForMultipleSpacesValue() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(formatRequestPath("              ")).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(2));
    }

    @Test
    public void testDetectPhoneReturnsLatviaForLatvianPhone() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(formatRequestPath("+37129648790")).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.country").value("Latvia"));
    }


    @Test
    public void testDetectPhoneReturnsErrorForNonExistingCountryCode() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(formatRequestPath("+21429648790")).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1));
    }

    @Test
    public void testDetectPhoneReturnsErrorForWrongNumber() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(formatRequestPath("+37129648a790")).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(2));
    }

    private String formatRequestPath(String phone) {
        return String.format(DETECT_COUNTRY_PATH_MASK, phone);
    }
}