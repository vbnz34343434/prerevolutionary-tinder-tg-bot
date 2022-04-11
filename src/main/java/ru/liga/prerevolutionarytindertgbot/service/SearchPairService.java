package ru.liga.prerevolutionarytindertgbot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.liga.prerevolutionarytindertgbot.cache.UserSearchState;
import ru.liga.prerevolutionarytindertgbot.domain.user.User;
import ru.liga.prerevolutionarytindertgbot.domain.user.UserDto;
import ru.liga.prerevolutionarytindertgbot.domain.user.UserPageableResponse;
import ru.liga.prerevolutionarytindertgbot.enums.Gender;

import java.util.Base64;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchPairService {

    private final UserSearchState userSearchState;
    private final UserServiceWebClient userServiceWebClient;
    protected final Map<Long, Long> userShownUser;

    public User searchNext(long userId, boolean needNext) {
        int currentPage = userSearchState.getCurrentPage(userId);
        UserPageableResponse users = userServiceWebClient.getUsers(userId, currentPage, 1);

        if (users.getTotalElements() == 0) {
            throw new RuntimeException("Больше нет пользователей, которых можно просмотреть!");
        }

        if (users.hasNext()) {
            userSearchState.setNextPage(userId, currentPage + (needNext ? 1 : 0));
        } else {
            userSearchState.setNextPage(userId, 0);
        }

        UserDto userResponse = users.getContent().get(0);

        User shownUser = User.builder()
                .username(userResponse.getUsertgid())
                .name(userResponse.getName())
                .gender(Gender.valueOf(userResponse.getGender().toUpperCase()))
                .profileImage(Base64.getDecoder().decode(userResponse.getAttachBase64Code()))
                .build();

        userShownUser.put(userId, shownUser.getUsername());
        return shownUser;
    }

    public void likeCurrentUser(long userId) {
        long currentUser = userShownUser.get(userId);
        userServiceWebClient.likeUser(userId, currentUser);
    }
}
