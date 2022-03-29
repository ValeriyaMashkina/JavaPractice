package net.thumbtack.school.concert;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import net.thumbtack.school.concert.dao.*;
import net.thumbtack.school.concert.database.Database;
import net.thumbtack.school.concert.dto.LoginDto;
import net.thumbtack.school.concert.dto.RegisterUserDto;
import net.thumbtack.school.concert.errors.ServerErrorCode;
import net.thumbtack.school.concert.errors.ServerException;
import net.thumbtack.school.concert.model.SessionData;
import net.thumbtack.school.concert.model.User;
import net.thumbtack.school.concert.server.Server;
import net.thumbtack.school.concert.service.*;
import org.junit.Before;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

/*


В базе данных MySQL создать список некоторых городов - столиц стран.
Написать метод, которые загружает этот список, для каждого города выводит страну
и национальную валюту используя сервис http://restcountries.eu
(например http://restcountries.eu/rest/v2/capital/london ). Написать тесты для этого метода.
 */

public class MockitoTestService {

  //  В рамках своего 11-го задания написать тесты для слоя сервисов,
  //  используя моки DAO слоя. Возможные исключения также протестировать.

    private final Server server = new Server();
    private final UserService userService = new UserService();
    private final UserDao mockUserDao = mock(UserDao.class);
    private final Gson gson = new Gson();

    @Before
    public void start() throws IOException {
        Database.getInstance().cleanBase();
        server.startServer("test.txt");
        userService.setUserDao(mockUserDao);
    }


    @Test
    public void testRegisterBadUser() throws ServerException
     {
         List<RegisterUserDto> variants = new LinkedList<RegisterUserDto>();
         variants.add(new RegisterUserDto(null, "randomName", "randomLogin", "randomPassword"));
         variants.add(new RegisterUserDto("", "randomName", "randomLogin2", "randomPassword"));
         variants.add(new RegisterUserDto(" ", "randomName", "randomLogin2", "randomPassword"));
         variants.add(new RegisterUserDto("randomSurname", null, "randomLogin2", "randomPassword"));
         variants.add(new RegisterUserDto("randomSurname", "randomName", "randomLogin2", null));

       for (RegisterUserDto v : variants)
       {
             try {userService.registerUser(v);} catch (ServerException ex) { }
       }

       verify(mockUserDao, never()).registrateNewUser(any(User.class));
    }

    @Test
    public void testRegisterUserLogin() throws ServerException {

        doThrow(new ServerException(ServerErrorCode.LOGIN_ALREADY_EXISTS))
                .when(mockUserDao).registrateNewUser(any(User.class));

        try {
            mockUserDao.registrateNewUser(new User  ("randomSurname", "randomName", "randomLogin", "randomPassword"));
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.LOGIN_ALREADY_EXISTS, ex.getErrorCode());
        }
        try {
            mockUserDao.registrateNewUser(new User  ("randomSurname", "randomName", "randomLogin", "randomPassword"));
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.LOGIN_ALREADY_EXISTS, ex.getErrorCode());
        }
    }


    @Test
    public void testLoginAndLogout() throws ServerException {

        when(mockUserDao.registrateNewUser(any(User.class))).thenReturn("resultReg");
        doReturn("resultLogIn").when(mockUserDao).loginUser(any(LoginDto.class));

        String errorText = gson.toJson(ServerErrorCode.LOGIN_DOES_NOT_EXIST);
        String resultReg = mockUserDao.registrateNewUser(new User  ("randomSurname", "randomName",
                "randomLogin", "randomPassword"));
        userService.logoutUser(new SessionData(UUID.randomUUID(), true, "randomLogin"));
        String resultLogIn = mockUserDao.loginUser(new LoginDto("randomLogin", "randomPassword"));

        assertAll(
                () -> assertEquals("resultReg", resultReg),
                () -> assertEquals("resultLogIn", resultLogIn),
                () -> assertNotEquals(errorText, resultLogIn)
        );
    }


    @Test
    public void testLoginDoesNotExist() throws ServerException {

        when(mockUserDao.registrateNewUser(any(User.class))).thenReturn("resultReg");

        when(mockUserDao.loginUser(any(LoginDto.class)))
                .thenThrow(new ServerException(ServerErrorCode.LOGIN_DOES_NOT_EXIST));

        doReturn("resultLogIn").when(mockUserDao).loginUser(any(LoginDto.class));


        String resultReg = mockUserDao.registrateNewUser(new User  ("randomSurname", "randomName",
                "randomLogin", "randomPassword"));
        userService.logoutUser(new SessionData(UUID.randomUUID(), true, "randomLogin"));

        try {
            mockUserDao.registrateNewUser(new User  ("randomSurname", "randomName",
                    "randomLogin1", "randomPassword"));
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.LOGIN_DOES_NOT_EXIST, ex.getErrorCode());
        }

        try {
            mockUserDao.registrateNewUser(new User  ("randomSurname", "randomName",
                    "randomLogin2", "randomPassword"));
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.LOGIN_DOES_NOT_EXIST, ex.getErrorCode());
        }
    }

    @Test
    public void testRegisterEmployee() throws ServerException {

       User user = new User  ("randomSurname", "randomName", "randomLogin", "randomPassword");

        when(mockUserDao.registrateNewUser(any(User.class))).thenReturn("resultReg");

        mockUserDao.registrateNewUser(user);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        verify(mockUserDao).registrateNewUser(userCaptor.capture());

        assertEquals(user, userCaptor.getValue());
    }


}

/*




*/
