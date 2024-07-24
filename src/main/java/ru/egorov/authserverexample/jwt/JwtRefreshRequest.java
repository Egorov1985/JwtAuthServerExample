package ru.egorov.authserverexample.jwt;


import org.springframework.lang.NonNull;

public record JwtRefreshRequest(@NonNull String refreshToken) {
}
