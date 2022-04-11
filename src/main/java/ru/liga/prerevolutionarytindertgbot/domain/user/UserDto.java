package ru.liga.prerevolutionarytindertgbot.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode
public class UserDto {
    private long id;
    private long usertgid;
    private String name;
    private String password;
    private String gender;
    private String header;
    private String lookingFor;
    private String formFileName;
    private String attachBase64Code;
    private String description;
    private String loveSign;
}