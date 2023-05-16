package com.joolove.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private JavaMailSender mailSender;
    private MailProperties mailProperties;
    @Value("${spring.mail.username}")
    private String id;
    private String ePw;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender, MailProperties mailProperties) {
        this.mailSender = mailSender;
        this.mailProperties = mailProperties;
    }

    @Override
    public String sendAuthCode(String to)
            throws MailException, MessagingException, UnsupportedEncodingException {
        ePw = createKey();
        MimeMessage message = createMessage(to);

        try {
            mailSender.send(message);
        } catch (MailException ex) {
            throw new MailSendException("Failed to send email", ex);
        }

        return ePw;
    }

    public MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException {
        log.info("보내는 대상 : "+ to);
        log.info("인증 번호 : " + ePw);

        MimeMessage message = mailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, to);
        message.setSubject("칠링필링 회원가입 인증 코드 : 이메일");

        // 메일 내용 : 메일의 sub-type 을 html 로 지정하여 html 문법 사용
        String msg = "";
        msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">이메일 주소 확인</h1>";
        msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">아래 확인 코드를 입력해주세요.</p>";
        msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
        msg += ePw;
        msg += "</td></tr></tbody></table></div>";

        message.setText(msg, "utf-8", "html");
        message.setFrom(new InternetAddress(id,"CF_Admin"));

        return message;
    }

    public static String createKey() {
        StringBuffer key = new StringBuffer();  // thread-safe in multi-threading environment using synchronized keyword
        Random rnd = new Random();

        // code 6 digits
        for (int i = 0; i < 6; ++i) {
            key.append((rnd.nextInt(10)));
        }

        return key.toString();
    }

    public String getEPw() {
        return ePw;
    }
}