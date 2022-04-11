package ru.liga.prerevolutionarytindertgbot.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserSearchState {
    private final Map<Long, Integer> userSearchPageState;

    public int getCurrentPage(long userId) {
        return userSearchPageState.getOrDefault(userId, 0);
    }

    public void setNextPage(long userId, int page) {
        userSearchPageState.put(userId, page);
    }
}