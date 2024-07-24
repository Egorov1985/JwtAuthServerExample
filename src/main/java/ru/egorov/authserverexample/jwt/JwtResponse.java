package ru.egorov.authserverexample.jwt;

public record JwtResponse(String type, String accessToken, String refreshToken) {

}
