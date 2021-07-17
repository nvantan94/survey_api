package de.niahealth.patient.survey.controller;

import de.niahealth.patient.survey.constant.Paths;
import de.niahealth.patient.survey.entity.Survey;
import de.niahealth.patient.survey.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class SurveyController {
    private SurveyService surveyService;

    @Autowired
    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @RequestMapping(value = Paths.SURVEY_API, method = RequestMethod.POST)
    @ResponseBody
    public Survey addSurvey(@RequestBody Survey survey) {
        return surveyService.saveSurvey(survey);
    }
}
