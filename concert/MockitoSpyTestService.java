package net.thumbtack.school.concert;

import com.google.gson.Gson;
import net.thumbtack.school.concert.dao.UserDao;
import net.thumbtack.school.concert.daoImpl.UserDaoImpl;
import net.thumbtack.school.concert.database.Database;
import net.thumbtack.school.concert.dto.LoginDto;
import net.thumbtack.school.concert.dto.RegisterUserDto;
import net.thumbtack.school.concert.errors.ServerErrorCode;
import net.thumbtack.school.concert.errors.ServerException;
import net.thumbtack.school.concert.model.SessionData;
import net.thumbtack.school.concert.model.User;
import net.thumbtack.school.concert.server.Server;
import net.thumbtack.school.concert.service.UserService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class MockitoSpyTestService {
    private final Gson gson = new Gson();
    private final Server server = new Server();
    private final UserService userService = new UserService();
    private UserDao spyUserDao = spy(UserDao.class);


    @Before
    public void start() throws IOException {
        Database.getInstance().cleanBase();
        server.startServer("test.txt");
        userService.setUserDao(spyUserDao);
    }

    @Test
    public void testRegisterUser() throws ServerException {
        User user = new User  ("randomSurname", "randomName",
                "randomLogin", "randomPassword");
        when(spyUserDao.registrateNewUser(user)).thenThrow(new ServerException(ServerErrorCode.LOGIN_ALREADY_EXISTS));
        RegisterUserDto userDto1 = new RegisterUserDto("randomSurname", "randomName",
                "randomLogin", "randomPassword");
        userService.registerUser(userDto1);


        RegisterUserDto userDto2 = new RegisterUserDto("randomSurname", "randomName",
                "randomLogin", "randomPassword");

        try {
            userService.registerUser(userDto2);;
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.LOGIN_ALREADY_EXISTS, ex.getErrorCode());
        }
    }


    @Test
    public void testLoginUser() throws ServerException {

        when(spyUserDao.loginUser(any(LoginDto.class))).thenThrow(new ServerException(ServerErrorCode.LOGIN_DOES_NOT_EXIST));
        LoginDto userDto = new LoginDto("randomLogin", "randomPassword");

        try {
            userService.loginUser(userDto);
            ;
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.LOGIN_DOES_NOT_EXIST, ex.getErrorCode());
        }
    }
}