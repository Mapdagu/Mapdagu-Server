package com.project.mapdagu.domain.evaluation.service;

import com.project.mapdagu.domain.evaluation.dto.request.EvaluationInfoRequestDto;
import com.project.mapdagu.domain.evaluation.dto.request.EvaluationSaveRequestDto;
import com.project.mapdagu.domain.evaluation.dto.request.EvaluationUpdateRequestDto;
import com.project.mapdagu.domain.evaluation.dto.response.EvaluationGetResponseDto;
import com.project.mapdagu.domain.evaluation.dto.response.EvaluationSearchResponseDto;
import com.project.mapdagu.domain.evaluation.dto.response.EvaluationsGetResponseDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.project.mapdagu.error.ErrorCode.*;

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

    public void updateEvaluation(String email, EvaluationUpdateRequestDto requestDto, Long evaluationId) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        Evaluation evaluation = evaluationRepository.findByIdAndMemberId(evaluationId, member.getId()).orElseThrow(() -> new BusinessException(EVALUATION_NOT_FOUND));
        evaluation.updateEvaluation(requestDto.score());
    }

    public void saveEvaluationInfo(String email, EvaluationInfoRequestDto infoRequestDto) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
        member.updateEvaluationInfo(infoRequestDto.scoville(), infoRequestDto.level());
    }

    @Transactional(readOnly = true)
    public EvaluationGetResponseDto getOneEvaluation(String email, Long evaluationId) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
        Evaluation evaluation = evaluationRepository.findById(evaluationId).orElseThrow(() -> new BusinessException(EVALUATION_NOT_FOUND));
        return EvaluationGetResponseDto.from(evaluation);
    }

    @Transactional(readOnly = true)
    public Slice<EvaluationsGetResponseDto> getEvaluations(String email, Pageable pageable) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
        Slice<Evaluation> evaluations = evaluationRepository.findByMemberId(member.getId(), pageable);
        Slice<EvaluationsGetResponseDto> responseDto = evaluations.map(e -> EvaluationsGetResponseDto.from(e));
        return responseDto;
    }

    @Transactional(readOnly = true)
    public Slice<EvaluationSearchResponseDto> searchEvaluation(String email, String search, Pageable pageable) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
        Slice<Evaluation> results = evaluationRepository.findByMemberIdAndFoodNameLike(member.getId(), search, pageable);
        Slice<EvaluationSearchResponseDto> response = results.map(e -> EvaluationSearchResponseDto.from(e));
        return response;
    }
}
