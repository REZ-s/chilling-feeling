//package com.joolove.core;
//
//import com.joolove.core.service.SMSServiceImpl;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.transaction.annotation.Transactional;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//@Transactional
//public class SMSServiceTest {
//
//    @Autowired
//    private SMSServiceImpl smsService;
//
//    @Test
//    public void smsTest() throws Exception {
//        // 1. 직접 메소드 호출
//        String phoneNumber = "010-7369-6639";
//        smsService.sendAuthCode(phoneNumber);
//
//    }
//}
