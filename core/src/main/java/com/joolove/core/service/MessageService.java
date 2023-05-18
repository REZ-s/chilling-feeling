package com.joolove.core.service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

public class MessageService {

    private String ePw;

    public String sendAuthCode(String to) throws Exception {
        ePw = createAuthCode();
        // ...
        return ePw;
    }

    /* Need implementation */
    public Object createMessage(String to) throws Exception {
        return null;
    }

    /* 6-digits auth code number */
    public String createAuthCode() {
        StringBuffer key = new StringBuffer();  // thread-safe in multi-threading environment using synchronized keyword
        Random rnd = new Random();

        for (short i = 0; i < 6; ++i) {
            key.append((rnd.nextInt(10)));
        }

        return key.toString();
    }

    public String getEPw() {
        return ePw;
    }

}
