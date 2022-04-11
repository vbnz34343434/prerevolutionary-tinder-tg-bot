package ru.liga.prerevolutionarytindertgbot.service;

import org.springframework.stereotype.Service;
import ru.liga.prerevolutionarytindertgbot.domain.user.User;
import ru.liga.prerevolutionarytindertgbot.domain.user.UserDto;
import ru.liga.prerevolutionarytindertgbot.enums.Gender;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PairsNavigationService {
    private final UserServiceWebClient userServiceWebClient;
    private final UserService userService;
    private final Map<Long, List<User>> userLikes = new HashMap<>();
    private final Map<Long, Integer> currentShownUser = new HashMap<>();

    public PairsNavigationService(UserServiceWebClient userServiceWebClient, UserService userService) {
        this.userServiceWebClient = userServiceWebClient;
        this.userService = userService;
    }

    public User navigate(long userId, String direction) {
        List<User> users = userLikes.getOrDefault(userId, Collections.emptyList());
        if (users.isEmpty()) {
            users = refreshFavouritesList(userId);
            if (users.isEmpty()) {
                throw new RuntimeException("Вас никто не любит :(");
            }
        }

        int userIterator = currentShownUser.getOrDefault(userId, 0);

        if (direction.equals("next")) {
            userIterator++;
            if (userIterator >= users.size()) {
                userIterator = 0;
            }
        } else if (direction.equals("previous")) {
            userIterator--;
            if (userIterator < 0) {
                userIterator = users.size() - 1;
            }
        } else throw new RuntimeException("Неизвестное направление");

        currentShownUser.put(userId, userIterator);

        User user = userService.getUserById(users.get(userIterator).getUsername());
        user.setLoveSign(userLikes.get(userId).get(userIterator).getLoveSign());
        return user;
    }

    public List<User> refreshFavouritesList(Long userId) {
        List<UserDto> userDtos = userServiceWebClient
                .getUserFavourites(userId)
                .orElseThrow(() -> new RuntimeException("Вас никто не любит :("));
        userLikes.put(userId, userDtos
                .stream()
                .map((userDto ->
                        User.builder()
                                .id(userDto.getId())
                                .username(userDto.getUsertgid())
                                .gender(Gender.valueOf(userDto.getGender().toUpperCase()))
                                .loveSign(userDto.getLoveSign())
                                .build()))
                .collect(Collectors.toList()));
        return userLikes.get(userId);
    }
}
