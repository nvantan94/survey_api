package de.niahealth.patient.survey.controller;

import de.niahealth.patient.survey.entity.Survey;
import de.niahealth.patient.survey.exception.SurveyAlreadyExistsException;
import de.niahealth.patient.survey.service.PatientService;
import de.niahealth.patient.survey.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class SurveyController {
    private SurveyService surveyService;
    private PatientService patientService;

    @Autowired
    public SurveyController(SurveyService surveyService,
                            PatientService patientService) {
        this.surveyService = surveyService;
        this.patientService = patientService;
    }

    @RequestMapping(value = "/survey", method = RequestMethod.POST)
    @ResponseBody
    public Survey addSurvey(@RequestBody Survey survey) {
        if (surveyService.existsTodaySurvey())
            throw new SurveyAlreadyExistsException();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        survey.setPatient(patientService.retrievePatient(username));

        return surveyService.saveSurvey(survey);
    }
}
