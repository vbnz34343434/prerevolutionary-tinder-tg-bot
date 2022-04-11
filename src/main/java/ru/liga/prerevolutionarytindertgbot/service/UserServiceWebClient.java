package ru.liga.prerevolutionarytindertgbot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import ru.liga.prerevolutionarytindertgbot.domain.user.UserDto;
import ru.liga.prerevolutionarytindertgbot.domain.user.UserPageableResponse;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceWebClient {
    private final WebClient webClient;


    public UserServiceWebClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Optional<UserDto> getUserByUsername(long username) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/{usertgid}")
                        .build(username))
                .retrieve()
                .bodyToMono(UserDto.class)
                .onErrorResume(WebClientResponseException.class,
                        ex -> ex.getStatusCode().equals(HttpStatus.NOT_FOUND) ? Mono.empty() : Mono.error(ex))
                .blockOptional();
    }

    public UserDto createUser(UserDto userDto) {

        return webClient
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(userDto))
                .retrieve()
                .bodyToMono(UserDto.class)
                .block();
    }

    public UserDto updateUser(UserDto userDto) {
        return webClient
                .put()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(userDto))
                .retrieve()
                .bodyToMono(UserDto.class)
                .block();
    }

    public UserPageableResponse getUsers(long userId, int page, int size) {
        return webClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .queryParam("username", userId)
                                .queryParam("page", page)
                                .queryParam("size", size).build())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(UserPageableResponse.class)
                .log()
                .block();
    }


    public void likeUser(long userId1, long userId2) {
        webClient
                .post()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/{user1}/likes/{user2}")
                                .build(userId1, userId2))
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public Optional<List<UserDto>> getUserFavourites(long userId) {

        return webClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/{username}/favorite")
                                .build(userId))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<UserDto>>() {})
                .log()
                .blockOptional();
    }

}