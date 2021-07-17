package de.niahealth.patient.survey.service.impl;

import de.niahealth.patient.survey.entity.Survey;
import de.niahealth.patient.survey.repository.SurveyRepository;
import de.niahealth.patient.survey.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import javax.validation.Validation;

@Service
public class SurveyServiceImpl implements SurveyService {
    private SurveyRepository surveyRepository;

    @Autowired
    public SurveyServiceImpl(SurveyRepository surveyRepository) {
        this.surveyRepository = surveyRepository;
    }

    @Override
    public Survey saveSurvey(Survey survey) {
        validateSurvey(survey);
        return surveyRepository.save(survey);
    }

    @Override
    public boolean existsTodaySurvey() {
        return surveyRepository.existsTodaySurvey();
    }

    private void validateSurvey(Survey survey) {
        var validator = Validation.buildDefaultValidatorFactory().getValidator();

        var constraintViolations = validator.validate(survey);

        if (constraintViolations.size() > 0)
            throw new ConstraintViolationException(constraintViolations);
    }

}
