package de.niahealth.patient.survey.service.impl;

import de.niahealth.patient.survey.entity.Patient;
import de.niahealth.patient.survey.entity.Survey;
import de.niahealth.patient.survey.exception.SurveyAlreadyExistsException;
import de.niahealth.patient.survey.repository.PatientRepository;
import de.niahealth.patient.survey.repository.SurveyRepository;
import de.niahealth.patient.survey.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import javax.validation.Validation;

@Service
public class SurveyServiceImpl implements SurveyService {
    private SurveyRepository surveyRepository;
    private PatientRepository patientRepository;

    @Autowired
    public SurveyServiceImpl(SurveyRepository surveyRepository,
                             PatientRepository patientRepository) {
        this.surveyRepository = surveyRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public Survey saveSurvey(Survey survey) {
        validateSurvey(survey);

        Patient patient = retrievePatient();
        validateExistsTodaySurvey(patient.getId());

        survey.setPatient(patient);
        return surveyRepository.save(survey);
    }

    private void validateSurvey(Survey survey) {
        var validator = Validation.buildDefaultValidatorFactory().getValidator();

        var constraintViolations = validator.validate(survey);

        if (constraintViolations.size() > 0)
            throw new ConstraintViolationException(constraintViolations);
    }

    private Patient retrievePatient() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return patientRepository.findByUsername(username);
    }

    private void validateExistsTodaySurvey(long patientId) {
        if (surveyRepository.existsTodaySurvey(patientId))
            throw new SurveyAlreadyExistsException();
    }

}
