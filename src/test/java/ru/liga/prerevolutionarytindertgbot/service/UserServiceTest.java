package ru.liga.prerevolutionarytindertgbot.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.liga.prerevolutionarytindertgbot.domain.user.User;
import ru.liga.prerevolutionarytindertgbot.domain.user.UserDto;

import java.util.Base64;
import java.util.Optional;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static ru.liga.prerevolutionarytindertgbot.enums.Gender.MALE;
import static ru.liga.prerevolutionarytindertgbot.enums.LookingFor.FEMALES;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "/test.properties")
class UserServiceTest {

    @MockBean
    private UserServiceWebClient userServiceWebClient;
    @Autowired
    private UserService userService;


    @Test
    public void whenGettingUser_thenUserReturned() {
        long username = 1234;

        Mockito.when(userServiceWebClient.getUserByUsername(username))
                .thenReturn(
                        Optional.of(
                                UserDto.builder()
                                        .id(1)
                                        .usertgid(username)
                                        .name("Василий")
                                        .gender("male")
                                        .lookingFor("females")
                                        .header("Мущина")
                                        .description("в самом расцвете сил")
                                        .attachBase64Code("4iZisZ0k8p9oJEIGM7EYTg==")
                                        .build()
                        )
                );

        assertEquals("Получение пользователя работает некорректно: ",
                User.builder()
                        .id(1)
                        .username(1234)
                        .name("Василий")
                        .gender(MALE)
                        .lookingFor(FEMALES)
                        .header("Мущина")
                        .description("в самом расцвете сил")
                        .profileImage(Base64.getDecoder().decode("4iZisZ0k8p9oJEIGM7EYTg=="))
                        .build()
                , userService.getUserById(username));
    }


    @Test
    public void whenUserNotFound_thenReturnedUserWithId() {
        long username = 1234;

        Mockito.when(userServiceWebClient.getUserByUsername(username))
                .thenReturn(
                        Optional.empty()
                );

        assertEquals("Вернулся некорректный пользователь: ",
                User.builder().username(username).build(),
                userService.getUserById(username));
    }

    @Test
    public void whenRegisteringUser_ThenUserRegistered() {
        User user = User.builder()
                .username(1234)
                .name("Василий")
                .gender(MALE)
                .lookingFor(FEMALES)
                .header("Мущина")
                .description("в самом расцвете сил")
                .build();

        UserDto userDto = UserDto.builder()
                .id(0)
                .usertgid(1234)
                .name("Василий")
                .gender("male")
                .lookingFor("females")
                .header("Мущина")
                .description("в самом расцвете сил")
                .password("12344")
                .build();

        Mockito.when(userServiceWebClient.createUser(userDto))
                .thenReturn(
                        UserDto.builder()
                                .id(1)
                                .usertgid(1234)
                                .name("Василiй")
                                .gender("male")
                                .lookingFor("females")
                                .header("Мущiна")
                                .description("в самом расцвете силъ")
                                .attachBase64Code("4iZisZ0k8p9oJEIGM7EYTg==")
                                .password("12344")
                                .build()
                );

        assertTrue("Регистрация пользователя не была завершена успешно", userService.registerUser(user));
        assertEquals("Имя пользователя не изменилось", "Василiй", user.getName());
        assertEquals("Заголовок профиля не изменился", "Мущiна", user.getHeader());
        assertEquals("Тело профиля не изменилось", "в самом расцвете силъ", user.getDescription());
        assertEquals("Фото профиля не было сохранено", Base64.getDecoder().decode("4iZisZ0k8p9oJEIGM7EYTg=="), user.getProfileImage());
    }


    @Test
    public void whenUpdatingUser_thenUserUpdated() {
        User user = User.builder()
                .username(1234)
                .name("Василий")
                .gender(MALE)
                .lookingFor(FEMALES)
                .header("Мущина")
                .description("в самом расцвете сил")
                .build();

        UserDto userDto = UserDto.builder()
                .id(0)
                .usertgid(1234)
                .name("Василий")
                .gender("male")
                .lookingFor("females")
                .header("Мущина")
                .description("в самом расцвете сил")
                .password("12344")
                .build();

        Mockito.when(userServiceWebClient.updateUser(userDto))
                .thenReturn(
                        UserDto.builder()
                                .id(1)
                                .usertgid(1234)
                                .name("Василiй")
                                .gender("male")
                                .lookingFor("females")
                                .header("Мущiна")
                                .description("в самом расцвете силъ")
                                .attachBase64Code("4iZisZ0k8p9oJEIGM7EYTg==")
                                .password("12344")
                                .build()
                );

        assertTrue("Регистрация пользователя не была завершена успешно", userService.updateUser(user));
        assertEquals("Имя пользователя не изменилось", "Василiй", user.getName());
        assertEquals("Заголовок профиля не изменился", "Мущiна", user.getHeader());
        assertEquals("Тело профиля не изменилось", "в самом расцвете силъ", user.getDescription());
        assertEquals("Фото профиля не было сохранено", Base64.getDecoder().decode("4iZisZ0k8p9oJEIGM7EYTg=="), user.getProfileImage());
    }
}