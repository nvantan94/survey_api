package de.niahealth.patient.survey.controller;

import de.niahealth.patient.survey.constant.Paths;
import de.niahealth.patient.survey.entity.Patient;
import de.niahealth.patient.survey.entity.Survey;
import de.niahealth.patient.survey.exception.SurveyAlreadyExistsException;
import de.niahealth.patient.survey.service.PatientService;
import de.niahealth.patient.survey.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class SurveyController {
    @Autowired
    private SurveyService surveyService;

    @Autowired
    private PatientService patientService;

    @RequestMapping(value = Paths.SURVEY_API, method = RequestMethod.POST)
    @ResponseBody
    public Survey addSurvey(@RequestBody Survey survey) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Patient patient = patientService.retrievePatient(username);

        validateExistsTodaySurvey(patient.getId());

        survey.setPatient(patient);
        return surveyService.saveSurvey(survey);
    }

    private void validateExistsTodaySurvey(long patientId) {
        if (surveyService.existsTodaySurvey(patientId))
            throw new SurveyAlreadyExistsException();
    }
}
