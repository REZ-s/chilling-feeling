package com.joolove.core.service;

import java.util.Random;

public abstract class MessageService {

    private String authCode;

    public abstract String sendAuthCode(String to) throws Exception;

    public abstract Object createMessage(String to) throws Exception;

    public abstract String getAuthCode();

    /* 6-digits auth code number */
    public String createAuthCode() {
        StringBuilder key = new StringBuilder(6);
        Random rnd = new Random();

        for (short i = 0; i < 6; ++i) {
            key.append(rnd.nextInt(10));
        }

        return key.toString();
    }

}
