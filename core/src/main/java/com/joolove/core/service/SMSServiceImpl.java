package com.joolove.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.model.MessageType;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Slf4j
@RequiredArgsConstructor
public class SMSServiceImpl extends MessageService {

    private DefaultMessageService messageService;

    @Value("${sms.coolsms.api-key}")
    private String apiKey;

    @Value("${sms.coolsms.api-secret}")
    private String apiSecret;

    @Value("${sms.coolsms.host}")
    private String host;

    @Value("${sms.coolsms.domain}")
    private String domain;

    private String ePw;

    @PostConstruct
    private void initialize() {
        messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, domain);
    }

    @Override
    public String sendAuthCode(String to) throws Exception {
        ePw = createAuthCode();
        Message message = createMessage(to);

        log.info("보내는 대상 : "+ to);
        log.info("인증 번호 : " + ePw);
        try {
            messageService.sendOne(new SingleMessageSendingRequest(message));
        } catch (Exception ex) {
            throw new Exception("Failed to send sms", ex);
        }

        return ePw;
    }

    @Override
    public Message createMessage(String to) throws Exception {
        Message message = new Message();
        message.setFrom(to);
        message.setTo(host);
        message.setType(MessageType.SMS);
        message.setText("[칠링필링] 인증번호 : " + ePw);

        return message;
    }

}
