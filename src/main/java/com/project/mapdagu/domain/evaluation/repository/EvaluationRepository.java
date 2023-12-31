package com.project.mapdagu.domain.evaluation.repository;

import com.project.mapdagu.domain.evaluation.entity.Evaluation;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

    @Query("select e from Evaluation e " +
            "   where e.member.id = :memberId and e.food.name = :foodName")
    Optional<Evaluation> findByMemberIdAndFoodName(@Param("memberId") Long memberId, @Param("foodName") String foodName);

    @Query("select e from Evaluation e " +
            "   join fetch e.food " +
            "   where e.member.id =:memberId order by e.createdDate DESC ")
    Slice<Evaluation> findByMemberId(@Param("memberId") Long memberId, Pageable pageable);

    @Query("select e from Evaluation e " +
            "   join fetch e.food " +
            " where e.member.id = :memberId and e.food.name Like %:foodName% " +
            " order by e.createdDate desc ")
    Slice<Evaluation> findByMemberIdAndFoodNameLike(@Param("memberId") Long memberId, String foodName, Pageable pageable);

    Optional<Evaluation> findByIdAndMemberId(Long id, Long memberId);

    Optional<Evaluation> findByMemberIdAndFoodId(Long memberId, Long foodId);
}
