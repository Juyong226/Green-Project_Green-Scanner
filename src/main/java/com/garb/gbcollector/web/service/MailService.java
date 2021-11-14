package com.garb.gbcollector.web.service;

import java.util.ArrayList;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
     
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMail(String normalEmail, String mempw) {
        
        // 수신 대상을 담을 ArrayList 생성
        ArrayList<String> toUserList = new ArrayList<>();
        
        // 수신 대상 추가
        toUserList.add(normalEmail);
        
        // 수신 대상 개수
        int toUserSize = toUserList.size();
        
        // SimpleMailMessage (단순 텍스트 구성 메일 메시지 생성할 때 이용)
        SimpleMailMessage simpleMessage = new SimpleMailMessage();
        
        // 수신자 설정
        simpleMessage.setTo((String[]) toUserList.toArray(new String[toUserSize]));
        
        // 메일 제목
        simpleMessage.setSubject("Greenscanner 비밀번호 초기화");
        
        // 메일 내용
        simpleMessage.setText("초기화된 비밀번호: " + mempw);
        
        // 메일 발송
        javaMailSender.send(simpleMessage);
    }
}
