package edu.ucsb.cs.scaffold.model;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "question_concepts")
@Getter
@Setter
@NoArgsConstructor
public class QuestionConcept {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "question_id", nullable = false)
  private UUID questionId;

  @Column(name = "concept_id", nullable = false)
  private String conceptId;

  @Column(name = "subconcept_label")
  private String subconceptLabel;
}
