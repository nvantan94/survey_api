package de.niahealth.patient.survey.controller;

import de.niahealth.patient.survey.constant.Paths;
import de.niahealth.patient.survey.entity.Patient;
import de.niahealth.patient.survey.entity.Survey;
import de.niahealth.patient.survey.exception.SurveyAlreadyExistsException;
import de.niahealth.patient.survey.service.PatientService;
import de.niahealth.patient.survey.service.SurveyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Api(value = "Survey controller", description = "Contains api for adding daily survey.")
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

    @ApiOperation(value = "Add a daily survey, you can only add one survey a day.", response = Survey.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added survey"),
            @ApiResponse(code = 401, message = "You are not authenticated."),
            @ApiResponse(code = 400, message = "Your survey data is wrong or you have already added one today.")
    })
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
