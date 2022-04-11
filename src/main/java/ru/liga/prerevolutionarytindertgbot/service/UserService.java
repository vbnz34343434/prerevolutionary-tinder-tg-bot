package ru.liga.prerevolutionarytindertgbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.liga.prerevolutionarytindertgbot.domain.user.User;
import ru.liga.prerevolutionarytindertgbot.domain.user.UserDto;
import ru.liga.prerevolutionarytindertgbot.enums.Gender;
import ru.liga.prerevolutionarytindertgbot.enums.LookingFor;

import java.util.Base64;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserServiceWebClient userServiceWebClient;

    public User getUserById(long userId) {
        Optional<UserDto> dto = userServiceWebClient.getUserByUsername(userId);

        if (dto.isEmpty()) {
            return User.builder()
                    .username(userId)
                    .build();
        } else {
            UserDto userDto = dto.get();
            return User.builder()
                    .id(userDto.getId())
                    .username(userDto.getUsertgid())
                    .name(userDto.getName())
                    .gender(Gender.valueOf(userDto.getGender().toUpperCase()))
                    .lookingFor(LookingFor.valueOf(userDto.getLookingFor().toUpperCase()))
                    .header(userDto.getHeader())
                    .description(userDto.getDescription())
                    .profileImage(Base64.getDecoder().decode(userDto.getAttachBase64Code()))
                    .build();
        }
    }

    public boolean registerUser(User user) {
        assert user.getName() != null : "Не заполнено имя пользователя";
        assert user.getGender() != null : "Не заполнен пол пользователя";
        assert user.getHeader() != null : "Не заполнена информация о пользователе";
        assert user.getLookingFor() != null : "Не заполнено информация о том, кого ищет польователь";

        UserDto request = UserDto.builder()
                .usertgid(user.getUsername())
                .name(user.getName())
                .gender(user.getGender().toString().toLowerCase())
                .header(user.getHeader())
                .description(user.getDescription())
                .lookingFor(user.getLookingFor().toString().toLowerCase())
                .password("12344")
                .build();

        UserDto response = userServiceWebClient.createUser(request);
        user.setName(response.getName());
        user.setHeader(response.getHeader());
        user.setDescription(response.getDescription());
        user.setProfileImage(Base64.getDecoder().decode(response.getAttachBase64Code()));
        return true;
    }

    public boolean updateUser(User user) {
        UserDto request = UserDto.builder()
                .id(user.getId())
                .usertgid(user.getUsername())
                .name(user.getName())
                .gender(user.getGender().toString().toLowerCase())
                .header(user.getHeader())
                .description(user.getDescription())
                .lookingFor(user.getLookingFor().toString().toLowerCase())
                .password("12344")
                .build();
        UserDto response = userServiceWebClient.updateUser(request);
        user.setName(response.getName());
        user.setHeader(response.getHeader());
        user.setDescription(response.getDescription());
        user.setProfileImage(Base64.getDecoder().decode(response.getAttachBase64Code()));
        return true;
    }
}
