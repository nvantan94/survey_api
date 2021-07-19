package de.niahealth.patient.survey.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;

/**
 * An entity for storing information related to a survey.
 * A survey contains the following fields:
 *
 * <p>
 * - <tt>id</tt>: unique id for each survey.
 *
 * <p>
 * - <tt>patient</tt>: the patient that this survey belongs to.
 *
 * <p>
 * - <tt>lastNightSleep</tt>: how was the patient sleep last night?
 *
 * <p>
 * - <tt>skinCondition</tt>: skin condition of patient.
 *
 * <p>
 * - <tt>createdAt</tt>: creation time of this survey.
 */
@ApiModel("Survey entity")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patientId", nullable = false)
    @JsonIgnore
    private Patient patient;

    @ApiModelProperty(value = "last night sleep", example = "5")
    @Column(nullable = false)
    @Min(value = 0)
    @Max(value = 10)
    private Byte lastNightSleep;

    @ApiModelProperty(value = "skin condition", example = "10")
    @Column(nullable = false)
    @Min(value = 0)
    @Max(value = 10)
    private Byte skinCondition;

    @ApiModelProperty(value = "creation time")
    @CreationTimestamp
    private Date createdAt;

    public Survey(byte lastNightSleep, byte skinCondition) {
        this.lastNightSleep = lastNightSleep;
        this.skinCondition = skinCondition;
    }

    public Survey(int lastNightSleep, int skinCondition) {
        this.lastNightSleep = (byte) lastNightSleep;
        this.skinCondition = (byte) skinCondition;
    }
}
