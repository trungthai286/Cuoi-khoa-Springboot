package org.example.story_web.repository;

import org.example.story_web.entity.TokenConfirm;
import org.example.story_web.model.enums.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenConfirmRepository extends JpaRepository<TokenConfirm, Integer> {
    Optional<TokenConfirm> findByTokenAndType(String token, TokenType type);
}