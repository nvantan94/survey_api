package de.niahealth.patient.survey.service;

import de.niahealth.patient.survey.entity.Survey;
import org.springframework.stereotype.Component;

/**
 * This service acts as a bridge between
 * {@link de.niahealth.patient.survey.controller.SurveyController SurveyController}
 * and {@link de.niahealth.patient.survey.repository.SurveyRepository SurveyRepository}
 */
@Component
public interface SurveyService {
    /**
     * Save a new survey, we also do some validations on survey data in this function.
     * @param survey the survey user wants to save.
     * @return inserted survey
     *
     * @throws javax.validation.ConstraintViolationException if any fields on the survey has a wrong value
     */
    Survey saveSurvey(Survey survey);

    /**
     * Check if there exists a survey for a specific patient today.
     * @param patientId the patientId you want to check for.
     * @return <tt>true</tt> if database contains a survey for this patient in today.
     */
    boolean existsTodaySurvey(long patientId);
}
