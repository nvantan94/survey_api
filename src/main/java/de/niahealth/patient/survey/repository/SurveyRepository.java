package de.niahealth.patient.survey.repository;

import de.niahealth.patient.survey.entity.Survey;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyRepository extends CrudRepository<Survey, Long> {
    @Query("SELECT CASE WHEN count(*) > 0 THEN true ELSE false END FROM Survey WHERE createdAt > current_date()")
    boolean existsTodaySurvey();
}
