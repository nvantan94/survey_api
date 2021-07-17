package de.niahealth.patient.survey.loader;

import de.niahealth.patient.survey.entity.Patient;
import de.niahealth.patient.survey.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {
    private PatientRepository patientRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public DataLoader(PatientRepository patientRepository,
                      PasswordEncoder passwordEncoder) {
        this.patientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        patientRepository.save(new Patient(
                "bob", passwordEncoder.encode("bob"), "Bob"));
        patientRepository.save(new Patient(
                "alice", passwordEncoder.encode("alice"), "Alice"));
    }
}
