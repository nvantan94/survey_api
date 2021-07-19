package de.niahealth.patient.survey;

import de.niahealth.patient.survey.constant.Paths;
import de.niahealth.patient.survey.dto.SurveyDTORequest;
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
@DisplayName("Survey controller integration test")
public class SurveyControllerIntegrationTest {
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
    @DisplayName("Test add valid survey")
    public void testAddValidSurveys() throws Exception {
        testAddValidSurvey(new SurveyDTORequest(5, 8), USERNAME_BOB);
        testAddValidSurvey(new SurveyDTORequest(0, 10), USERNAME_ALICE);
    }

    private void testAddValidSurvey(SurveyDTORequest surveyDTOReq, String username) throws Exception {
        String today = DATE_FORMATTER.format(LocalDateTime.now(ZoneOffset.UTC));
        mockMvc.perform(MockMvcRequestBuilders.post(Paths.SURVEY_API)
                .with(user(username))
                .content(createSurveyDTOReqJSONContent(surveyDTOReq))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastNightSleep", is((int)surveyDTOReq.getLastNightSleep())))
                .andExpect(jsonPath("$.skinCondition", is((int)surveyDTOReq.getSkinCondition())))
                .andExpect(jsonPath("$.createdAt", startsWith(today)))
                .andReturn();
    }

    @Test
    @DisplayName("Test add invalid survey")
    public void testAddInvalidSurvey() throws Exception {
        String lessThanMinValErrMsg = "must be greater than or equal to 0";
        String moreThanMaxValErrMsg = "must be less than or equal to 10";

        testAddInvalidSurvey(new SurveyDTORequest(-1, 8), lessThanMinValErrMsg, null);
        testAddInvalidSurvey(new SurveyDTORequest(11, 8), moreThanMaxValErrMsg, null);
        testAddInvalidSurvey(new SurveyDTORequest(3, -1), null, lessThanMinValErrMsg);
        testAddInvalidSurvey(new SurveyDTORequest(12, 11), null, moreThanMaxValErrMsg);
    }

    private void testAddInvalidSurvey(SurveyDTORequest surveyDTOReq, String lastNightSleepErrMsg,
                                      String skinConditionErrMsg) throws Exception {
        var actions = mockMvc.perform(MockMvcRequestBuilders.post(Paths.SURVEY_API)
                .with(user(USERNAME_DAVID))
                .content(createSurveyDTOReqJSONContent(surveyDTOReq))
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
    @DisplayName("Test add existing survey")
    public void addExistsSurveyTest() throws Exception {
        addSampleSurvey();

        var surveyDTOReq = new SurveyDTORequest(3, 3);
        mockMvc.perform(MockMvcRequestBuilders.post(Paths.SURVEY_API)
                .with(user(USERNAME_DUCK))
                .content(createSurveyDTOReqJSONContent(surveyDTOReq))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andReturn();
    }

    private void addSampleSurvey() {
        var patient = patientService.retrievePatient(USERNAME_DUCK);
        var survey = new Survey(4, 8);
        survey.setPatient(patient);
        surveyService.saveSurvey(survey);
    }

    private String createSurveyDTOReqJSONContent(SurveyDTORequest surveyDTOReq) {
        return String.format("{\"lastNightSleep\": %d, \"skinCondition\": %d }",
                surveyDTOReq.getLastNightSleep(), surveyDTOReq.getSkinCondition());
    }
}
