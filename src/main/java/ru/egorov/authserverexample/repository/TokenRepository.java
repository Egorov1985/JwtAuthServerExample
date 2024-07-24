package ru.egorov.authserverexample.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.egorov.authserverexample.model.Token;

import java.math.BigInteger;

public interface TokenRepository extends ReactiveCrudRepository<Token, BigInteger> {

    Mono<Token> findByToken(String token);
    Flux<Token> findTokensByLogin(String login);
}
