package org.example.naghshegard.Service;


import org.example.naghshegard.Model.VerificationCode;
import org.example.naghshegard.Repository.VerificationCodeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VerificationService {

    private final VerificationCodeRepository codeRepository;
    private final EmailService emailService;

    @Value("${verification.code.expiration:10}")
    private int expirationMinutes;

    public VerificationService(VerificationCodeRepository codeRepository, EmailService emailService) {
        this.codeRepository = codeRepository;
        this.emailService = emailService;
    }

    private String generateCode() {
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    @Transactional
    public void sendVerificationCode(String email, String type) throws Exception {
        codeRepository.deleteByEmailAndType(email, type);

        String code = generateCode();
        VerificationCode verificationCode = new VerificationCode(email, code, type, expirationMinutes);
        codeRepository.save(verificationCode);

        emailService.sendVerificationCode(email, code, type);
    }

    @Transactional
    public boolean verifyCode(String email, String code, String type) {
        VerificationCode verificationCode = codeRepository
                .findByEmailAndCodeAndTypeAndUsedFalse(email, code, type)
                .orElse(null);

        if (verificationCode == null) {
            return false;
        }

        if (!verificationCode.isValid()) {
            return false;
        }

        verificationCode.setUsed(true);
        codeRepository.save(verificationCode);
        return true;
    }

    public List<VerificationCode> getAllCodes() {
        return codeRepository.findAll();
    }

    public void deleteCode(Long id) {
        codeRepository.deleteById(id);
    }

    public void cleanupExpiredCodes() {
        codeRepository.deleteByExpiresAtBefore(LocalDateTime.now());
    }
}