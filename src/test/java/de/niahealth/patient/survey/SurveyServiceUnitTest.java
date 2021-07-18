package de.niahealth.patient.survey;

import de.niahealth.patient.survey.entity.Survey;
import de.niahealth.patient.survey.repository.PatientRepository;
import de.niahealth.patient.survey.repository.SurveyRepository;
import de.niahealth.patient.survey.service.SurveyService;
import de.niahealth.patient.survey.service.impl.SurveyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Survey service unit tests")
public class SurveyServiceUnitTest {
    @Mock
    private SurveyRepository surveyRepository;
    @Mock
    private PatientRepository patientRepository;

    SurveyService surveyService;

    @BeforeEach
    public void init() {
        surveyService = new SurveyServiceImpl(surveyRepository, patientRepository);
    }

    @Test
    @DisplayName("Test add valid survey")
    public void testAddValidSurvey() {
        Survey survey = new Survey(4, 5);

        when(surveyRepository.save(any(Survey.class))).thenReturn(survey);

        Survey insertedSurvey = surveyService.saveSurvey(survey);

        verify(surveyRepository).save(survey);
        assertEquals(survey.getLastNightSleep(), insertedSurvey.getLastNightSleep());
        assertEquals(survey.getSkinCondition(), insertedSurvey.getSkinCondition());
    }

    @Test
    @DisplayName("Test add invalid survey")
    public void testAddInvalidSurvey() {
        var invalidSurveys = new ArrayList<Survey>();
        invalidSurveys.add(new Survey(-1, 9));
        invalidSurveys.add(new Survey(11, 5));
        invalidSurveys.add(new Survey(5, 11));
        invalidSurveys.add(new Survey(7, -2));

        for (var survey : invalidSurveys)
            assertThrows(ConstraintViolationException.class, () -> surveyService.saveSurvey(survey));
    }

    @Test
    @DisplayName("Test add existing survey")
    public void testExistsSurvey() {
        when(surveyRepository.existsTodaySurvey(1L)).thenReturn(true);
        when(surveyRepository.existsTodaySurvey(2L)).thenReturn(false);

        var exists1 = surveyService.existsTodaySurvey(1L);
        var exists2 = surveyService.existsTodaySurvey(2L);

        verify(surveyRepository).existsTodaySurvey(1L);
        verify(surveyRepository).existsTodaySurvey(2L);
        assertTrue(exists1);
        assertFalse(exists2);
    }
}
