package com.project.mapdagu.domain.evaluation.repository;

import com.project.mapdagu.domain.evaluation.entity.Evaluation;
import com.project.mapdagu.domain.food.entity.Food;
import com.project.mapdagu.domain.member.entity.Member;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

    @Query("select e from Evaluation e " +
            "   where e.member.id = :memberId and e.food.name = :foodName")
    Optional<Evaluation> findByMemberIdAndFoodName(@Param("memberId") Long memberId, @Param("foodName") String foodName);
}
