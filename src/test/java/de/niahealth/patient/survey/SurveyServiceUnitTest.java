package de.niahealth.patient.survey;

import de.niahealth.patient.survey.entity.Survey;
import de.niahealth.patient.survey.repository.PatientRepository;
import de.niahealth.patient.survey.repository.SurveyRepository;
import de.niahealth.patient.survey.service.SurveyService;
import de.niahealth.patient.survey.service.impl.SurveyServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@DisplayName("Survey service unit test")
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

        Mockito.when(surveyRepository.save(any(Survey.class))).thenReturn(survey);

        Survey insertedSurvey = surveyService.saveSurvey(survey);

        Mockito.verify(surveyRepository).save(survey);
        Assertions.assertEquals(survey.getLastNightSleep(), insertedSurvey.getLastNightSleep());
        Assertions.assertEquals(survey.getSkinCondition(), insertedSurvey.getSkinCondition());
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
            Assertions.assertThrows(ConstraintViolationException.class, () -> surveyService.saveSurvey(survey));
    }
}
