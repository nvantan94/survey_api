package de.niahealth.patient.survey;

import de.niahealth.patient.survey.constant.Paths;
import de.niahealth.patient.survey.entity.Survey;
import de.niahealth.patient.survey.service.PatientService;
import de.niahealth.patient.survey.service.SurveyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Test survey controller")
public class SurveyControllerTest {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final static String USERNAME_BOB = "bob";
    private final static String USERNAME_ALICE = "alice";
    private final static String USERNAME_DAVID = "david";
    private final static String USERNAME_DUCK = "duck";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SurveyService surveyService;
    @Autowired
    private PatientService patientService;

    @Test
    @DisplayName("Add valid survey")
    public void testAddValidSurveys() throws Exception {
        testAddValidSurvey(new Survey((byte) 5, (byte) 8), USERNAME_BOB);
        testAddValidSurvey(new Survey((byte) 0, (byte) 10), USERNAME_ALICE);
    }

    private void testAddValidSurvey(Survey survey, String username) throws Exception {
        String today = DATE_FORMATTER.format(LocalDateTime.now(ZoneOffset.UTC));
        mockMvc.perform(MockMvcRequestBuilders.post(Paths.SURVEY_API)
                .with(user(username))
                .content(createSurveyJSONContent(survey))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastNightSleep", is((int)survey.getLastNightSleep())))
                .andExpect(jsonPath("$.skinCondition", is((int)survey.getSkinCondition())))
                .andExpect(jsonPath("$.createdAt", startsWith(today)))
                .andReturn();
    }

    @Test
    @DisplayName("Add invalid survey")
    public void testAddInvalidSurvey() throws Exception {
        String lessThanMinValErrMsg = "must be greater than or equal to 0";
        String moreThanMaxValErrMsg = "must be less than or equal to 10";

        testAddInvalidSurvey(new Survey((byte) -1, (byte) 8), lessThanMinValErrMsg, null);
        testAddInvalidSurvey(new Survey((byte) 11, (byte) 8), moreThanMaxValErrMsg, null);
        testAddInvalidSurvey(new Survey((byte) 3, (byte) -1), null, lessThanMinValErrMsg);
        testAddInvalidSurvey(new Survey((byte) 12, (byte) 11), null, moreThanMaxValErrMsg);
    }

    private void testAddInvalidSurvey(Survey survey, String lastNightSleepErrMsg,
                                      String skinConditionErrMsg) throws Exception {
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.post(Paths.SURVEY_API)
                .with(user(USERNAME_DAVID))
                .content(createSurveyJSONContent(survey))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        if (lastNightSleepErrMsg != null)
            actions.andExpect(jsonPath("$.lastNightSleep", is(lastNightSleepErrMsg)));
        if (skinConditionErrMsg != null)
            actions.andExpect(jsonPath("$.skinCondition", is(skinConditionErrMsg)));

        actions.andReturn();
    }

    @Test
    @DisplayName("Add existing survey")
    public void addExistsSurveyTest() throws Exception {
        addSampleSurvey();

        Survey survey = new Survey((byte) 3, (byte) 3);
        mockMvc.perform(MockMvcRequestBuilders.post(Paths.SURVEY_API)
                .with(user(USERNAME_DUCK))
                .content(createSurveyJSONContent(survey))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andReturn();
    }

    private void addSampleSurvey() {
        var patient = patientService.retrievePatient(USERNAME_DUCK);
        Survey survey = new Survey((byte) 4, (byte) 8);
        survey.setPatient(patient);
        surveyService.saveSurvey(survey);
    }

    private String createSurveyJSONContent(Survey survey) {
        return String.format("{\"lastNightSleep\": %d, \"skinCondition\": %d }",
                survey.getLastNightSleep(), survey.getSkinCondition());
    }
}
