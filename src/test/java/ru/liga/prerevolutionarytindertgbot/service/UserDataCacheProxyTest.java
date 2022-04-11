package ru.liga.prerevolutionarytindertgbot.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "/test.properties")
class UserDataCacheProxyTest {

    @MockBean
    private UserService userService;

    @Autowired
    private UserDataCacheProxy userDataCacheProxy;

    @Test
    public void whenGettingUserThatNotExistsInCache_thenUserServiceCalled() {
        userDataCacheProxy.cachedUsers.clear();
        userDataCacheProxy.getUserById(1234);
        Mockito.verify(userService).getUserById(1234);
    }

    @Test
    public void whenGettingUserThatExistsInCache_thenSecondCallGotUserFromCache() {

        userDataCacheProxy.getUserById(1234);
        userDataCacheProxy.getUserById(1234);

        Mockito.verify(userService, Mockito.atMostOnce())
                .getUserById(1234);
    }

}