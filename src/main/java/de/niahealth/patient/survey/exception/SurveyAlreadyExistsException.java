package de.niahealth.patient.survey.exception;

public class SurveyAlreadyExistsException extends RuntimeException {
    public SurveyAlreadyExistsException() {
        super("You have already finished survey for today!");
    }
}
