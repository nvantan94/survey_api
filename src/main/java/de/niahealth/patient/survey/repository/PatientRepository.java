package de.niahealth.patient.survey.repository;

import de.niahealth.patient.survey.entity.Patient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends CrudRepository<Patient, Long> {
    Patient findByUsername(String username);
}
