package com.joolove.core.service;

import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.model.MessageType;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SMSServiceImpl extends MessageService {
    private String authCode;
    private DefaultMessageService messageService;

    @Value("${sms.coolsms.api-key}")
    private String apiKey;

    @Value("${sms.coolsms.api-secret}")
    private String apiSecret;

    @Value("${sms.coolsms.host}")
    private String host;

    @Value("${sms.coolsms.domain}")
    private String domain;

    @PostConstruct
    private void initialize() {
        messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, domain);
    }

    @Override
    public String sendAuthCode(String to) throws Exception {
        authCode = createAuthCode();
        Message message = createMessage(to);

        try {
            messageService.sendOne(new SingleMessageSendingRequest(message));
        } catch (Exception ex) {
            throw new Exception("Failed to send sms", ex);
        }

        return authCode;
    }

    @Override
    public Message createMessage(String to) {
        Message message = new Message();
        message.setFrom(host);
        message.setTo(extractPureNumberFromPhoneNumberString(to));
        message.setType(MessageType.SMS);
        message.setText("[칠링필링] 인증번호 : " + authCode);

        return message;
    }

    public String extractPureNumberFromPhoneNumberString(String phoneNumberString) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < phoneNumberString.length(); i++) {
            if (Character.isDigit(phoneNumberString.charAt(i))) {
                result.append(phoneNumberString.charAt(i));
            }
        }

        return result.toString();
    }

    @Override
    public String getAuthCode() {
        return authCode;
    }

}
