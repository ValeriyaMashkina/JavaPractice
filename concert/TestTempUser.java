package net.thumbtack.school.concert;
import com.google.gson.Gson;
import net.thumbtack.school.concert.dao.ReportDao;
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
import net.thumbtack.school.concert.service.ReportService;
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


public class TestTempUser {

  private final TempUser temporaryUserOnServer = new TempUser();
  private final Server server = new Server();
  private final UserService userService = new UserService();
  private final UserDao mockUserDao = mock(UserDao.class);
  private final ReportService reportService = new ReportService();
  private final ReportDao mockReportDao = mock(ReportDao.class);
  private final Gson gson = new Gson();


    @Before
    public void startServer() throws IOException {
        Database.getInstance().cleanBase();
        server.startServer("test.txt");
        userService.setUserDao(mockUserDao);
        reportService.setReportDao(mockReportDao);
        temporaryUserOnServer.setServer(server);
    }

    @Test
    void testTempUserInvalidPassword() throws ServerException {

        when(mockUserDao.registrateNewUser(any(User.class))).
                thenThrow(new ServerException(ServerErrorCode.INVALID_USER_PASSWORD));
        User user = new User("randomSurname", "randomName", "randomLogin", "randomPassword");
        try {
            temporaryUserOnServer.tempUserBehave(user);
        }

        catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_USER_PASSWORD, ex.getErrorCode());
        }
        verify(mockUserDao, never()).registrateNewUser(any(User.class));
        verify(mockReportDao, never()).showCurrentProgramm();
    }


    /*


    @Test
    void testTempUserInvalidSurname() throws ServerException {

        when(mockUserDao.registrateNewUser(any(User.class))).
                thenThrow(new ServerException(ServerErrorCode.INVALID_USER_SURNAME));

        User user = new User("randomSurname", "randomName", "randomLogin", "randomPassword");
        try {
            temporaryUserOnServer.tempUserBehave(user);
        }

        catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_USER_SURNAME, ex.getErrorCode());
        }
        verify(mockUserDao, never()).registrateNewUser(any(User.class));
        verify(mockReportDao, never()).showCurrentProgramm();
    }

    @Test
    void testTempUserUsedLogin() throws ServerException {
        when(mockUserDao.registrateNewUser(any(User.class))).
                thenThrow(new ServerException(ServerErrorCode.LOGIN_ALREADY_EXISTS));

        User user = new User("randomSurname", "randomName", "randomLogin", "randomPassword");
        try {
            temporaryUserOnServer.tempUserBehave(user);
        }

        catch (ServerException ex) {
            assertEquals(ServerErrorCode.LOGIN_ALREADY_EXISTS, ex.getErrorCode());
        }
        verify(mockUserDao, never()).registrateNewUser(any(User.class));
        verify(mockReportDao, never()).showCurrentProgramm();
    }


    @Test
    void testEmptyProgramm() throws ServerException {
        LinkedList<ProgrammRecord> currentProgramm = new LinkedList<ProgrammRecord>();
        when(mockUserDao.registrateNewUser(any(User.class))).thenReturn(gson.toJson(new SessionData(UUID.randomUUID(), true,
                any(User.class).getLogin())));
        when(mockReportDao.showCurrentProgramm()).thenReturn(currentProgramm);
        User user = new User("randomSurname", "randomName", "randomLogin", "randomPassword");
        String result = temporaryUserOnServer.tempUserBehave(user);
        assertEquals("0", result);
    }


    @Test
    void testFullProgramm() throws ServerException {
        LinkedList<ProgrammRecord> currentProgramm = new LinkedList<ProgrammRecord>();
        currentProgramm.add(new ProgrammRecord("songName", "composerName",
                "lyricAutorName", "singerName", "userWhoProposed",
                5.0, new LinkedHashSet<Comment>()));
        currentProgramm.add(new ProgrammRecord("songName", "composerName",
                "lyricAutorName", "singerName", "userWhoProposed",
                4.0, new LinkedHashSet<Comment>()));
        currentProgramm.add(new ProgrammRecord("songName", "composerName",
                "lyricAutorName", "singerName", "userWhoProposed",
                4.8, new LinkedHashSet<Comment>()));

        when(mockUserDao.registrateNewUser(any(User.class))).thenReturn(gson.toJson(new SessionData(UUID.randomUUID(), true,
                any(User.class).getLogin())));
        when(mockReportDao.showCurrentProgramm()).thenReturn(currentProgramm);
        User user = new User("randomSurname", "randomName", "randomLogin", "randomPassword");
        String result = temporaryUserOnServer.tempUserBehave(user);
        assertEquals("3", result);
    }

     */
}
