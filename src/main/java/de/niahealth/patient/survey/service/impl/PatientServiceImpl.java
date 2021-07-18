package de.niahealth.patient.survey.service.impl;

import de.niahealth.patient.survey.entity.Patient;
import de.niahealth.patient.survey.repository.PatientRepository;
import de.niahealth.patient.survey.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientServiceImpl implements PatientService {
    @Autowired
    private PatientRepository patientRepository;

    @Override
    public Patient retrievePatient(String username) {
        return patientRepository.findByUsername(username);
    }
}
