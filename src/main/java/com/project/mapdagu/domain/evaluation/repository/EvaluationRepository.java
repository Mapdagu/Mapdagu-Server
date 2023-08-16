package com.project.mapdagu.domain.evaluation.repository;

import com.project.mapdagu.domain.evaluation.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
}
