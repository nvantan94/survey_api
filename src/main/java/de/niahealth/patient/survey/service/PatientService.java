package de.niahealth.patient.survey.service;

import de.niahealth.patient.survey.entity.Patient;
import org.springframework.stereotype.Component;

@Component
public interface PatientService {
    Patient retrievePatient(String username);
}
