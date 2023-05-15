package com.joolove.core.service;

public interface EmailService {
    String sendAuthCode(String to) throws Exception;

}