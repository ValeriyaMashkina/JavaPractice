package net.thumbtack.school.concert;

import com.google.gson.Gson;
import net.thumbtack.school.concert.daoImpl.SongRatingDaoImpl;
import net.thumbtack.school.concert.database.Database;
import net.thumbtack.school.concert.dto.*;
import net.thumbtack.school.concert.errors.ServerErrorCode;
import net.thumbtack.school.concert.errors.ServerException;
import net.thumbtack.school.concert.model.*;
import net.thumbtack.school.concert.server.Server;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.nio.file.Files;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import net.jcip.annotations.NotThreadSafe;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

@NotThreadSafe

public class TestSongRatingService {

    @Test
    @Order(1)
    public void testAddSongRating() throws ServerException {
        Server ser = new Server();
        Gson gson = new Gson();

        AddSongDto songData = new AddSongDto(new SessionData(UUID.randomUUID(), true, "randomLogin"),
                "randomSongName", "randomComposerName", "randomLyricAutorName",
                "randomSingerName", 180);

        ser.addSong(gson.toJson(songData));
        assertEquals(1, Database.getInstance().getSongsBase().size());
        AddSongRatingDto songRatingData = new AddSongRatingDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                "randomSongName", "randomSingerName", 4);
        ser.addSongRating(gson.toJson(songRatingData));
        assertEquals(2, Database.getInstance().getSong("randomSongName", "randomSingerName").getRatings().size());
        assertTrue(Database.getInstance().getSong("randomSongName", "randomSingerName")
                .getSongRating("randomLogin1").equals(new SongRating("randomLogin1", 4)));


        try {
            AddSongRatingDto songRatingData1 = new AddSongRatingDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                    "randomSongName", "randomSingerName", 1);
            ser.addSongRating(gson.toJson(songRatingData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.USER_ALREADY_RATED, ex.getErrorCode());
        }


        try {
            AddSongRatingDto songRatingData1 = new AddSongRatingDto(new SessionData(UUID.randomUUID(), true, "randomLogin2"),
                    "randomSongName1", "randomSingerName", 5);
            ser.addSongRating(gson.toJson(songRatingData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.SONG_DOES_NOT_EXISTS, ex.getErrorCode());
        }


        try {
            AddSongRatingDto songRatingData1 = new AddSongRatingDto(new SessionData(null, true, "randomLogin2"),
                    "randomSongName", "randomSingerName", 5);
            ser.addSongRating(gson.toJson(songRatingData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            AddSongRatingDto songRatingData1 = new AddSongRatingDto(new SessionData(UUID.randomUUID(), false, "randomLogin2"),
                    "randomSongName", "randomSingerName", 5);
            ser.addSongRating(gson.toJson(songRatingData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }


        try {
            AddSongRatingDto songRatingData1 = new AddSongRatingDto(new SessionData(UUID.randomUUID(), true, null),
                    "randomSongName", "randomSingerName", 5);
            ser.addSongRating(gson.toJson(songRatingData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }


        try {
            AddSongRatingDto songRatingData1 = new AddSongRatingDto(new SessionData(UUID.randomUUID(), true, "community"),
                    "randomSongName", "randomSingerName", 5);
            ser.addSongRating(gson.toJson(songRatingData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }


        try {
            AddSongRatingDto songRatingData1 = new AddSongRatingDto(new SessionData(UUID.randomUUID(), true, " "),
                    "randomSongName", "randomSingerName", 5);
            ser.addSongRating(gson.toJson(songRatingData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            AddSongRatingDto songRatingData1 = new AddSongRatingDto(new SessionData(UUID.randomUUID(), true, ""),
                    "randomSongName", "randomSingerName", 5);
            ser.addSongRating(gson.toJson(songRatingData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }


        try {
            AddSongRatingDto songRatingData1 = new AddSongRatingDto(new SessionData(UUID.randomUUID(), true, "randomLogin2"),
                    null, "randomSingerName", 5);
            ser.addSongRating(gson.toJson(songRatingData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SONG_NAME, ex.getErrorCode());
        }

        try {
            AddSongRatingDto songRatingData1 = new AddSongRatingDto(new SessionData(UUID.randomUUID(), true, "randomLogin2"),
                    "", "randomSingerName", 5);
            ser.addSongRating(gson.toJson(songRatingData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SONG_NAME, ex.getErrorCode());
        }

        try {
            AddSongRatingDto songRatingData1 = new AddSongRatingDto(new SessionData(UUID.randomUUID(), true, "randomLogin2"),
                    " ", "randomSingerName", 5);
            ser.addSongRating(gson.toJson(songRatingData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SONG_NAME, ex.getErrorCode());
        }

        try {
            AddSongRatingDto songRatingData1 = new AddSongRatingDto(new SessionData(UUID.randomUUID(), true, "randomLogin2"),
                    "randomSongName", "randomSingerName", 0);
            ser.addSongRating(gson.toJson(songRatingData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_RATING, ex.getErrorCode());
        }


        try {
            AddSongRatingDto songRatingData1 = new AddSongRatingDto(new SessionData(UUID.randomUUID(), true, "randomLogin2"),
                    "randomSongName", "randomSingerName", 6);
            ser.addSongRating(gson.toJson(songRatingData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_RATING, ex.getErrorCode());
        }

        try {
            AddSongRatingDto songRatingData1 = new AddSongRatingDto(new SessionData(UUID.randomUUID(), true, "randomLogin2"),
                    "randomSongName", "randomSingerName", -1);
            ser.addSongRating(gson.toJson(songRatingData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_RATING, ex.getErrorCode());
        }


        try {
            AddSongRatingDto songRatingData1 = new AddSongRatingDto(new SessionData(UUID.randomUUID(), true, "randomLogin2"),
                    "randomSongName", null, 5);
            ser.addSongRating(gson.toJson(songRatingData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }


        try {
            AddSongRatingDto songRatingData1 = new AddSongRatingDto(new SessionData(UUID.randomUUID(), true, "randomLogin2"),
                    "randomSongName", "", 5);
            ser.addSongRating(gson.toJson(songRatingData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            AddSongRatingDto songRatingData1 = new AddSongRatingDto(new SessionData(UUID.randomUUID(), true, "randomLogin2"),
                    "randomSongName", " ", 5);
            ser.addSongRating(gson.toJson(songRatingData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            AddSongRatingDto songRatingData1 = new AddSongRatingDto(new SessionData(UUID.randomUUID(), true, "randomLogin2"),
                    "randomSongName", "mbdobdrohbdrfnlmoxmbxdbfmojxxxxxxxxxxxxxxxxxxxxxxnbjjjjjjjjjjjjjjj", 5);
            ser.addSongRating(gson.toJson(songRatingData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            AddSongRatingDto songRatingData1 = new AddSongRatingDto(new SessionData(UUID.randomUUID(), true, "randomLogin2"),
                    "randomSongName", "random,random", 5);
            ser.addSongRating(gson.toJson(songRatingData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            AddSongRatingDto songRatingData1 = new AddSongRatingDto(new SessionData(UUID.randomUUID(), true, "randomLogin2"),
                    "randomSongName", "random&random", 5);
            ser.addSongRating(gson.toJson(songRatingData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            AddSongRatingDto songRatingData1 = new AddSongRatingDto(new SessionData(UUID.randomUUID(), true, "randomLogin2"),
                    "randomSongName", "random and random", 5);
            ser.addSongRating(gson.toJson(songRatingData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }


        try {
            AddSongRatingDto songRatingData1 = new AddSongRatingDto(new SessionData(UUID.randomUUID(), true, "randomLogin2"),
                    "randomSongName", "random и random", 5);
            ser.addSongRating(gson.toJson(songRatingData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        Database.getInstance().cleanBase();
    }

    @Test
    @Order(2)
    public void changeSongRating() throws ServerException {
        Server ser = new Server();
        Gson gson = new Gson();
        AddSongDto songData = new AddSongDto(new SessionData(UUID.randomUUID(), true, "randomLogin"),
                "randomSongName", "randomComposerName", "randomLyricAutorName",
                "randomSingerName", 180);
        ser.addSong(gson.toJson(songData));
        assertEquals(1, Database.getInstance().getSongsBase().size());
        AddSongRatingDto songRatingData = new AddSongRatingDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                "randomSongName", "randomSingerName", 4);
        ser.addSongRating(gson.toJson(songRatingData));
        assertEquals(2, Database.getInstance().getSong("randomSongName", "randomSingerName").getRatings().size());
        assertTrue(Database.getInstance().getSong("randomSongName", "randomSingerName")
                .getSongRating("randomLogin1").equals(new SongRating("randomLogin1", 4)));

        ChangeSongRatingDto songRatingDataUpdate = new ChangeSongRatingDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                "randomSongName", "randomSingerName", 5);
        ser.changeSongRating(gson.toJson(songRatingDataUpdate));
        assertEquals(2, Database.getInstance().getSong("randomSongName", "randomSingerName").getRatings().size());
        assertTrue(Database.getInstance().getSong("randomSongName", "randomSingerName")
                .getSongRating("randomLogin1").equals(new SongRating("randomLogin1", 5)));


        try {
            ChangeSongRatingDto songRatingDataUpdate1 = new ChangeSongRatingDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                    "randomSongName", "randomSingerName", 0);
            ser.changeSongRating(gson.toJson(songRatingDataUpdate1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_RATING, ex.getErrorCode());
        }

        try {
            ChangeSongRatingDto songRatingDataUpdate1 = new ChangeSongRatingDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                    "randomSongName", "randomSingerName", -1);
            ser.changeSongRating(gson.toJson(songRatingDataUpdate1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_RATING, ex.getErrorCode());
        }

        try {
            ChangeSongRatingDto songRatingDataUpdate1 = new ChangeSongRatingDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                    "randomSongName", "randomSingerName", 10);
            ser.changeSongRating(gson.toJson(songRatingDataUpdate1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_RATING, ex.getErrorCode());
        }


        try {
            ChangeSongRatingDto songRatingDataUpdate1 = new ChangeSongRatingDto(new SessionData(null, true, "randomLogin1"),
                    "randomSongName", "randomSingerName", 3);
            ser.changeSongRating(gson.toJson(songRatingDataUpdate1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            ChangeSongRatingDto songRatingDataUpdate1 = new ChangeSongRatingDto(new SessionData(UUID.randomUUID(), false, "randomLogin1"),
                    "randomSongName", "randomSingerName", 3);
            ser.changeSongRating(gson.toJson(songRatingDataUpdate1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            ChangeSongRatingDto songRatingDataUpdate1 = new ChangeSongRatingDto(new SessionData(UUID.randomUUID(), true,
                    null),
                    "randomSongName", "randomSingerName", 3);
            ser.changeSongRating(gson.toJson(songRatingDataUpdate1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            ChangeSongRatingDto songRatingDataUpdate1 = new ChangeSongRatingDto(new SessionData(UUID.randomUUID(), true,
                    "community"),
                    "randomSongName", "randomSingerName", 3);
            ser.changeSongRating(gson.toJson(songRatingDataUpdate1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            ChangeSongRatingDto songRatingDataUpdate1 = new ChangeSongRatingDto(new SessionData(UUID.randomUUID(), true,
                    " "),
                    "randomSongName", "randomSingerName", 3);
            ser.changeSongRating(gson.toJson(songRatingDataUpdate1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }


        try {
            ChangeSongRatingDto songRatingDataUpdate1 = new ChangeSongRatingDto(new SessionData(UUID.randomUUID(), true,
                    ""),
                    "randomSongName", "randomSingerName", 3);
            ser.changeSongRating(gson.toJson(songRatingDataUpdate1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }


        try {
            ChangeSongRatingDto songRatingDataUpdate1 = new ChangeSongRatingDto(new SessionData(UUID.randomUUID(), true,
                    "randomLogin1"),
                    null, "randomSingerName", 3);
            ser.changeSongRating(gson.toJson(songRatingDataUpdate1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SONG_NAME, ex.getErrorCode());
        }

        try {
            ChangeSongRatingDto songRatingDataUpdate1 = new ChangeSongRatingDto(new SessionData(UUID.randomUUID(), true,
                    "randomLogin1"),
                    "", "randomSingerName", 3);
            ser.changeSongRating(gson.toJson(songRatingDataUpdate1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SONG_NAME, ex.getErrorCode());
        }

        try {
            ChangeSongRatingDto songRatingDataUpdate1 = new ChangeSongRatingDto(new SessionData(UUID.randomUUID(), true,
                    "randomLogin1"),
                    " ", "randomSingerName", 3);
            ser.changeSongRating(gson.toJson(songRatingDataUpdate1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SONG_NAME, ex.getErrorCode());
        }


        try {
            ChangeSongRatingDto songRatingDataUpdate1 = new ChangeSongRatingDto(new SessionData(UUID.randomUUID(), true,
                    "randomLogin1"),
                    "randomSongName", null, 3);
            ser.changeSongRating(gson.toJson(songRatingDataUpdate1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            ChangeSongRatingDto songRatingDataUpdate1 = new ChangeSongRatingDto(new SessionData(UUID.randomUUID(), true,
                    "randomLogin1"),
                    "randomSongName", "", 3);
            ser.changeSongRating(gson.toJson(songRatingDataUpdate1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            ChangeSongRatingDto songRatingDataUpdate1 = new ChangeSongRatingDto(new SessionData(UUID.randomUUID(), true,
                    "randomLogin1"),
                    "randomSongName", " ", 3);
            ser.changeSongRating(gson.toJson(songRatingDataUpdate1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            ChangeSongRatingDto songRatingDataUpdate1 = new ChangeSongRatingDto(new SessionData(UUID.randomUUID(), true,
                    "randomLogin1"),
                    "randomSongName", "random, random", 3);
            ser.changeSongRating(gson.toJson(songRatingDataUpdate1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            ChangeSongRatingDto songRatingDataUpdate1 = new ChangeSongRatingDto(new SessionData(UUID.randomUUID(), true,
                    "randomLogin1"),
                    "randomSongName", "random &random", 3);
            ser.changeSongRating(gson.toJson(songRatingDataUpdate1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            ChangeSongRatingDto songRatingDataUpdate1 = new ChangeSongRatingDto(new SessionData(UUID.randomUUID(), true,
                    "randomLogin1"),
                    "randomSongName", "random and random", 3);
            ser.changeSongRating(gson.toJson(songRatingDataUpdate1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            ChangeSongRatingDto songRatingDataUpdate1 = new ChangeSongRatingDto(new SessionData(UUID.randomUUID(), true,
                    "randomLogin1"),
                    "randomSongName", "randomxhbfdchnfjnfcjmncmmmmmmmmmmcfhbcfcncvmcmfcnfcdbkjvldvnkdxdrandom", 3);
            ser.changeSongRating(gson.toJson(songRatingDataUpdate1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            ChangeSongRatingDto songRatingDataUpdate1 = new ChangeSongRatingDto(new SessionData(UUID.randomUUID(), true,
                    "randomLogin1"),
                    "randomSongName1", "randomSingerName13214", 3);
            ser.changeSongRating(gson.toJson(songRatingDataUpdate1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.SONG_DOES_NOT_EXISTS, ex.getErrorCode());
        }

        try {
            ChangeSongRatingDto songRatingDataUpdate1 = new ChangeSongRatingDto(new SessionData(UUID.randomUUID(), true,
                    "randomLogin25"),
                    "randomSongName", "randomSingerName", 3);
            ser.changeSongRating(gson.toJson(songRatingDataUpdate1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.USER_HAS_NOT_RATED, ex.getErrorCode());
        }

        Database.getInstance().cleanBase();
    }


    @Test
    @Order(2)
    public void deleteSongRating() throws ServerException {


        Server ser = new Server();
        Gson gson = new Gson();

        AddSongDto songData = new AddSongDto(new SessionData(UUID.randomUUID(), true, "randomLogin"),
                "randomSongName", "randomComposerName", "randomLyricAutorName",
                "randomSingerName", 180);

        ser.addSong(gson.toJson(songData));
        assertEquals(1, Database.getInstance().getSongsBase().size());
        AddSongRatingDto songRatingData = new AddSongRatingDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                "randomSongName", "randomSingerName", 4);
        ser.addSongRating(gson.toJson(songRatingData));
        assertEquals(2, Database.getInstance().getSong("randomSongName", "randomSingerName").getRatings().size());
        assertTrue(Database.getInstance().getSong("randomSongName", "randomSingerName")
                .getSongRating("randomLogin1").equals(new SongRating("randomLogin1", 4)));


        try {
            DeleteSongRatingDto deleteSongRating1 = new DeleteSongRatingDto(new SessionData(null, true,
                    "randomLogin1"), "randomSongName", "randomSingerName");
            ser.deleteSongRating(gson.toJson(deleteSongRating1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            DeleteSongRatingDto deleteSongRating1 = new DeleteSongRatingDto(new SessionData(UUID.randomUUID(), false,
                    "randomLogin1"), "randomSongName", "randomSingerName");
            ser.deleteSongRating(gson.toJson(deleteSongRating1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            DeleteSongRatingDto deleteSongRating1 = new DeleteSongRatingDto(new SessionData(UUID.randomUUID(), true,
                    null), "randomSongName", "randomSingerName");
            ser.deleteSongRating(gson.toJson(deleteSongRating1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            DeleteSongRatingDto deleteSongRating1 = new DeleteSongRatingDto(new SessionData(UUID.randomUUID(), true,
                    "community"), "randomSongName", "randomSingerName");
            ser.deleteSongRating(gson.toJson(deleteSongRating1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }


        try {
            DeleteSongRatingDto deleteSongRating1 = new DeleteSongRatingDto(new SessionData(UUID.randomUUID(), true,
                    ""), "randomSongName", "randomSingerName");
            ser.deleteSongRating(gson.toJson(deleteSongRating1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }


        try {
            DeleteSongRatingDto deleteSongRating1 = new DeleteSongRatingDto(new SessionData(UUID.randomUUID(), true,
                    "randomLogin1"), null, "randomSingerName");
            ser.deleteSongRating(gson.toJson(deleteSongRating1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SONG_NAME, ex.getErrorCode());
        }

        try {
            DeleteSongRatingDto deleteSongRating1 = new DeleteSongRatingDto(new SessionData(UUID.randomUUID(), true,
                    "randomLogin1"), "", "randomSingerName");
            ser.deleteSongRating(gson.toJson(deleteSongRating1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SONG_NAME, ex.getErrorCode());
        }

        try {
            DeleteSongRatingDto deleteSongRating1 = new DeleteSongRatingDto(new SessionData(UUID.randomUUID(), true,
                    "randomLogin1"), " ", "randomSingerName");
            ser.deleteSongRating(gson.toJson(deleteSongRating1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SONG_NAME, ex.getErrorCode());
        }

        try {
            DeleteSongRatingDto deleteSongRating1 = new DeleteSongRatingDto(new SessionData(UUID.randomUUID(), true,
                    "randomLogin1"), "randomSongName", null);
            ser.deleteSongRating(gson.toJson(deleteSongRating1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            DeleteSongRatingDto deleteSongRating1 = new DeleteSongRatingDto(new SessionData(UUID.randomUUID(), true,
                    "randomLogin1"), "randomSongName", "");
            ser.deleteSongRating(gson.toJson(deleteSongRating1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            DeleteSongRatingDto deleteSongRating1 = new DeleteSongRatingDto(new SessionData(UUID.randomUUID(), true,
                    "randomLogin1"), "randomSongName", " ");
            ser.deleteSongRating(gson.toJson(deleteSongRating1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            DeleteSongRatingDto deleteSongRating1 = new DeleteSongRatingDto(new SessionData(UUID.randomUUID(), true,
                    "randomLogin1"), "randomSongName", "random , random , random");
            ser.deleteSongRating(gson.toJson(deleteSongRating1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            DeleteSongRatingDto deleteSongRating1 = new DeleteSongRatingDto(new SessionData(UUID.randomUUID(), true,
                    "randomLogin1"), "randomSongName", "random & random random");
            ser.deleteSongRating(gson.toJson(deleteSongRating1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            DeleteSongRatingDto deleteSongRating1 = new DeleteSongRatingDto(new SessionData(UUID.randomUUID(), true,
                    "randomLogin1"), "randomSongName", "random  and  random random");
            ser.deleteSongRating(gson.toJson(deleteSongRating1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            DeleteSongRatingDto deleteSongRating1 = new DeleteSongRatingDto(new SessionData(UUID.randomUUID(), true,
                    "randomLogin1"), "randomSongName", "random  и  random random");
            ser.deleteSongRating(gson.toJson(deleteSongRating1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            DeleteSongRatingDto deleteSongRating1 = new DeleteSongRatingDto(new SessionData(UUID.randomUUID(), true,
                    "randomLogin1"), "randomSongName", "randomshbdxhndfjncnfcjnfgjnfcblknpinf;kaz';na;zn;nbalks;kmskrandom random");
            ser.deleteSongRating(gson.toJson(deleteSongRating1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }


        try {
            DeleteSongRatingDto deleteSongRating1 = new DeleteSongRatingDto(new SessionData(UUID.randomUUID(), true,
                    "randomLogin1"), "randomSongName123125", "randomSingerName");
            ser.deleteSongRating(gson.toJson(deleteSongRating1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.SONG_DOES_NOT_EXISTS, ex.getErrorCode());
        }


        try {
            DeleteSongRatingDto deleteSongRating1 = new DeleteSongRatingDto(new SessionData(UUID.randomUUID(), true,
                    "randomLogin1"), "randomSongName", "randomSingerNamezgbxnccc");
            ser.deleteSongRating(gson.toJson(deleteSongRating1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.SONG_DOES_NOT_EXISTS, ex.getErrorCode());
        }


        try {
            DeleteSongRatingDto deleteSongRating1 = new DeleteSongRatingDto(new SessionData(UUID.randomUUID(), true,
                    "randomLogin12"), "randomSongName", "randomSingerName");
            ser.deleteSongRating(gson.toJson(deleteSongRating1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.USER_HAS_NOT_RATED, ex.getErrorCode());
        }

        DeleteSongRatingDto deleteSongRating = new DeleteSongRatingDto(new SessionData(UUID.randomUUID(), true,
                "randomLogin1"), "randomSongName", "randomSingerName");
        ser.deleteSongRating(gson.toJson(deleteSongRating));
        assertEquals(1, Database.getInstance().getSong("randomSongName", "randomSingerName").getRatings().size());
        assertNull(Database.getInstance().getSong("randomSongName", "randomSingerName")
                .getSongRating("randomLogin1"));
        Database.getInstance().cleanBase();
    }
}
