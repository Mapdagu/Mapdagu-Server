package com.project.mapdagu.domain.evaluation.repository;

import com.project.mapdagu.domain.evaluation.entity.Evaluation;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

    @Query("select e from Evaluation e " +
            "   where e.member.id = :memberId and e.food.name = :foodName")
    Optional<Evaluation> findByMemberIdAndFoodName(@Param("memberId") Long memberId, @Param("foodName") String foodName);

    @Query("select e from Evaluation e " +
            "   where e.member.id =:memberId order by e.createdDate DESC ")
    Page<Evaluation> findByMemberId(@Param("memberId") Long memberId, Pageable pageable);
}
