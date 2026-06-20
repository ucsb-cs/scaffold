package edu.ucsb.cs.scaffold.repository;

import edu.ucsb.cs.scaffold.model.Question;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, UUID> {

  List<Question> findByAssessmentIdOrderByTitleAsc(UUID assessmentId);
}
