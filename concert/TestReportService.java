package net.thumbtack.school.concert;

import com.google.gson.Gson;
import net.thumbtack.school.concert.database.Database;

import net.thumbtack.school.concert.dto.SelectorDto;
import net.thumbtack.school.concert.errors.ServerErrorCode;
import net.thumbtack.school.concert.errors.ServerException;
import net.thumbtack.school.concert.model.*;
import net.thumbtack.school.concert.server.Server;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import net.jcip.annotations.NotThreadSafe;


import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.UUID;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@NotThreadSafe
public class TestReportService {

    public void fillTheBase() {

        Song song1 = new Song("Interstellar", "Brock", "Brock", "LeBrock",
                180, "rock-forever", new LinkedHashSet<Comment>(), new LinkedHashMap<String, SongRating>());
        Song song2 = new Song("Wall", "Brock", "Brock", "LeBrock",
                180, "rock-forever", new LinkedHashSet<Comment>(), new LinkedHashMap<String, SongRating>());

        Song song3 = new Song("Coma White", "M.M.", "M.M.", "Marilyn Manson",
                163, "voodo-dancer", new LinkedHashSet<Comment>(), new LinkedHashMap<String, SongRating>());

        Song song4 = new Song("Can't go to hell", "S.S.", "M.M.", "Sin Shake Sin",
                195, "voodo-dancer", new LinkedHashSet<Comment>(), new LinkedHashMap<String, SongRating>());

        Song song5 = new Song("Beautiful people", "M.M.", "yyttyyt", "Marilyn Manson",
                163, "voodo-dancer", new LinkedHashSet<Comment>(), new LinkedHashMap<String, SongRating>());

        Database.getInstance().addSong(song1);
        Database.getInstance().addSong(song2);
        Database.getInstance().addSong(song3);
        Database.getInstance().addSong(song4);
        Database.getInstance().addSong(song5);
    }


    @Test
    @Order(1)
    public void testShowAllAddedSongs() throws ServerException {
        Server ser = new Server();
        Gson gson = new Gson();
        fillTheBase();

        assertEquals(5, Database.getInstance().getSongsBase().size());

        SessionData sessionData = new SessionData(UUID.randomUUID(), true, "ordinary_man");

        String data = ser.showAllAddedSongs(gson.toJson(sessionData));
        LinkedHashSet<Song> result = gson.fromJson(data, LinkedHashSet.class);

        assertEquals(5, result.size());


        try {
            SessionData sessionData1 = new SessionData(null, true, "ordinary_man");
            String newData = ser.showAllAddedSongs(gson.toJson(sessionData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }


        try {
            SessionData sessionData1 = new SessionData(UUID.randomUUID(), false, "ordinary_man");
            String newData = ser.showAllAddedSongs(gson.toJson(sessionData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }


        try {
            SessionData sessionData1 = new SessionData(UUID.randomUUID(), true, null);
            String newData = ser.showAllAddedSongs(gson.toJson(sessionData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            SessionData sessionData1 = new SessionData(UUID.randomUUID(), true, "community");
            String newData = ser.showAllAddedSongs(gson.toJson(sessionData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            SessionData sessionData1 = new SessionData(UUID.randomUUID(), true, " ");
            String newData = ser.showAllAddedSongs(gson.toJson(sessionData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            SessionData sessionData1 = new SessionData(UUID.randomUUID(), true, "");
            String newData = ser.showAllAddedSongs(gson.toJson(sessionData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            SessionData sessionData1 = new SessionData(UUID.randomUUID(), true, "             ");
            String newData = ser.showAllAddedSongs(gson.toJson(sessionData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        Database.getInstance().cleanBase();

        try {
            SessionData sessionData1 = new SessionData(UUID.randomUUID(), true, "ordinary_man");
            String newData = ser.showAllAddedSongs(gson.toJson(sessionData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.SONGS_BASE_IS_EMPTY, ex.getErrorCode());
        }
    }


    @Test
    @Order(2)
    public void testShowSongsByComposer() throws ServerException {
        Server ser = new Server();
        Gson gson = new Gson();
        fillTheBase();

        assertEquals(5, Database.getInstance().getSongsBase().size());

        SelectorDto requestData1 = new SelectorDto(new SessionData(UUID.randomUUID(), true, "ordinary_man"),
                "Brock");

        SelectorDto requestData2 = new SelectorDto(new SessionData(UUID.randomUUID(), true, "ordinary_man"),
                "M.M.");

        SelectorDto requestData3 = new SelectorDto(new SessionData(UUID.randomUUID(), true, "ordinary_man"),
                "S.S.");

        String data1 = ser.showSongsSelectedByComposer(gson.toJson(requestData1));
        LinkedHashSet<Song> result1 = gson.fromJson(data1, LinkedHashSet.class);
        assertEquals(2, result1.size());

        String data2 = ser.showSongsSelectedByComposer(gson.toJson(requestData2));
        LinkedHashSet<Song> result2 = gson.fromJson(data2, LinkedHashSet.class);
        assertEquals(2, result2.size());

        String data3 = ser.showSongsSelectedByComposer(gson.toJson(requestData3));
        LinkedHashSet<Song> result3 = gson.fromJson(data3, LinkedHashSet.class);
        assertEquals(1, result3.size());


        try {
            SelectorDto requestData4 = new SelectorDto(new SessionData(null, true, "ordinary_man"),
                    "Brock");
            String data4 = ser.showSongsSelectedByComposer(gson.toJson(requestData4));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            SelectorDto requestData4 = new SelectorDto(new SessionData(UUID.randomUUID(), false, "ordinary_man"),
                    "Brock");
            String data4 = ser.showSongsSelectedByComposer(gson.toJson(requestData4));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            SelectorDto requestData4 = new SelectorDto(new SessionData(UUID.randomUUID(), true, null),
                    "Brock");
            String data4 = ser.showSongsSelectedByComposer(gson.toJson(requestData4));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            SelectorDto requestData4 = new SelectorDto(new SessionData(UUID.randomUUID(), true, "community"),
                    "Brock");
            String data4 = ser.showSongsSelectedByComposer(gson.toJson(requestData4));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            SelectorDto requestData4 = new SelectorDto(new SessionData(UUID.randomUUID(), true, " "),
                    "Brock");
            String data4 = ser.showSongsSelectedByComposer(gson.toJson(requestData4));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            SelectorDto requestData4 = new SelectorDto(new SessionData(UUID.randomUUID(), true, ""),
                    "Brock");
            String data4 = ser.showSongsSelectedByComposer(gson.toJson(requestData4));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            SelectorDto requestData4 = new SelectorDto(new SessionData(UUID.randomUUID(), true, "             "),
                    "Brock");
            String data4 = ser.showSongsSelectedByComposer(gson.toJson(requestData4));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            SelectorDto requestData4 = new SelectorDto(new SessionData(UUID.randomUUID(), true, "ordinary_man"),
                    "glhgglijfggkvmjvgi;oh;");
            String data4 = ser.showSongsSelectedByComposer(gson.toJson(requestData4));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.NO_SONGS_FOUND, ex.getErrorCode());
        }


        Database.getInstance().cleanBase();

        try {
            SelectorDto requestData4 = new SelectorDto(new SessionData(UUID.randomUUID(), true, "ordinary_man"),
                    "Brock");
            String data4 = ser.showSongsSelectedByComposer(gson.toJson(requestData4));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.SONGS_BASE_IS_EMPTY, ex.getErrorCode());
        }

    }


    @Test
    @Order(3)
    public void testShowSongsByLyricAutor() throws ServerException {
        Server ser = new Server();
        Gson gson = new Gson();
        fillTheBase();

        assertEquals(5, Database.getInstance().getSongsBase().size());

        SelectorDto requestData1 = new SelectorDto(new SessionData(UUID.randomUUID(), true, "ordinary_man"),
                "Brock");

        SelectorDto requestData2 = new SelectorDto(new SessionData(UUID.randomUUID(), true, "ordinary_man"),
                "M.M.");

        SelectorDto requestData3 = new SelectorDto(new SessionData(UUID.randomUUID(), true, "ordinary_man"),
                "yyttyyt");

        String data1 = ser.showSongsSelectedByLyricAutor(gson.toJson(requestData1));
        LinkedHashSet<Song> result1 = gson.fromJson(data1, LinkedHashSet.class);
        assertEquals(2, result1.size());

        String data2 = ser.showSongsSelectedByLyricAutor(gson.toJson(requestData2));
        LinkedHashSet<Song> result2 = gson.fromJson(data2, LinkedHashSet.class);
        assertEquals(2, result2.size());

        String data3 = ser.showSongsSelectedByLyricAutor(gson.toJson(requestData3));
        LinkedHashSet<Song> result3 = gson.fromJson(data3, LinkedHashSet.class);
        assertEquals(1, result3.size());


        try {
            SelectorDto requestData4 = new SelectorDto(new SessionData(null, true, "ordinary_man"),
                    "Brock");
            String data4 = ser.showSongsSelectedByLyricAutor(gson.toJson(requestData4));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            SelectorDto requestData4 = new SelectorDto(new SessionData(UUID.randomUUID(), false, "ordinary_man"),
                    "Brock");
            String data4 = ser.showSongsSelectedByLyricAutor(gson.toJson(requestData4));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            SelectorDto requestData4 = new SelectorDto(new SessionData(UUID.randomUUID(), true, null),
                    "Brock");
            String data4 = ser.showSongsSelectedByLyricAutor(gson.toJson(requestData4));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            SelectorDto requestData4 = new SelectorDto(new SessionData(UUID.randomUUID(), true, "community"),
                    "Brock");
            String data4 = ser.showSongsSelectedByLyricAutor(gson.toJson(requestData4));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            SelectorDto requestData4 = new SelectorDto(new SessionData(UUID.randomUUID(), true, " "),
                    "Brock");
            String data4 = ser.showSongsSelectedByLyricAutor(gson.toJson(requestData4));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            SelectorDto requestData4 = new SelectorDto(new SessionData(UUID.randomUUID(), true, ""),
                    "Brock");
            String data4 = ser.showSongsSelectedByLyricAutor(gson.toJson(requestData4));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            SelectorDto requestData4 = new SelectorDto(new SessionData(UUID.randomUUID(), true, "             "),
                    "Brock");
            String data4 = ser.showSongsSelectedByLyricAutor(gson.toJson(requestData4));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            SelectorDto requestData4 = new SelectorDto(new SessionData(UUID.randomUUID(), true, "ordinary_man"),
                    "glhgglijfggkvmjvgi;oh;");
            String data4 = ser.showSongsSelectedByLyricAutor(gson.toJson(requestData4));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.NO_SONGS_FOUND, ex.getErrorCode());
        }


        Database.getInstance().cleanBase();

        try {
            SelectorDto requestData4 = new SelectorDto(new SessionData(UUID.randomUUID(), true, "ordinary_man"),
                    "Brock");
            String data4 = ser.showSongsSelectedByLyricAutor(gson.toJson(requestData4));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.SONGS_BASE_IS_EMPTY, ex.getErrorCode());
        }

    }

    @Test
    @Order(4)
    public void testShowSongsBySinger() throws ServerException {
        Server ser = new Server();
        Gson gson = new Gson();
        fillTheBase();

        assertEquals(5, Database.getInstance().getSongsBase().size());

        SelectorDto requestData1 = new SelectorDto(new SessionData(UUID.randomUUID(), true, "ordinary_man"),
                "LeBrock");

        SelectorDto requestData2 = new SelectorDto(new SessionData(UUID.randomUUID(), true, "ordinary_man"),
                "Marilyn Manson");

        SelectorDto requestData3 = new SelectorDto(new SessionData(UUID.randomUUID(), true, "ordinary_man"),
                "Sin Shake Sin");

        String data1 = ser.showSongsSelectedBySinger(gson.toJson(requestData1));
        LinkedHashSet<Song> result1 = gson.fromJson(data1, LinkedHashSet.class);
        assertEquals(2, result1.size());

        String data2 = ser.showSongsSelectedBySinger(gson.toJson(requestData2));
        LinkedHashSet<Song> result2 = gson.fromJson(data2, LinkedHashSet.class);
        assertEquals(2, result2.size());

        String data3 = ser.showSongsSelectedBySinger(gson.toJson(requestData3));
        LinkedHashSet<Song> result3 = gson.fromJson(data3, LinkedHashSet.class);
        assertEquals(1, result3.size());


        try {
            SelectorDto requestData4 = new SelectorDto(new SessionData(null, true, "ordinary_man"),
                    "LeBrock");
            String data4 = ser.showSongsSelectedBySinger(gson.toJson(requestData4));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            SelectorDto requestData4 = new SelectorDto(new SessionData(UUID.randomUUID(), false, "ordinary_man"),
                    "LeBrock");
            String data4 = ser.showSongsSelectedBySinger(gson.toJson(requestData4));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            SelectorDto requestData4 = new SelectorDto(new SessionData(UUID.randomUUID(), true, null),
                    "LeBrock");
            String data4 = ser.showSongsSelectedBySinger(gson.toJson(requestData4));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            SelectorDto requestData4 = new SelectorDto(new SessionData(UUID.randomUUID(), true, "community"),
                    "LeBrock");
            String data4 = ser.showSongsSelectedBySinger(gson.toJson(requestData4));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            SelectorDto requestData4 = new SelectorDto(new SessionData(UUID.randomUUID(), true, " "),
                    "LeBrock");
            String data4 = ser.showSongsSelectedBySinger(gson.toJson(requestData4));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            SelectorDto requestData4 = new SelectorDto(new SessionData(UUID.randomUUID(), true, ""),
                    "LeBrock");
            String data4 = ser.showSongsSelectedBySinger(gson.toJson(requestData4));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            SelectorDto requestData4 = new SelectorDto(new SessionData(UUID.randomUUID(), true, "             "),
                    "LeBrock");
            String data4 = ser.showSongsSelectedBySinger(gson.toJson(requestData4));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            SelectorDto requestData4 = new SelectorDto(new SessionData(UUID.randomUUID(), true, "ordinary_man"),
                    "Brock");
            String data4 = ser.showSongsSelectedBySinger(gson.toJson(requestData4));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.NO_SONGS_FOUND, ex.getErrorCode());
        }


        Database.getInstance().cleanBase();

        try {
            SelectorDto requestData4 = new SelectorDto(new SessionData(UUID.randomUUID(), true, "ordinary_man"),
                    "LeBrock");
            String data4 = ser.showSongsSelectedBySinger(gson.toJson(requestData4));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.SONGS_BASE_IS_EMPTY, ex.getErrorCode());
        }

    }

    @Test
    @Order(5)
    public void testShowCurrentProgramm() throws ServerException {

        Server ser = new Server();
        Gson gson = new Gson();

        Song song1 = new Song("song1", "composer1", "lyric1", "singer1",
                600, "user1", new LinkedHashSet<Comment>(), new LinkedHashMap<String, SongRating>());
        song1.getRatings().put("otherUser1", new SongRating("otherUser1", 5));
        song1.getRatings().put("otherUser2", new SongRating("otherUser2", 5));
        song1.getRatings().put("otherUser3", new SongRating("otherUser3", 5));

        Database.getInstance().addSong(song1);

        Song song2 = new Song("song2", "composer2", "lyric2", "singer2",
                460, "user2", new LinkedHashSet<Comment>(), new LinkedHashMap<String, SongRating>());
        song2.getRatings().put("otherUser1", new SongRating("otherUser1", 5));
        song2.getRatings().put("otherUser2", new SongRating("otherUser2", 5));
        song2.getRatings().put("otherUser3", new SongRating("otherUser3", 5));

        Database.getInstance().addSong(song2);


        Song song3 = new Song("song3", "composer3", "lyric3", "singer3",
                700, "user3", new LinkedHashSet<Comment>(), new LinkedHashMap<String, SongRating>());
        song3.getRatings().put("otherUser1", new SongRating("otherUser1", 4));
        song3.getRatings().put("otherUser2", new SongRating("otherUser2", 5));
        song3.getRatings().put("otherUser3", new SongRating("otherUser3", 5));

        Database.getInstance().addSong(song3);

        Song song4 = new Song("song4", "composer4", "lyric4", "singer4",
                1000, "user4", new LinkedHashSet<Comment>(), new LinkedHashMap<String, SongRating>());
        song4.getRatings().put("otherUser1", new SongRating("otherUser1", 4));
        song4.getRatings().put("otherUser2", new SongRating("otherUser2", 4));
        song4.getRatings().put("otherUser3", new SongRating("otherUser3", 5));

        Database.getInstance().addSong(song4);


        Song song5 = new Song("song5", "composer5", "lyric5", "singer5",
                500, "user5", new LinkedHashSet<Comment>(), new LinkedHashMap<String, SongRating>());
        song5.getRatings().put("otherUser1", new SongRating("otherUser1", 4));
        song5.getRatings().put("otherUser2", new SongRating("otherUser2", 4));
        song5.getRatings().put("otherUser3", new SongRating("otherUser3", 4));


        Database.getInstance().addSong(song5);


        Song song6 = new Song("song6", "composer6", "lyric6", "singer6",
                300, "user6", new LinkedHashSet<Comment>(), new LinkedHashMap<String, SongRating>());

        song6.getRatings().put("otherUser1", new SongRating("otherUser1", 3));
        song6.getRatings().put("otherUser2", new SongRating("otherUser2", 4));
        song6.getRatings().put("otherUser3", new SongRating("otherUser3", 4));
        Database.getInstance().addSong(song6);


        Song song7 = new Song("song7", "composer7", "lyric7", "singer7",
                250, "user7", new LinkedHashSet<Comment>(), new LinkedHashMap<String, SongRating>());
        song7.getRatings().put("otherUser1", new SongRating("otherUser1", 3));
        song7.getRatings().put("otherUser2", new SongRating("otherUser2", 3));
        song7.getRatings().put("otherUser3", new SongRating("otherUser3", 3));
        Database.getInstance().addSong(song7);
        assertEquals(7, Database.getInstance().getSongsBase().size());

        SessionData sessionData = new SessionData(UUID.randomUUID(), true, "ordinary_man");
        String data1 = ser.showCurrentProgramm(gson.toJson(sessionData));
        LinkedList<ProgrammRecord> result1 = gson.fromJson(data1, LinkedList.class);

        assertEquals(6, result1.size());

        try {
            SessionData sessionData1 = new SessionData(null, true, "ordinary_man");
            String newData = ser.showCurrentProgramm(gson.toJson(sessionData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }


        try {
            SessionData sessionData1 = new SessionData(UUID.randomUUID(), false, "ordinary_man");
            String newData = ser.showCurrentProgramm(gson.toJson(sessionData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }


        try {
            SessionData sessionData1 = new SessionData(UUID.randomUUID(), true, null);
            String newData = ser.showCurrentProgramm(gson.toJson(sessionData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            SessionData sessionData1 = new SessionData(UUID.randomUUID(), true, "community");
            String newData = ser.showCurrentProgramm(gson.toJson(sessionData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            SessionData sessionData1 = new SessionData(UUID.randomUUID(), true, " ");
            String newData = ser.showCurrentProgramm(gson.toJson(sessionData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            SessionData sessionData1 = new SessionData(UUID.randomUUID(), true, "");
            String newData = ser.showCurrentProgramm(gson.toJson(sessionData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            SessionData sessionData1 = new SessionData(UUID.randomUUID(), true, "             ");
            String newData = ser.showCurrentProgramm(gson.toJson(sessionData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        Database.getInstance().cleanBase();
        try {
            SessionData sessionData1 = new SessionData(UUID.randomUUID(), true, "ordinary_man");
            String newData = ser.showCurrentProgramm(gson.toJson(sessionData1));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.SONGS_BASE_IS_EMPTY, ex.getErrorCode());
        }


    }


}
