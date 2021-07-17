package de.niahealth.patient.survey.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;

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
    private Patient patient;

    @Column(nullable = false)
    @Min(value = 0)
    @Max(value = 10)
    private byte lastNightSleep;

    @Column(nullable = false)
    @Min(value = 0)
    @Max(value = 10)
    private byte skinCondition;

    @CreationTimestamp
    private Date createdAt;

    public Survey(byte lastNightSleep, byte skinCondition) {
        this.lastNightSleep = lastNightSleep;
        this.skinCondition = skinCondition;
    }
}
