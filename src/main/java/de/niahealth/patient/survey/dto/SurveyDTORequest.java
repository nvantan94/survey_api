package de.niahealth.patient.survey.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel(value = "Survey request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SurveyDTORequest {
    @ApiModelProperty(
            value = "last night sleep",
            example = "5"
    )
    private int lastNightSleep;

    @ApiModelProperty(
            value = "skin condition",
            example = "9"
    )
    private int skinCondition;
}
