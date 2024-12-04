package com.hotel.util;

import java.util.Set;
import java.util.HashSet;

import org.springframework.stereotype.Component;

// We will use database later on
@Component
public class TokenBlacklist {
    private final Set<String> blacklistedTokens = new HashSet<>();

    public void add(String token) {
        blacklistedTokens.add(token);
    }

    public void remove(String token) {
        blacklistedTokens.remove(token);
    }

    public boolean isBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}

