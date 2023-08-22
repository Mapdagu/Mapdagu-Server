package com.project.mapdagu.domain.evaluation.service;

import com.project.mapdagu.domain.evaluation.dto.request.EvaluationInfoRequestDto;
import com.project.mapdagu.domain.evaluation.dto.request.EvaluationSaveRequestDto;
import com.project.mapdagu.domain.evaluation.entity.Evaluation;
import com.project.mapdagu.domain.evaluation.repository.EvaluationRepository;
import com.project.mapdagu.domain.food.entity.Food;
import com.project.mapdagu.domain.food.repository.FoodRepository;
import com.project.mapdagu.domain.member.entity.Member;
import com.project.mapdagu.domain.member.repository.MemberRepository;
import com.project.mapdagu.error.ErrorCode;
import com.project.mapdagu.error.exception.custom.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.project.mapdagu.error.ErrorCode.ALREADY_EXIST_EVALUATION;
import static com.project.mapdagu.error.ErrorCode.MEMBER_NOT_FOUND;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class EvaluationService {

    private final MemberRepository memberRepository;
    private final EvaluationRepository evaluationRepository;
    private final FoodRepository foodRepository;

    public void saveEvaluation(String email, EvaluationSaveRequestDto requestDto) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        Food food = foodRepository.findByName(requestDto.name()).orElseThrow(() -> new BusinessException(ErrorCode.FOOD_NOT_FOUND));

        if (evaluationRepository.findByMemberIdAndFoodName(member.getId(), food.getName()).isPresent()) {
            throw new BusinessException(ALREADY_EXIST_EVALUATION);
        }

        Evaluation evaluation = requestDto.toEntity(member, food, requestDto.score());
        evaluationRepository.save(evaluation);
    }

    public void saveEvaluationInfo(String email, EvaluationInfoRequestDto infoRequestDto) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
        member.updateEvaluationInfo(infoRequestDto.scoville(), infoRequestDto.level());
    }
}
