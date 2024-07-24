package ru.egorov.authserverexample.jwt;


import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ru.egorov.authserverexample.model.Token;
import ru.egorov.authserverexample.model.User;
import ru.egorov.authserverexample.repository.TokenRepository;
import ru.egorov.authserverexample.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final static String JWT_TYPE = "Bearer";

    @Transactional
    public Mono<JwtResponse> login(@NonNull JwtRequest request) {
        return getUser(request.login())
                .filter(user -> passwordEncoder.matches(request.password(), user.getPassword()))
                .flatMap(user -> {
                    final String accessToken = jwtProvider.generatedAccessToken(user);
                    final String refreshToken = jwtProvider.generatedRefreshToken(user);
                    logger.info("Generated access and refresh token for {}", user.getLogin());
                    return tokenRepository.save(Token.builder().token(refreshToken).isActive(true).login(user.getLogin()).build())
                            .map(token -> new JwtResponse(JWT_TYPE, accessToken, refreshToken));
                }).switchIfEmpty(Mono.empty());
    }

    @Transactional
    public Mono<JwtResponse> refreshAccessToken(JwtRefreshRequest jwtRefreshRequest) {
        return Mono.just(jwtRefreshRequest.refreshToken())
                .filter(jwtProvider::validateRefreshToken)
                .flatMap(tokenRepository::findByToken)
                .filter(token -> token != null && token.isActive()
                        && token.getToken().equals(jwtRefreshRequest.refreshToken()))
                .flatMap(token -> {
                    final Claims claims = jwtProvider.getRefreshClaims(token.getToken());
                    final String login = claims.getSubject();
                    logger.info("Generated new access token for {}", login);
                    return userRepository.findUsersByLogin(login)
                            .flatMap(user -> Mono.fromSupplier(() -> new JwtResponse(JWT_TYPE,
                                    jwtProvider.generatedAccessToken(user), null)))
                            .switchIfEmpty(Mono.error(new RuntimeException("User not found!")));
                })
                .switchIfEmpty(Mono.empty());
    }

    @Transactional
    public Mono<JwtResponse> logout(Authentication authentication) {
        return userRepository.findUsersByLogin(authentication.getPrincipal().toString())
                .flatMapMany(user -> tokenRepository.findTokensByLogin(user.getLogin()))
                .flatMap(token -> {
                    token.setActive(false);
                   return tokenRepository.save(token);
                })
                .single().flatMap(token -> Mono.fromSupplier(() -> new JwtResponse(null,
                        null, null)))
                .switchIfEmpty(Mono.empty());
    }

    private Mono<User> getUser(String login) {
        return userRepository.findUsersByLogin(login)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")));

    }

}
