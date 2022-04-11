package ru.liga.prerevolutionarytindertgbot;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.TestPropertySource;
import ru.liga.prerevolutionarytindertgbot.service.UserServiceWebClient;

@SpringBootTest
@TestPropertySource(locations = "/test.properties")
class PrerevolutionaryTinderTgBotApplicationTests {


	@Test
	void contextLoads() {
	}

}

