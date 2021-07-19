package de.niahealth.patient.survey.exception;

/**
 * <tt>SurveyAlreadyExistsException</tt> can be thrown as a patient tries to add a survey in a day in which there exists one already.
 */
public class SurveyAlreadyExistsException extends RuntimeException {
    public SurveyAlreadyExistsException() {
        super("You have already finished survey for today!");
    }
}
