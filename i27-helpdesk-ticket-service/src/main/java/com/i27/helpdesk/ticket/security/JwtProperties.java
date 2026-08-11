package com.i27.helpdesk.ticket.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String secret;
    private long expiryMillis;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpiryMillis() {
        return expiryMillis;
    }

    public void setExpiryMillis(long expiryMillis) {
        this.expiryMillis = expiryMillis;
    }
}
