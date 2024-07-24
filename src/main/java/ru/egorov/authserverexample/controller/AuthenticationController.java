package ru.egorov.authserverexample.controller;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.egorov.authserverexample.jwt.AuthService;
import ru.egorov.authserverexample.jwt.JwtRefreshRequest;
import ru.egorov.authserverexample.jwt.JwtRequest;
import ru.egorov.authserverexample.jwt.JwtResponse;

@RestController
@RequestMapping("/api/v1/auth/user")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthService authService;


    @PostMapping("/login")
    public Mono<ResponseEntity<JwtResponse>> login(@NonNull @RequestBody JwtRequest jwtRequest) {
        return authService.login(jwtRequest)
                .map(jwtResponse -> ResponseEntity.ok().body(jwtResponse))
                .switchIfEmpty(Mono.fromSupplier(() -> ResponseEntity.badRequest().build()));
    }

    @PostMapping("/refresh-access")
    public Mono<ResponseEntity<JwtResponse>> refreshAccessToken(@RequestBody JwtRefreshRequest refreshRequest) {
        return authService.refreshAccessToken(refreshRequest)
                .map(jwtResponse -> ResponseEntity.ok().body(jwtResponse))
                .switchIfEmpty(Mono.fromSupplier(() -> ResponseEntity.badRequest().build()));
    }

    @PostMapping("/logout")
    public Mono<ResponseEntity<JwtResponse>> logout(Authentication authentication) {
        return authService.logout(authentication)
                .map(jwtResponse -> ResponseEntity.accepted().body(jwtResponse))
                .switchIfEmpty(Mono.fromSupplier(() -> ResponseEntity.badRequest().build()));
    }
}
