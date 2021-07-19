package de.niahealth.patient.survey;

import de.niahealth.patient.survey.controller.SurveyController;
import de.niahealth.patient.survey.dto.SurveyDTORequest;
import de.niahealth.patient.survey.entity.Patient;
import de.niahealth.patient.survey.entity.Survey;
import de.niahealth.patient.survey.exception.SurveyAlreadyExistsException;
import de.niahealth.patient.survey.service.PatientService;
import de.niahealth.patient.survey.service.SurveyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Survey controller unit tests")
public class SurveyControllerUnitTest {
    private static final String USERNAME_BOB = "bob";
    @Mock
    private SurveyService surveyService;
    @Mock
    private PatientService patientService;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private SurveyController surveyController;

    @Captor
    ArgumentCaptor<Survey> surveyCaptor;

    private Patient patient = new Patient();
    private SurveyDTORequest surveyDTOReq = new SurveyDTORequest(3, 5);
    private Survey survey = new Survey(surveyDTOReq.getLastNightSleep(), surveyDTOReq.getSkinCondition());

    @BeforeEach
    public void init() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(USERNAME_BOB);

        patient.setId(1L);
        when(patientService.retrievePatient(USERNAME_BOB)).thenReturn(patient);
    }

    @Test
    @DisplayName("test add valid survey")
    public void testAddValidSurvey() {
        when(surveyService.existsTodaySurvey(patient.getId())).thenReturn(false);
        when(surveyService.saveSurvey(any(Survey.class))).thenReturn(survey);

        var insertedSurvey = surveyController.addSurvey(surveyDTOReq);

        verify(surveyService).saveSurvey(surveyCaptor.capture());
        verify(surveyService).existsTodaySurvey(patient.getId());
        verify(patientService).retrievePatient(USERNAME_BOB);

        assertEquals(survey.getLastNightSleep(), insertedSurvey.getLastNightSleep());
        assertEquals(survey.getSkinCondition(), insertedSurvey.getSkinCondition());
    }

    @Test
    @DisplayName("test add invalid survey")
    public void testAddInvalidSurvey() {
        when(surveyService.existsTodaySurvey(patient.getId())).thenReturn(false);
        when(surveyService.saveSurvey(any(Survey.class))).thenThrow(ConstraintViolationException.class);

        assertThrows(ConstraintViolationException.class, () -> surveyController.addSurvey(surveyDTOReq));
    }

    @Test
    @DisplayName("test add existing survey")
    public void testAddExistingSurvey() {
        when(patientService.retrievePatient(USERNAME_BOB)).thenReturn(patient);
        when(surveyService.existsTodaySurvey(patient.getId())).thenReturn(true);

        assertThrows(SurveyAlreadyExistsException.class, () -> surveyController.addSurvey(surveyDTOReq));
    }
}
