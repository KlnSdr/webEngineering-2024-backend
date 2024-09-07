package com.sks.users.service;

import com.sks.users.service.data.TokenService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TokenCleanupTask {
    private static final Logger log = LoggerFactory.getLogger(TokenCleanupTask.class);
    private final TokenService tokenService;

    public TokenCleanupTask(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void cleanup() {
        log.info("Cleaning up expired tokens");
        tokenService.deleteExpired();
    }
}
