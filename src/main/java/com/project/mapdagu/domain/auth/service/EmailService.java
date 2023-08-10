package com.project.mapdagu.domain.auth.service;

import com.project.mapdagu.domain.auth.dto.request.EmailRequestDto;
import com.project.mapdagu.domain.auth.dto.response.EmailResponseDto;
import com.project.mapdagu.domain.member.repository.MemberRepository;
import com.project.mapdagu.error.exception.custom.BusinessException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import static com.project.mapdagu.error.ErrorCode.ALREADY_EXIST_EMAIL;
import static com.project.mapdagu.error.ErrorCode.EMAIL_SEND_ERROR;


@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;
    private final MemberRepository memberRepository;
    private String authNum; //랜덤 인증 코드

    /**
     * 랜덤 인증 코드 생성
     */
    private void createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for(int i = 0; i < 8; i++) { // 8자리
            int index = random.nextInt(3);

            switch (index) {
                case 0 : // 소문자
                    key.append((char) ((int)random.nextInt(26) + 97));
                    break;
                case 1: // 대문자
                    key.append((char) ((int)random.nextInt(26) + 65));
                    break;
                case 2: // 0-9 숫자
                    key.append(random.nextInt(9));
                    break;
            }
        }
        authNum = key.toString();
    }

    /**
     * 메일 양식 작성
     */
    private MimeMessage createEmailForm(String email) throws MessagingException, UnsupportedEncodingException {

        createCode(); //인증 코드 생성
        String setFrom = "mapdaguu@gmail.com"; // email-config에 설정한 자신의 이메일 주소
        String toEmail = email; // 받는 사람
        String title = "내가 맵다 했지!\uD83D\uDD25 회원가입 인증 번호입니다."; // 제목

        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, toEmail); // 보낼 이메일 설정
        message.setSubject(title);
        message.setFrom(setFrom);
        message.setText(setContext(authNum), "utf-8", "html");

        return message;
    }

    /**
     * 메일 전송
     */
    public EmailResponseDto sendEmail(EmailRequestDto requestDto) throws BusinessException {

        if (memberRepository.existsByEmail(requestDto.email())) {
            throw new BusinessException(ALREADY_EXIST_EMAIL);
        }
        try{
            // 메일전송에 필요한 정보 설정
            MimeMessage emailForm = createEmailForm(requestDto.email());
            emailSender.send(emailForm);
            return new EmailResponseDto(authNum);
        }catch (UnsupportedEncodingException | MessagingException e){
            throw new BusinessException(EMAIL_SEND_ERROR);
        }

    }

    /**
     * context 설정
     */
    private String setContext(String code) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process("mail", context);
    }
}
