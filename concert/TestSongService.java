package net.thumbtack.school.concert;

import com.google.gson.Gson;
import net.thumbtack.school.concert.database.Database;
import net.thumbtack.school.concert.dto.AddSongDto;
import net.thumbtack.school.concert.dto.DeleteSongDto;
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
import java.util.*;

import net.jcip.annotations.NotThreadSafe;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

@NotThreadSafe
public class TestSongService {

    @Test
    @Order(1)
    public void testAddSong() throws ServerException {
        Server ser = new Server();
        Gson gson = new Gson();

        AddSongDto songData = new AddSongDto(new SessionData(UUID.randomUUID(), true, "randomLogin"),
                "randomSongName", "randomComposerName", "randomLyricAutorName",
                "randomSingerName", 180);

        String res = ser.addSong(gson.toJson(songData));

        assertEquals(1, Database.getInstance().getSongsBase().size());


        LinkedHashMap<String, SongRating> ratings = new LinkedHashMap<String, SongRating>();
        ratings.put("randomLogin", new SongRating("randomLogin", 5));

        assertTrue(Database.getInstance().getSongsBase().containsValue((new Song("randomSongName", "randomComposerName",
                "randomLyricAutorName", "randomSingerName",
                180, "randomLogin", new LinkedHashSet<Comment>(), ratings))));

        assertTrue(gson.fromJson(res, SessionData.class).getValid());
        assertEquals("randomLogin", gson.fromJson(res, SessionData.class).getCurrentUserLogin());
        assertNotEquals(null, gson.fromJson(res, SessionData.class).getToken());

        try {
            AddSongDto songData1 = new AddSongDto(new SessionData(null, true, "randomLogin"),
                    "randomSongName", "randomComposerName", "randomLyricAutorName",
                    "randomSingerName", 180);
            ser.addSong(gson.toJson(songData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            AddSongDto songData1 = new AddSongDto(new SessionData(UUID.randomUUID(), false, "randomLogin"),
                    "randomSongName", "randomComposerName", "randomLyricAutorName",
                    "randomSingerName", 180);
            ser.addSong(gson.toJson(songData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            AddSongDto songData1 = new AddSongDto(new SessionData(UUID.randomUUID(), true, null),
                    "randomSongName", "randomComposerName", "randomLyricAutorName",
                    "randomSingerName", 180);
            ser.addSong(gson.toJson(songData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            AddSongDto songData1 = new AddSongDto(new SessionData(UUID.randomUUID(), true, " "),
                    "randomSongName", "randomComposerName", "randomLyricAutorName",
                    "randomSingerName", 180);
            ser.addSong(gson.toJson(songData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            AddSongDto songData1 = new AddSongDto(new SessionData(UUID.randomUUID(), true, ""),
                    "randomSongName", "randomComposerName", "randomLyricAutorName",
                    "randomSingerName", 180);
            ser.addSong(gson.toJson(songData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            AddSongDto songData1 = new AddSongDto(new SessionData(UUID.randomUUID(), true, "community"),
                    "randomSongName", "randomComposerName", "randomLyricAutorName",
                    "randomSingerName", 180);
            ser.addSong(gson.toJson(songData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }


        try {
            AddSongDto songData1 = new AddSongDto(new SessionData(UUID.randomUUID(), true, "randomLogin"),
                    null, "randomComposerName", "randomLyricAutorName",
                    "randomSingerName", 180);
            ser.addSong(gson.toJson(songData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SONG_NAME, ex.getErrorCode());
        }

        try {
            AddSongDto songData1 = new AddSongDto(new SessionData(UUID.randomUUID(), true, "randomLogin"),
                    "", "randomComposerName", "randomLyricAutorName",
                    "randomSingerName", 180);
            ser.addSong(gson.toJson(songData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SONG_NAME, ex.getErrorCode());
        }


        try {
            AddSongDto songData1 = new AddSongDto(new SessionData(UUID.randomUUID(), true, "randomLogin"),
                    " ", "randomComposerName", "randomLyricAutorName",
                    "randomSingerName", 180);
            ser.addSong(gson.toJson(songData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SONG_NAME, ex.getErrorCode());
        }


        try {
            AddSongDto songData1 = new AddSongDto(new SessionData(UUID.randomUUID(), true, "randomLogin"),
                    "randomSongName", null, "randomLyricAutorName",
                    "randomSingerName", 180);
            ser.addSong(gson.toJson(songData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SONG_COMPOSER_NAME, ex.getErrorCode());
        }

        try {
            AddSongDto songData1 = new AddSongDto(new SessionData(UUID.randomUUID(), true, "randomLogin"),
                    "randomSongName", "", "randomLyricAutorName",
                    "randomSingerName", 180);
            ser.addSong(gson.toJson(songData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SONG_COMPOSER_NAME, ex.getErrorCode());
        }

        try {
            AddSongDto songData1 = new AddSongDto(new SessionData(UUID.randomUUID(), true, "randomLogin"),
                    "randomSongName", " ", "randomLyricAutorName",
                    "randomSingerName", 180);
            ser.addSong(gson.toJson(songData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SONG_COMPOSER_NAME, ex.getErrorCode());
        }


        try {
            AddSongDto songData1 = new AddSongDto(new SessionData(UUID.randomUUID(), true, "randomLogin"),
                    "randomSongName", "randomComposerName", null,
                    "randomSingerName", 180);
            ser.addSong(gson.toJson(songData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_LYRIC_AUTOR_NAME, ex.getErrorCode());
        }

        try {
            AddSongDto songData1 = new AddSongDto(new SessionData(UUID.randomUUID(), true, "randomLogin"),
                    "randomSongName", "randomComposerName", "",
                    "randomSingerName", 180);
            ser.addSong(gson.toJson(songData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_LYRIC_AUTOR_NAME, ex.getErrorCode());
        }

        try {
            AddSongDto songData1 = new AddSongDto(new SessionData(UUID.randomUUID(), true, "randomLogin"),
                    "randomSongName", "randomComposerName", " ",
                    "randomSingerName", 180);
            ser.addSong(gson.toJson(songData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_LYRIC_AUTOR_NAME, ex.getErrorCode());
        }


        try {
            AddSongDto songData1 = new AddSongDto(new SessionData(UUID.randomUUID(), true, "randomLogin"),
                    "randomSongName", "randomComposerName", "randomLyricAutorName",
                    "randomSingerName", 0);
            ser.addSong(gson.toJson(songData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SONG_DURATION, ex.getErrorCode());
        }

        try {
            AddSongDto songData1 = new AddSongDto(new SessionData(UUID.randomUUID(), true, "randomLogin"),
                    "randomSongName", "randomComposerName", "randomLyricAutorName",
                    "randomSingerName", -8);
            ser.addSong(gson.toJson(songData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SONG_DURATION, ex.getErrorCode());
        }


        try {
            AddSongDto songData1 = new AddSongDto(new SessionData(UUID.randomUUID(), true, "randomLogin"),
                    "randomSongName", "randomComposerName", "randomLyricAutorName",
                    null, 180);
            ser.addSong(gson.toJson(songData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            AddSongDto songData1 = new AddSongDto(new SessionData(UUID.randomUUID(), true, "randomLogin"),
                    "randomSongName", "randomComposerName", "randomLyricAutorName",
                    "", 180);
            ser.addSong(gson.toJson(songData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            AddSongDto songData1 = new AddSongDto(new SessionData(UUID.randomUUID(), true, "randomLogin"),
                    "randomSongName", "randomComposerName", "randomLyricAutorName",
                    "random singer 1, random singer 2", 180);
            ser.addSong(gson.toJson(songData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            AddSongDto songData1 = new AddSongDto(new SessionData(UUID.randomUUID(), true, "randomLogin"),
                    "randomSongName", "randomComposerName", "randomLyricAutorName",
                    "random singer 1 & random singer 2", 180);
            ser.addSong(gson.toJson(songData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            AddSongDto songData1 = new AddSongDto(new SessionData(UUID.randomUUID(), true, "randomLogin"),
                    "randomSongName", "randomComposerName", "randomLyricAutorName",
                    "random singer 1 and random singer 2", 180);
            ser.addSong(gson.toJson(songData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            AddSongDto songData1 = new AddSongDto(new SessionData(UUID.randomUUID(), true, "randomLogin"),
                    "randomSongName", "randomComposerName", "randomLyricAutorName",
                    "random singer 1 и random singer 2", 180);
            ser.addSong(gson.toJson(songData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            AddSongDto songData1 = new AddSongDto(new SessionData(UUID.randomUUID(), true, "randomLogin"),
                    "randomSongName", "randomComposerName", "randomLyricAutorName",
                    "randomsinger1randomsinger2randomsinger3randomsinger4randomsinger5", 180);
            ser.addSong(gson.toJson(songData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }


        try {
            AddSongDto songData1 = new AddSongDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                    "randomSongName", "randomComposerName1", "randomLyricAutorName1",
                    "randomSingerName", 200);
            ser.addSong(gson.toJson(songData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.SONG_ALREADY_EXISTS, ex.getErrorCode());
        }
        Database.getInstance().cleanBase();
    }

    @Test
    @Order(2)
    public void testDeleteSong() throws ServerException {

        Server ser = new Server();
        Gson gson = new Gson();
        AddSongDto songData1 = new AddSongDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                "randomSongName1", "randomComposerName1", "randomLyricAutorName1",
                "randomSingerName1", 180);
        AddSongDto songData2 = new AddSongDto(new SessionData(UUID.randomUUID(), true, "randomLogin2"),
                "randomSongName2", "randomComposerName2", "randomLyricAutorName2",
                "randomSingerName2", 180);
        AddSongDto songData3 = new AddSongDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                "randomSongName3", "randomComposerName3", "randomLyricAutorName3",
                "randomSingerName3", 180);

        ser.addSong(gson.toJson(songData1));
        ser.addSong(gson.toJson(songData2));
        ser.addSong(gson.toJson(songData3));

        assertEquals(3, Database.getInstance().getSongsBase().size());


        try {
            DeleteSongDto deleteData1 = new DeleteSongDto((new SessionData(UUID.randomUUID(), true, "randomLogin3")),
                    "randomSongName1", "randomSingerName7");

            ser.deleteSong(gson.toJson(deleteData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.SONG_DOES_NOT_EXISTS, ex.getErrorCode());
        }

        try {
            DeleteSongDto deleteData1 = new DeleteSongDto((new SessionData(null, true, "randomLogin1")),
                    "randomSongName1", "randomSingerName1");

            ser.deleteSong(gson.toJson(deleteData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            DeleteSongDto deleteData1 = new DeleteSongDto((new SessionData(UUID.randomUUID(), false, "randomLogin1")),
                    "randomSongName1", "randomSingerName1");

            ser.deleteSong(gson.toJson(deleteData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            DeleteSongDto deleteData1 = new DeleteSongDto((new SessionData(UUID.randomUUID(), true, null)),
                    "randomSongName1", "randomSingerName1");

            ser.deleteSong(gson.toJson(deleteData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }


        try {
            DeleteSongDto deleteData1 = new DeleteSongDto((new SessionData(UUID.randomUUID(), true, "community")),
                    "randomSongName1", "randomSingerName1");
            ser.deleteSong(gson.toJson(deleteData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }


        try {
            DeleteSongDto deleteData1 = new DeleteSongDto((new SessionData(UUID.randomUUID(), true, "randomLogin1")),
                    null, "randomSingerName1");
            ser.deleteSong(gson.toJson(deleteData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SONG_NAME, ex.getErrorCode());
        }

        try {
            DeleteSongDto deleteData1 = new DeleteSongDto((new SessionData(UUID.randomUUID(), true, "randomLogin1")),
                    "", "randomSingerName1");
            ser.deleteSong(gson.toJson(deleteData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SONG_NAME, ex.getErrorCode());
        }

        try {
            DeleteSongDto deleteData1 = new DeleteSongDto((new SessionData(UUID.randomUUID(), true, "randomLogin1")),
                    " ", "randomSingerName1");
            ser.deleteSong(gson.toJson(deleteData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SONG_NAME, ex.getErrorCode());
        }

        try {
            DeleteSongDto deleteData1 = new DeleteSongDto((new SessionData(UUID.randomUUID(), true, "randomLogin1")),
                    "randomSongName1", null);
            ser.deleteSong(gson.toJson(deleteData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            DeleteSongDto deleteData1 = new DeleteSongDto((new SessionData(UUID.randomUUID(), true, "randomLogin1")),
                    "randomSongName1", "");
            ser.deleteSong(gson.toJson(deleteData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            DeleteSongDto deleteData1 = new DeleteSongDto((new SessionData(UUID.randomUUID(), true, "randomLogin1")),
                    "randomSongName1", " ");
            ser.deleteSong(gson.toJson(deleteData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            DeleteSongDto deleteData1 = new DeleteSongDto((new SessionData(UUID.randomUUID(), true, "randomLogin1")),
                    "randomSongName1", "random singer 1,random singer 2");
            ser.deleteSong(gson.toJson(deleteData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            DeleteSongDto deleteData1 = new DeleteSongDto((new SessionData(UUID.randomUUID(), true, "randomLogin1")),
                    "randomSongName1", "random singer 1 & random singer 2");
            ser.deleteSong(gson.toJson(deleteData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            DeleteSongDto deleteData1 = new DeleteSongDto((new SessionData(UUID.randomUUID(), true, "randomLogin1")),
                    "randomSongName1", "random singer 1 and random singer 2");
            ser.deleteSong(gson.toJson(deleteData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            DeleteSongDto deleteData1 = new DeleteSongDto((new SessionData(UUID.randomUUID(), true, "randomLogin1")),
                    "randomSongName1", "random singer 1 и random singer 2");
            ser.deleteSong(gson.toJson(deleteData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }


        DeleteSongDto deleteData3 = new DeleteSongDto((new SessionData(UUID.randomUUID(), true, "randomLogin3")),
                "randomSongName3", "randomSingerName3");

        assertEquals(1, Database.getInstance().getSong
                ("randomLogin3", "randomSongName3", "randomSingerName3").getRatings().size());

        ser.deleteSong(gson.toJson(deleteData3));
        assertEquals(2, Database.getInstance().getSongsBase().size());
        assertFalse(Database.getInstance().checkSong("randomLogin3", "randomSongName3", "randomSingerName3"));
        try {
            DeleteSongDto deleteData1 = new DeleteSongDto((new SessionData(UUID.randomUUID(), true, "randomLogin3")),
                    "randomSongName3", "randomSingerName3");

            ser.deleteSong(gson.toJson(deleteData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.SONG_DOES_NOT_EXISTS, ex.getErrorCode());
        }

        LinkedHashMap<String, SongRating> ratings = new LinkedHashMap<String, SongRating>();
        ratings.put("randomLogin4", new SongRating("randomLogin4", 5));
        ratings.put("randomLogin1", new SongRating("randomLogin1", 5));
        Song newSong = new Song("randomSongName4", "randomComposerName4", "randomLyricAutorName4",
                "randomSingerName4", 300, "randomLogin4",
                new LinkedHashSet<Comment>(), ratings);
        Database.getInstance().addSong(newSong);

        assertEquals(3, Database.getInstance().getSongsBase().size());
        assertTrue(Database.getInstance().checkSong("randomLogin4", "randomSongName4", "randomSingerName4"));
        assertEquals(2, Database.getInstance().getSong("randomLogin4", "randomSongName4", "randomSingerName4").getRatings().size());
        DeleteSongDto deleteData4 = new DeleteSongDto((new SessionData(UUID.randomUUID(), true, "randomLogin4")),
                "randomSongName4", "randomSingerName4");
        ser.deleteSong(gson.toJson(deleteData4));

        assertEquals(3, Database.getInstance().getSongsBase().size());
        assertTrue(Database.getInstance().checkSong("community", "randomSongName4", "randomSingerName4"));
        assertEquals(1, Database.getInstance().getSong("community", "randomSongName4", "randomSingerName4").getRatings().size());

        Database.getInstance().cleanBase();
    }
}
