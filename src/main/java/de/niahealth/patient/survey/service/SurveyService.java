package de.niahealth.patient.survey.service;

import de.niahealth.patient.survey.entity.Survey;
import org.springframework.stereotype.Component;

@Component
public interface SurveyService {
    Survey saveSurvey(Survey survey);
    boolean existsTodaySurvey();
}
