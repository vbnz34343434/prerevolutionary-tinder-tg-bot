package ru.liga.prerevolutionarytindertgbot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.liga.prerevolutionarytindertgbot.domain.user.User;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class UserDataCacheProxy {
    private final UserService userService;
    protected final Map<Long, User> cachedUsers = new HashMap<>();

    public UserDataCacheProxy(UserService userService) {
        this.userService = userService;
    }

    public User getUserById(long userId) {
        if (!cachedUsers.containsKey(userId)) {
            cachedUsers.put(userId, userService.getUserById(userId));
            log.info("got user ({}) from service: {}", userId, cachedUsers.get(userId));
        } else {
            log.info("got user ({}) from cache: {}", userId, cachedUsers.get(userId));
        }

        return cachedUsers.get(userId);
    }
}