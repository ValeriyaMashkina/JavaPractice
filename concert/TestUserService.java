package net.thumbtack.school.concert;

import com.google.gson.Gson;
import net.thumbtack.school.concert.dto.LoginDto;
import net.thumbtack.school.concert.dto.RegisterUserDto;
import net.thumbtack.school.concert.errors.ServerErrorCode;
import net.thumbtack.school.concert.errors.ServerException;
import net.thumbtack.school.concert.model.*;
import net.thumbtack.school.concert.server.Server;
import net.thumbtack.school.concert.database.Database;
import net.jcip.annotations.NotThreadSafe;


import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@NotThreadSafe
public class TestUserService {

    @Test
    @Order(1)
    public void testRegisterUser() throws ServerException {
        Server ser = new Server();
        Gson gson = new Gson();
        RegisterUserDto userData = new RegisterUserDto("randomSurname", "randomName", "randomLogin", "randomPassword");
        String res = ser.registerUser(gson.toJson(userData));
        assertEquals(1, Database.getInstance().getUsersBase().size());
        assertTrue(Database.getInstance().getUsersBase().containsValue(new User("randomSurname", "randomName", "randomLogin", "randomPassword")));
        assertTrue(gson.fromJson(res, SessionData.class).getValid());
        assertEquals("randomLogin", gson.fromJson(res, SessionData.class).getCurrentUserLogin());
        assertNotEquals(null, gson.fromJson(res, SessionData.class).getToken());

        try {
            RegisterUserDto userData1 = new RegisterUserDto
                    ("randomSurname", "randomName", "randomLogin", "randomPassword");
            ser.registerUser(gson.toJson(userData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.LOGIN_ALREADY_EXISTS, ex.getErrorCode());
        }

        try {
            RegisterUserDto userData1 = new RegisterUserDto(null, "randomName", "randomLogin", "randomPassword");
            ser.registerUser(gson.toJson(userData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_USER_SURNAME, ex.getErrorCode());
        }


        try {
            RegisterUserDto userData1 = new RegisterUserDto(null, "randomName", "randomLogin2", "randomPassword");
            ser.registerUser(gson.toJson(userData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_USER_SURNAME, ex.getErrorCode());
        }

        try {
            RegisterUserDto userData1 = new RegisterUserDto("", "randomName", "randomLogin2", "randomPassword");
            ser.registerUser(gson.toJson(userData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_USER_SURNAME, ex.getErrorCode());
        }

        try {
            RegisterUserDto userData1 = new RegisterUserDto(" ", "randomName", "randomLogin2", "randomPassword");
            ser.registerUser(gson.toJson(userData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_USER_SURNAME, ex.getErrorCode());
        }

        try {
            RegisterUserDto userData1 = new RegisterUserDto("randomSurname", null, "randomLogin2", "randomPassword");
            ser.registerUser(gson.toJson(userData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_USER_NAME, ex.getErrorCode());
        }

        try {
            RegisterUserDto userData1 = new RegisterUserDto("randomSurname", "", "randomLogin2", "randomPassword");
            ser.registerUser(gson.toJson(userData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_USER_NAME, ex.getErrorCode());
        }

        try {
            RegisterUserDto userData1 = new RegisterUserDto("randomSurname", " ", "randomLogin2", "randomPassword");
            ser.registerUser(gson.toJson(userData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_USER_NAME, ex.getErrorCode());
        }


        try {
            RegisterUserDto userData1 = new RegisterUserDto("randomSurname", "randomName", null, "randomPassword");
            ser.registerUser(gson.toJson(userData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_USER_LOGIN, ex.getErrorCode());
        }

        try {
            RegisterUserDto userData1 = new RegisterUserDto("randomSurname", "randomName", "community", "randomPassword");
            ser.registerUser(gson.toJson(userData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_USER_LOGIN, ex.getErrorCode());
        }

        try {
            RegisterUserDto userData1 = new RegisterUserDto("randomSurname", "randomName", "", "randomPassword");
            ser.registerUser(gson.toJson(userData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_USER_LOGIN, ex.getErrorCode());
        }

        try {
            RegisterUserDto userData1 = new RegisterUserDto("randomSurname", "randomName", " ", "randomPassword");
            ser.registerUser(gson.toJson(userData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_USER_LOGIN, ex.getErrorCode());
        }

        try {
            RegisterUserDto userData1 = new RegisterUserDto("randomSurname", "randomName", "com", "randomPassword");
            ser.registerUser(gson.toJson(userData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_USER_LOGIN, ex.getErrorCode());
        }

        try {
            RegisterUserDto userData1 = new RegisterUserDto("randomSurname", "randomName", "randomLogin2", null);
            ser.registerUser(gson.toJson(userData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_USER_PASSWORD, ex.getErrorCode());
        }

        try {
            RegisterUserDto userData1 = new RegisterUserDto("randomSurname", "randomName", "randomLogin2", "");
            ser.registerUser(gson.toJson(userData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_USER_PASSWORD, ex.getErrorCode());
        }

        try {
            RegisterUserDto userData1 = new RegisterUserDto("randomSurname", "randomName", "randomLogin2", " ");
            ser.registerUser(gson.toJson(userData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_USER_PASSWORD, ex.getErrorCode());
        }

        try {
            RegisterUserDto userData1 = new RegisterUserDto("randomSurname", "randomName", "randomLogin2", "ran");
            ser.registerUser(gson.toJson(userData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_USER_PASSWORD, ex.getErrorCode());
        }

        Database.getInstance().cleanBase();
    }

    @Test
    @Order(2)
    public void testLoginUser() throws ServerException {

        Server ser = new Server();
        Gson gson = new Gson();
        ser.registerUser(gson.toJson(new RegisterUserDto("randomSurname", "randomName", "randomLogin", "randomPassword")));

        LoginDto userData = new LoginDto("randomLogin", "randomPassword");
        String res = ser.loginUser(gson.toJson(userData));

        assertNotEquals(null, gson.fromJson(res, SessionData.class).getToken());
        assertTrue(gson.fromJson(res, SessionData.class).getValid());
        assertEquals("randomLogin", gson.fromJson(res, SessionData.class).getCurrentUserLogin());

        try {
            LoginDto userData1 = new LoginDto(null, "randomPassword");
            ser.loginUser(gson.toJson(userData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_USER_LOGIN, ex.getErrorCode());
        }

        try {
            LoginDto userData1 = new LoginDto("community", "randomPassword");
            ser.loginUser(gson.toJson(userData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_USER_LOGIN, ex.getErrorCode());
        }

        try {
            LoginDto userData1 = new LoginDto("", "randomPassword");
            ser.loginUser(gson.toJson(userData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_USER_LOGIN, ex.getErrorCode());
        }

        try {
            LoginDto userData1 = new LoginDto(" ", "randomPassword");
            ser.loginUser(gson.toJson(userData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_USER_LOGIN, ex.getErrorCode());
        }

        try {
            LoginDto userData1 = new LoginDto("randomLogin", null);
            ser.loginUser(gson.toJson(userData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_USER_PASSWORD, ex.getErrorCode());
        }

        try {
            LoginDto userData1 = new LoginDto("randomLogin", "");
            ser.loginUser(gson.toJson(userData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_USER_PASSWORD, ex.getErrorCode());
        }

        try {
            LoginDto userData1 = new LoginDto("randomLogin", " ");
            ser.loginUser(gson.toJson(userData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_USER_PASSWORD, ex.getErrorCode());
        }

        try {
            LoginDto userData1 = new LoginDto("incorrectLogin", "randomPassword");
            ser.loginUser(gson.toJson(userData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.LOGIN_DOES_NOT_EXIST, ex.getErrorCode());
        }

        try {
            LoginDto userData1 = new LoginDto("randomLogin", "incorrectPassword");
            ser.loginUser(gson.toJson(userData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INCORRECT_PASSWORD, ex.getErrorCode());
        }


        Database.getInstance().cleanBase();
    }

    @Test
    @Order(3)
    public void testlogoutUser() throws ServerException {

        Server ser = new Server();
        Gson gson = new Gson();
        ser.registerUser(gson.toJson(new RegisterUserDto("randomSurname", "randomName", "randomLogin", "randomPassword")));
        String input = ser.loginUser(gson.toJson(new LoginDto("randomLogin", "randomPassword")));
        String res = ser.logoutUser(input);


        assertEquals(null, gson.fromJson(res, SessionData.class).getToken());
        assertTrue(!gson.fromJson(res, SessionData.class).getValid());
        assertEquals(null, gson.fromJson(res, SessionData.class).getCurrentUserLogin());

        Database.getInstance().cleanBase();
    }

    @Test
    @Order(4)
    public void testDeleteUser() throws ServerException {

        Server ser = new Server();
        Gson gson = new Gson();
        ser.registerUser(gson.toJson(new RegisterUserDto("randomSurname", "randomName", "randomLogin", "randomPassword")));
        String session = ser.loginUser(gson.toJson(new LoginDto("randomLogin", "randomPassword")));
        String res = ser.deleteUser(session);


        assertEquals(null, gson.fromJson(res, SessionData.class).getToken());
        assertTrue(!gson.fromJson(res, SessionData.class).getValid());
        assertEquals(null, gson.fromJson(res, SessionData.class).getCurrentUserLogin());
        assertEquals(0, Database.getInstance().getUsersBase().size());

        Database.getInstance().cleanBase();
    }

    @Test
    @Order(5)
    public void testDeleteUser2() throws ServerException {

        Server ser = new Server();
        Gson gson = new Gson();

        ser.registerUser(gson.toJson(new RegisterUserDto("randomSurname", "randomName", "randomLogin", "randomPassword")));

        Song song1 = new Song("Interstellar", "Brock", "Brock", "LeBrock",
                180, "rock-forever", new LinkedHashSet<Comment>(), new LinkedHashMap<String, SongRating>());
        Song song2 = new Song("song2", "song2", "song2", "song2",
                180, "randomLogin", new LinkedHashSet<Comment>(), new LinkedHashMap<String, SongRating>());
        Song song3 = new Song("song3", "song3", "song3", "song3",
                180, "randomLogin", new LinkedHashSet<Comment>(), new LinkedHashMap<String, SongRating>());
        Song song4 = new Song("song4", "song4", "song4", "song4",
                180, "randomLogin", new LinkedHashSet<Comment>(), new LinkedHashMap<String, SongRating>());

        Database.getInstance().addSong(song1);
        Database.getInstance().addSong(song2);
        Database.getInstance().addSong(song3);
        Database.getInstance().addSong(song4);

        Comment comment1 = new Comment("randomLogin", "nice", null);
        Comment comment2 = new Comment("randomLogin2", "nice", null);
        Database.getInstance().getSong(song1).addComment(comment1);
        Database.getInstance().getSong(song3).addComment(comment2);

        SongRating songRating1 = new SongRating("randomLogin", 5); // song1
        SongRating songRating2 = new SongRating("randomLogin2", 5); // song1
        Database.getInstance().getSong(song1).addRating(songRating1);
        Database.getInstance().getSong(song4).addRating(songRating2);

        String session = ser.loginUser(gson.toJson(new LoginDto("randomLogin", "randomPassword")));
        String res = ser.deleteUser(session);

        assertEquals(1, Database.getInstance().getSong(song1).getSongsComments().size());
        assertEquals(new Comment("community", "nice", null), Database.getInstance().getSong(song1).getComment("community"));
        assertEquals(0, Database.getInstance().getSong(song1).getRatings().size());

        assertFalse(Database.getInstance().checkSong("song2", "song2"));
        assertTrue(Database.getInstance().checkSong("song3", "song3"));
        assertTrue(Database.getInstance().checkSong("song4", "song4"));
        assertEquals(3, Database.getInstance().getSongsBase().size());
        assertEquals("community", Database.getInstance().getSong(song3).getUserWhoProposed());
        assertEquals("community", Database.getInstance().getSong(song4).getUserWhoProposed());
        Database.getInstance().cleanBase();
    }


}
