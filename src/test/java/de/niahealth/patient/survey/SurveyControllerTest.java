package de.niahealth.patient.survey;

import de.niahealth.patient.survey.constant.Paths;
import de.niahealth.patient.survey.entity.Survey;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SurveyControllerTest {
    private final static String TEST_USER_NAME1 = "bob";
    private final static String TEST_USER_NAME2 = "alice";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAddValidSurveys() throws Exception {
        testAddValidSurvey(new Survey((byte) 5, (byte) 8), TEST_USER_NAME1);
        testAddValidSurvey(new Survey((byte) 0, (byte) 10), TEST_USER_NAME2);
    }

    private void testAddValidSurvey(Survey survey, String username) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(Paths.SURVEY_API)
                .with(user(username))
                .content(createSurveyJSONContent(survey))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastNightSleep", is(survey.getLastNightSleep())))
                .andExpect(jsonPath("$.skinCondition", is(survey.getSkinCondition())))
                .andReturn();
    }

    private String createSurveyJSONContent(Survey survey) {
        String surveyStr = String.format("{\"lastNightSleep\": %d, \"skinCondition\": %d }",
                survey.getLastNightSleep(), survey.getSkinCondition());
        return surveyStr;
    }
}
