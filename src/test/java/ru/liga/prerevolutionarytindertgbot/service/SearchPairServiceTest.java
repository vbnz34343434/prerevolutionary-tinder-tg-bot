package ru.liga.prerevolutionarytindertgbot.service;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.liga.prerevolutionarytindertgbot.cache.UserSearchState;
import ru.liga.prerevolutionarytindertgbot.domain.user.User;
import ru.liga.prerevolutionarytindertgbot.domain.user.UserDto;
import ru.liga.prerevolutionarytindertgbot.domain.user.UserPageableResponse;
import ru.liga.prerevolutionarytindertgbot.enums.Gender;

import java.util.Base64;
import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "/test.properties")
class SearchPairServiceTest {

    @MockBean
    private UserServiceWebClient userServiceWebClient;

    @MockBean
    private UserService userService;

    @Autowired
    private SearchPairService searchPairService;

    @Autowired
    private UserSearchState userSearchState;

    @Test
    public void whenLikeUserCalled_thenServiceCalled() {
        searchPairService.userShownUser.put(1234L, 4321L);
        searchPairService.likeCurrentUser(1234);
        Mockito.verify(userServiceWebClient).likeUser(1234, 4321);
    }

    @Test
    public void whenSearchNextCalled_thenUserServiceCalled() {

        Mockito.when(userServiceWebClient.getUsers(1234, 0, 1))
                .thenReturn(new UserPageableResponse(List.of(
                                UserDto.builder()
                                        .usertgid(1234)
                                        .name("Василий")
                                        .gender("male")
                                        .attachBase64Code("4iZisZ0k8p9oJEIGM7EYTg==")
                                        .build()
                        ),
                                1,
                                1,
                                1L,
                                JsonNodeFactory.instance.objectNode(),
                                true,
                                1,
                                JsonNodeFactory.instance.objectNode(),
                                true,
                                1
                        )
                );


        User actualUser = searchPairService.searchNext(1234, true);

        Mockito.verify(userServiceWebClient).getUsers(1234, 0, 1);

        assertEquals("Вернулся некорректный пользователь",
                User.builder()
                        .username(1234)
                        .name("Василий")
                        .gender(Gender.MALE)
                        .profileImage(Base64.getDecoder().decode("4iZisZ0k8p9oJEIGM7EYTg=="))
                        .build(),
                actualUser);

        assertEquals("некорректное состояние поиска", 0, userSearchState.getCurrentPage(1234));
    }

    @Test
    public void whenSearchNextCalledTwoTimes_thenReturnedTrueUser() {
        Mockito.when(userServiceWebClient.getUsers(1234, 0, 1))
                .thenReturn(new UserPageableResponse(List.of(UserDto.builder().usertgid(4444).name("Василий").gender("male").attachBase64Code("4iZisZ0k8p9oJEIGM7EYTg==").build()),
                                0,
                                1,
                                2L,
                                JsonNodeFactory.instance.objectNode(),
                                false,
                                2,
                                JsonNodeFactory.instance.objectNode(),
                                true,
                                1
                        )
                );

        Mockito.when(userServiceWebClient.getUsers(1234, 1, 1))
                .thenReturn(new UserPageableResponse(List.of(UserDto.builder().usertgid(5555).name("Фёдор").gender("male").attachBase64Code("4iZisZ0k8p9oJEIGM7EYTg==").build()),
                                1,
                                1,
                                2L,
                                JsonNodeFactory.instance.objectNode(),
                                true,
                                2,
                                JsonNodeFactory.instance.objectNode(),
                                false,
                                1
                        )
                );

        User actualUser1 = searchPairService.searchNext(1234L, true);
        Mockito.verify(userServiceWebClient, Mockito.atMostOnce()).getUsers(1234, 0, 1);
        assertEquals("некорректное состояние поиска", 1, userSearchState.getCurrentPage(1234));

        assertEquals("В первом вызове Вернулся некорректный пользователь",
                User.builder()
                        .username(4444)
                        .name("Василий")
                        .gender(Gender.MALE)
                        .profileImage(Base64.getDecoder().decode("4iZisZ0k8p9oJEIGM7EYTg=="))
                        .build(),
                actualUser1);


        User actualUser2 = searchPairService.searchNext(1234L, true);
        Mockito.verify(userServiceWebClient, Mockito.atMostOnce()).getUsers(1234, 1, 1);
        assertEquals("некорректное состояние поиска", 0, userSearchState.getCurrentPage(1234));

        assertEquals("Во втором вызове Вернулся некорректный пользователь",
                User.builder()
                        .username(5555)
                        .name("Фёдор")
                        .gender(Gender.MALE)
                        .profileImage(Base64.getDecoder().decode("4iZisZ0k8p9oJEIGM7EYTg=="))
                        .build(),
                actualUser2);
    }

    @Test
    public void whenReturnedEmptyContentPage_thenExceptionThrown() {
        Mockito.when(userServiceWebClient.getUsers(1234, 0, 1))
                .thenReturn(new UserPageableResponse(List.of(),
                                0,
                                1,
                                0L,
                                JsonNodeFactory.instance.objectNode(),
                                false,
                                0,
                                JsonNodeFactory.instance.objectNode(),
                                true,
                                0
                        )
                );

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> searchPairService.searchNext(1234L, true));
        assertEquals("некорректное сообщение об ошибке", "Больше нет пользователей, которых можно просмотреть!", exception.getMessage());
    }

    @Test
    public void whenNavigatingToOutOfBoundPage_thenReturnedResultOfFirstPage() {
        Mockito.when(userServiceWebClient.getUsers(1234, 0, 1))
                .thenReturn(new UserPageableResponse(List.of(UserDto.builder().usertgid(4444).name("Василий").gender("male").attachBase64Code("4iZisZ0k8p9oJEIGM7EYTg==").build()),
                                0,
                                1,
                                1L,
                                JsonNodeFactory.instance.objectNode(),
                                false,
                                1,
                                JsonNodeFactory.instance.objectNode(),
                                true,
                                1
                        )
                );

        User actualUser1 = searchPairService.searchNext(1234L, true);
        User actualUser2 = searchPairService.searchNext(1234L, true);

        Mockito.verify(userServiceWebClient, Mockito.times(2)).getUsers(1234, 0, 1);

        assertEquals("некорректный переход к пользователю", User.builder()
                        .username(4444)
                        .name("Василий")
                        .gender(Gender.MALE)
                        .profileImage(Base64.getDecoder().decode("4iZisZ0k8p9oJEIGM7EYTg=="))
                        .build()
                , actualUser1);
        assertEquals("некорректный пользователь", actualUser1, actualUser2);
    }
}