package ru.liga.prerevolutionarytindertgbot.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.liga.prerevolutionarytindertgbot.bot.TelegramBotState;
import ru.liga.prerevolutionarytindertgbot.domain.user.User;
import ru.liga.prerevolutionarytindertgbot.service.UserDataCacheProxy;

import java.util.HashMap;
import java.util.Map;

import static ru.liga.prerevolutionarytindertgbot.bot.TelegramBotState.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserBotState {
    private final Map<Long, TelegramBotState> userBotState = new HashMap<>();
    private final UserDataCacheProxy userService;

    public void setUserBotState(long userId, TelegramBotState state) {
        log.info("setting state for userId({}): {}", userId, state);
        userBotState.put(userId, state);
    }

    public TelegramBotState getUserBotState(long userId) {
        TelegramBotState state = userBotState.getOrDefault(userId, UNKNOWN_USER);

        if (state.equals(UNKNOWN_USER)) {
            User user = userService.getUserById(userId);
            if (user.getGender() == null) {
                state = REGISTRATION_GENDER;
            } else if (user.getName() == null) {
                state = REGISTRATION_NAME;
            } else if (user.getHeader() == null || user.getDescription() == null) {
                state = REGISTRATION_PROFILE_INFO;
            } else if (user.getLookingFor() == null) {
                state = REGISTRATION_LOOKING_FOR;
            } else {
                state = REGISTRATION_COMPLETED;
            }
            setUserBotState(userId, state);
        }

        log.info("got state for userId({}): {}", userId, state);
        return state;
    }

    public boolean isUserRegistered(long userId) {
        switch (getUserBotState(userId)) {
            case IN_SEARCH:
            case VIEW_PROFILE:
            case VIEW_PAIRS:
            case REGISTRATION_COMPLETED:
                return true;
            default:
                return false;
        }
    }
}