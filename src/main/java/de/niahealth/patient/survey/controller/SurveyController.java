package de.niahealth.patient.survey.controller;

import de.niahealth.patient.survey.constant.Paths;
import de.niahealth.patient.survey.dto.SurveyDTORequest;
import de.niahealth.patient.survey.entity.Patient;
import de.niahealth.patient.survey.entity.Survey;
import de.niahealth.patient.survey.exception.SurveyAlreadyExistsException;
import de.niahealth.patient.survey.service.PatientService;
import de.niahealth.patient.survey.service.SurveyService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@OpenAPIDefinition(
    info = @Info(
        title = "Survey controller",
        version = "1.0",
        description = "Contains api for adding daily survey."
    )
)
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

    @Operation(
        summary = "Add a daily survey, you can only add one survey a day.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Inserted Successfully Survey."),
            @ApiResponse(responseCode = "401", description = "You are not authenticated."),
            @ApiResponse(responseCode = "400", description = "Your survey fields are in wrong value."),
            @ApiResponse(responseCode = "409", description = "You already added a survey today.")
        }
    )
    @RequestMapping(value = Paths.SURVEY_API, method = RequestMethod.POST)
    @ResponseBody
    public Survey addSurvey(@RequestBody SurveyDTORequest surveyDTOReq) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Patient patient = patientService.retrievePatient(username);

        validateExistsTodaySurvey(patient.getId());

        var survey = new Survey(surveyDTOReq.getLastNightSleep(), surveyDTOReq.getSkinCondition());
        survey.setPatient(patient);
        return surveyService.saveSurvey(survey);
    }

    private void validateExistsTodaySurvey(long patientId) {
        if (surveyService.existsTodaySurvey(patientId))
            throw new SurveyAlreadyExistsException();
    }
}