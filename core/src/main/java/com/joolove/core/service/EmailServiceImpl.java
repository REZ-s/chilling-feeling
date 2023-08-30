package com.joolove.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmailServiceImpl extends MessageService {
    private String authCode;
    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;
    @Value("${spring.mail.username}")
    private String id;

    @Override
    public String sendAuthCode(String to)
            throws MailException, MessagingException, UnsupportedEncodingException {
        authCode = createAuthCode();
        MimeMessage message = createMessage(to);

        try {
            mailSender.send(message);
        } catch (MailException ex) {
            throw new MailSendException("Failed to send email", ex);
        }

        return authCode;
    }

    @Override
    public MimeMessage createMessage(String to)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, to);
        message.setSubject("칠링필링 회원가입 인증 코드 : 이메일");

        // 메일 내용 : 메일의 sub-type 을 html 로 지정하여 html 문법 사용
        String msg = "";
        msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">이메일 주소 확인</h1>";
        msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">아래 확인 코드를 입력해주세요.</p>";
        msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
        msg += authCode;
        msg += "</td></tr></tbody></table></div>";

        message.setText(msg, "utf-8", "html");
        message.setFrom(new InternetAddress(id,"CF_Admin"));

        return message;
    }

    @Override
    public String getAuthCode() {
        return authCode;
    }

}