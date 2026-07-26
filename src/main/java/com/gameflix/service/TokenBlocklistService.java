package com.gameflix.service;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TokenBlocklistService {

    private final Map<String, Long> blocklist = new ConcurrentHashMap<>();

    public void block(String token, long expirationEpochMs) {
        blocklist.put(token, expirationEpochMs);
    }

    public boolean isBlocked(String token) {
        Long expiration = blocklist.get(token);
        if (expiration == null) {
            return false;
        }
        if (expiration < System.currentTimeMillis()) {
            blocklist.remove(token);
            return false;
        }
        return true;
    }

    @Scheduled(fixedRate = 3600000)
    public void cleanupExpired() {
        long now = System.currentTimeMillis();
        Iterator<Map.Entry<String, Long>> iterator = blocklist.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Long> entry = iterator.next();
            if (entry.getValue() < now) {
                iterator.remove();
            }
        }
    }
}
