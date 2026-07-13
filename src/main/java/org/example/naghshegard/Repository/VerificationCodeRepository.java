package org.example.naghshegard.Repository;

import org.example.naghshegard.Model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    Optional<VerificationCode> findByEmailAndCodeAndTypeAndUsedFalse(String email, String code, String type);
    void deleteByEmailAndType(String email, String type);
    void deleteByExpiresAtBefore(LocalDateTime date);
}