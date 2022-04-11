package ru.liga.prerevolutionarytindertgbot.domain.user;

import lombok.*;
import org.springframework.stereotype.Component;
import ru.liga.prerevolutionarytindertgbot.enums.Gender;
import ru.liga.prerevolutionarytindertgbot.enums.LookingFor;

import java.util.Arrays;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class User {
    private long id;
    private long username;
    private String name;
    private Gender gender;
    private String header;
    private String description;
    private LookingFor lookingFor;
    private byte[] profileImage;
    private String loveSign;

    @Override
    public String toString() {
        return "User{" +
               "id=" + id +
               ", username=" + username +
               ", name='" + name + '\'' +
               ", gender=" + gender +
               ", header='" + header + '\'' +
               ", description='" + description + '\'' +
               ", lookingFor=" + lookingFor +
               ", profileImage=" + Arrays.toString(profileImage) +
               ", loveSign='" + loveSign + '\'' +
               '}';
    }
}
