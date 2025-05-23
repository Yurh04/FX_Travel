package org.fxtravel.fxspringboot.service.impl;

import org.fxtravel.fxspringboot.mapper.VerificationMapper;
import org.fxtravel.fxspringboot.pojo.entities.VerificationCode;
import org.fxtravel.fxspringboot.service.inter.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VerificationServiceImp implements VerificationService {
    @Autowired
    VerificationMapper verificationMapper;

    @Override
    public void save(String code, Integer userId) {
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setCode(code);
        verificationCode.setUserId(userId);

        verificationMapper.insertVerificationCode(verificationCode);
    }

}
