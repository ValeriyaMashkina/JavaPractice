package net.thumbtack.school.concert;

import com.google.gson.Gson;
import net.thumbtack.school.concert.daoImpl.CommentDaoImpl;
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

public class TestCommentService {

    @Test
    @Order(1)
    public void testAddComment() throws ServerException {
        Server ser = new Server();
        Gson gson = new Gson();

        AddSongDto songData = new AddSongDto(new SessionData(UUID.randomUUID(), true, "randomLogin"),
                "randomSongName", "randomComposerName", "randomLyricAutorName",
                "randomSingerName", 180);

        ser.addSong(gson.toJson(songData));
        assertEquals(1, Database.getInstance().getSongsBase().size());

        AddCommentDto commentData1 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                "randomText", "randomSongName", "randomSingerName");
        ser.addComment(gson.toJson(commentData1));
        assertEquals(1, Database.getInstance().getSong("randomSongName", "randomSingerName").
                getSongsComments().size());
        assertTrue(Database.getInstance().getSong("randomSongName", "randomSingerName")
                .getComment("randomLogin1", "randomText").
                        equals(new Comment("randomLogin1", "randomText", new LinkedHashSet<String>())));

        try {
            AddCommentDto commentData2 = new AddCommentDto(new SessionData(null, true, "randomLogin1"),
                    "randomText", "randomSongName", "randomSingerName");
            ser.addComment(gson.toJson(commentData2));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            AddCommentDto commentData2 = new AddCommentDto(new SessionData(UUID.randomUUID(), false, "randomLogin1"),
                    "randomText", "randomSongName", "randomSingerName");
            ser.addComment(gson.toJson(commentData2));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            AddCommentDto commentData2 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, null),
                    "randomText", "randomSongName", "randomSingerName");
            ser.addComment(gson.toJson(commentData2));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            AddCommentDto commentData2 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "community"),
                    "randomText", "randomSongName", "randomSingerName");
            ser.addComment(gson.toJson(commentData2));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            AddCommentDto commentData2 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, " "),
                    "randomText", "randomSongName", "randomSingerName");
            ser.addComment(gson.toJson(commentData2));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            AddCommentDto commentData2 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, ""),
                    "randomText", "randomSongName", "randomSingerName");
            ser.addComment(gson.toJson(commentData2));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            AddCommentDto commentData2 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                    "randomText", null, "randomSingerName");
            ser.addComment(gson.toJson(commentData2));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SONG_NAME, ex.getErrorCode());
        }

        try {
            AddCommentDto commentData2 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                    "randomText", "", "randomSingerName");
            ser.addComment(gson.toJson(commentData2));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SONG_NAME, ex.getErrorCode());
        }

        try {
            AddCommentDto commentData2 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                    " ", "", "randomSingerName");
            ser.addComment(gson.toJson(commentData2));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SONG_NAME, ex.getErrorCode());
        }

        try {
            AddCommentDto commentData2 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                    "randomText", "randomSongName", null);
            ser.addComment(gson.toJson(commentData2));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            AddCommentDto commentData2 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                    "randomText", "randomSongName", "");
            ser.addComment(gson.toJson(commentData2));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            AddCommentDto commentData2 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                    "randomText", "randomSongName", " ");
            ser.addComment(gson.toJson(commentData2));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            AddCommentDto commentData2 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                    "randomText", "randomSongName", "random singer 1, random singer 2");
            ser.addComment(gson.toJson(commentData2));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            AddCommentDto commentData2 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                    "randomText", "randomSongName", "random singer 1 & random singer 2");
            ser.addComment(gson.toJson(commentData2));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            AddCommentDto commentData2 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                    "randomText", "randomSongName", "random singer 1 and random singer 2");
            ser.addComment(gson.toJson(commentData2));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            AddCommentDto commentData2 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                    "randomText", "randomSongName", "random singer 1 и random singer 2");
            ser.addComment(gson.toJson(commentData2));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            AddCommentDto commentData2 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                    "randomText", "randomSongName", "alalnsncasncslncszn idn in dsn b dsd pidsdophpophoooooohbbddsddddddjjjd");
            ser.addComment(gson.toJson(commentData2));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            AddCommentDto commentData2 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                    null, "randomSongName", "randomSingerName");
            ser.addComment(gson.toJson(commentData2));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_COMMENT_TEXT, ex.getErrorCode());
        }


        try {
            AddCommentDto commentData2 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                    "", "randomSongName", "randomSingerName");
            ser.addComment(gson.toJson(commentData2));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_COMMENT_TEXT, ex.getErrorCode());
        }

        try {
            AddCommentDto commentData2 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                    " ", "randomSongName", "randomSingerName");
            ser.addComment(gson.toJson(commentData2));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_COMMENT_TEXT, ex.getErrorCode());
        }
        try {
            AddCommentDto commentData2 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                    "   ", "randomSongName", "randomSingerName");
            ser.addComment(gson.toJson(commentData2));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_COMMENT_TEXT, ex.getErrorCode());
        }

        try {
            AddCommentDto commentData2 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                    "randomText", "randomSongName1", "randomSingerName2");
            ser.addComment(gson.toJson(commentData2));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.SONG_DOES_NOT_EXISTS, ex.getErrorCode());
        }

        try {
            AddCommentDto commentData2 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin"),
                    "randomText", "randomSongName", "randomSingerName");
            ser.addComment(gson.toJson(commentData2));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_COMMENT_ATTEMPT, ex.getErrorCode());
        }

        try {
            AddCommentDto commentData2 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                    "newRandomText", "randomSongName", "randomSingerName");
            ser.addComment(gson.toJson(commentData2));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.USER_ALREADY_LEFT_COMMENT, ex.getErrorCode());
        }
        Database.getInstance().cleanBase();
    }

    @Test
    @Order(2)
    public void testJoinComment() throws ServerException {

        Server ser = new Server();
        Gson gson = new Gson();

        AddSongDto songData = new AddSongDto(new SessionData(UUID.randomUUID(), true, "randomLogin"),
                "randomSongName", "randomComposerName", "randomLyricAutorName",
                "randomSingerName", 180);

        ser.addSong(gson.toJson(songData));
        assertEquals(1, Database.getInstance().getSongsBase().size());

        AddCommentDto commentData1 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                "randomText", "randomSongName", "randomSingerName");
        ser.addComment(gson.toJson(commentData1));
        assertEquals(1, Database.getInstance().getSong("randomSongName", "randomSingerName").
                getSongsComments().size());
        assertTrue(Database.getInstance().getSong("randomSongName", "randomSingerName")
                .getComment("randomLogin1", "randomText").
                        equals(new Comment("randomLogin1", "randomText", new LinkedHashSet<String>())));


        JoinCommentDto joinData1 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin2"),
                "randomSongName", "randomSingerName", "randomLogin1", "randomText");
        JoinCommentDto joinData2 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                "randomSongName", "randomSingerName", "randomLogin1", "randomText");
        ser.joinComment(gson.toJson(joinData1));
        ser.joinComment(gson.toJson(joinData2));
        assertEquals(2, Database.getInstance().getSong("randomSongName", "randomSingerName").
                getComment("randomLogin1", "randomText").getJoinedUsers()
                .size());


        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(null, true, "randomLogin3"),
                    "randomSongName", "randomSingerName", "randomLogin1", "randomText");
            ser.joinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), false, "randomLogin3"),
                    "randomSongName", "randomSingerName", "randomLogin1", "randomText");
            ser.joinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true,
                    null),
                    "randomSongName", "randomSingerName", "randomLogin1", "randomText");
            ser.joinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true,
                    "community"),
                    "randomSongName", "randomSingerName", "randomLogin1", "randomText");
            ser.joinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true,
                    " "),
                    "randomSongName", "randomSingerName", "randomLogin1", "randomText");
            ser.joinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true,
                    ""),
                    "randomSongName", "randomSingerName", "randomLogin1", "randomText");
            ser.joinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true,
                    "   "),
                    "randomSongName", "randomSingerName", "randomLogin1", "randomText");
            ser.joinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    null, "randomSingerName", "randomLogin1", "randomText");
            ser.joinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SONG_NAME, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "", "randomSingerName", "randomLogin1", "randomText");
            ser.joinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SONG_NAME, ex.getErrorCode());
        }
        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    " ", "randomSingerName", "randomLogin1", "randomText");
            ser.joinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SONG_NAME, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "   ", "randomSingerName", "randomLogin1", "randomText");
            ser.joinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SONG_NAME, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "randomSongName", null, "randomLogin1", "randomText");
            ser.joinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "randomSongName", "", "randomLogin1", "randomText");
            ser.joinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "randomSongName", " ", "randomLogin1", "randomText");
            ser.joinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "randomSongName", "   ", "randomLogin1", "randomText");
            ser.joinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "randomSongName", "singer2,singerxgbmdp", "randomLogin1", "randomText");
            ser.joinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "randomSongName", "singer2&singerxgbmdp", "randomLogin1", "randomText");
            ser.joinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "randomSongName", "singer2 and singerxgbmdp", "randomLogin1", "randomText");
            ser.joinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "randomSongName", "singer2 и singerxgbmdp", "randomLogin1", "randomText");
            ser.joinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "randomSongName", "lnljdgjvd'bm'fcmb'fcmn'lf'ldxgbdchcfncfvjmcgmgcmgcmdcjndhsxghshllkoujojohguyftl''l", "randomLogin1", "randomText");
            ser.joinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }


        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "randomSongName", "randomSingerName", null, "randomText");
            ser.joinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_COMMENT_AUTHOR, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "randomSongName", "randomSingerName", "", "randomText");
            ser.joinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_COMMENT_AUTHOR, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "randomSongName", "randomSingerName", " ", "randomText");
            ser.joinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_COMMENT_AUTHOR, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "randomSongName", "randomSingerName", "     ", "randomText");
            ser.joinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_COMMENT_AUTHOR, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "randomSongName", "randomSingerName", "randomLogin3", "randomText");
            ser.joinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_JOINED_USER, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "randomSongName", "randomSingerName", "randomLogin1", null);
            ser.joinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_COMMENT_TEXT, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "randomSongName", "randomSingerName", "randomLogin1", "");
            ser.joinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_COMMENT_TEXT, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "randomSongName", "randomSingerName", "randomLogin1", " ");
            ser.joinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_COMMENT_TEXT, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "randomSongName", "randomSingerName", "randomLogin1", "      ");
            ser.joinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_COMMENT_TEXT, ex.getErrorCode());
        }


        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "randomSongName1", "randomSingerName1", "randomLogin1", "randomText");
            ser.joinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.SONG_DOES_NOT_EXISTS, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "randomSongName", "randomSingerName", "randomLogin", "randomText");
            ser.joinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_COMMENT_ATTEMPT, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin"),
                    "randomSongName", "randomSingerName", "randomLogin1", "randomText");
            ser.joinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_JOINED_USER, ex.getErrorCode());
        }


        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "randomSongName", "randomSingerName", "randomLogin8", "randomText");
            ser.joinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.COMMENT_DOES_NOT_EXIST, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin2"),
                    "randomSongName", "randomSingerName", "randomLogin1", "randomText");
            ser.joinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.USER_HAS_ALREADY_JOINED, ex.getErrorCode());
        }
        Database.getInstance().cleanBase();
    }

    @Test
    @Order(3)
    public void testDeleteJoinComment() throws ServerException {
        Server ser = new Server();
        Gson gson = new Gson();

        AddSongDto songData = new AddSongDto(new SessionData(UUID.randomUUID(), true, "randomLogin"),
                "randomSongName", "randomComposerName", "randomLyricAutorName",
                "randomSingerName", 180);

        ser.addSong(gson.toJson(songData));
        assertEquals(1, Database.getInstance().getSongsBase().size());

        AddCommentDto commentData1 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                "randomText", "randomSongName", "randomSingerName");
        ser.addComment(gson.toJson(commentData1));
        assertEquals(1, Database.getInstance().getSong("randomSongName", "randomSingerName").
                getSongsComments().size());
        assertTrue(Database.getInstance().getSong("randomSongName", "randomSingerName")
                .getComment("randomLogin1", "randomText").
                        equals(new Comment("randomLogin1", "randomText", new LinkedHashSet<String>())));


        JoinCommentDto joinData1 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin2"),
                "randomSongName", "randomSingerName", "randomLogin1", "randomText");
        JoinCommentDto joinData2 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                "randomSongName", "randomSingerName", "randomLogin1", "randomText");
        ser.joinComment(gson.toJson(joinData1));
        ser.joinComment(gson.toJson(joinData2));
        assertEquals(2, Database.getInstance().getSong("randomSongName", "randomSingerName").
                getComment("randomLogin1", "randomText").getJoinedUsers()
                .size());

        ser.deleteJoinComment(gson.toJson(joinData2));
        assertEquals(1, Database.getInstance().getSong("randomSongName", "randomSingerName").
                getComment("randomLogin1", "randomText").getJoinedUsers()
                .size());

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(null, true, "randomLogin3"),
                    "randomSongName", "randomSingerName", "randomLogin1", "randomText");
            ser.deleteJoinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), false, "randomLogin3"),
                    "randomSongName", "randomSingerName", "randomLogin1", "randomText");
            ser.deleteJoinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true,
                    null),
                    "randomSongName", "randomSingerName", "randomLogin1", "randomText");
            ser.deleteJoinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true,
                    "community"),
                    "randomSongName", "randomSingerName", "randomLogin1", "randomText");
            ser.deleteJoinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true,
                    " "),
                    "randomSongName", "randomSingerName", "randomLogin1", "randomText");
            ser.deleteJoinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true,
                    ""),
                    "randomSongName", "randomSingerName", "randomLogin1", "randomText");
            ser.deleteJoinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true,
                    "   "),
                    "randomSongName", "randomSingerName", "randomLogin1", "randomText");
            ser.deleteJoinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    null, "randomSingerName", "randomLogin1", "randomText");
            ser.deleteJoinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SONG_NAME, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "", "randomSingerName", "randomLogin1", "randomText");
            ser.deleteJoinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SONG_NAME, ex.getErrorCode());
        }
        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    " ", "randomSingerName", "randomLogin1", "randomText");
            ser.deleteJoinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SONG_NAME, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "   ", "randomSingerName", "randomLogin1", "randomText");
            ser.deleteJoinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SONG_NAME, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "randomSongName", null, "randomLogin1", "randomText");
            ser.deleteJoinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "randomSongName", "", "randomLogin1", "randomText");
            ser.deleteJoinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "randomSongName", " ", "randomLogin1", "randomText");
            ser.deleteJoinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "randomSongName", "   ", "randomLogin1", "randomText");
            ser.deleteJoinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "randomSongName", "singer2,singerxgbmdp", "randomLogin1", "randomText");
            ser.deleteJoinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "randomSongName", "singer2&singerxgbmdp", "randomLogin1", "randomText");
            ser.deleteJoinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "randomSongName", "singer2 and singerxgbmdp", "randomLogin1", "randomText");
            ser.deleteJoinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "randomSongName", "singer2 и singerxgbmdp", "randomLogin1", "randomText");
            ser.deleteJoinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "randomSongName", "lnljdgjvd'bm'fcmb'fcmn'lf'ldxgbdchcfncfvjmcgmgcmgcmdcjndhsxghshllkoujojohguyftl''l", "randomLogin1", "randomText");
            ser.deleteJoinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "randomSongName", "randomSingerName", null, "randomText");
            ser.deleteJoinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_COMMENT_AUTHOR, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "randomSongName", "randomSingerName", "", "randomText");
            ser.deleteJoinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_COMMENT_AUTHOR, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "randomSongName", "randomSingerName", " ", "randomText");
            ser.deleteJoinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_COMMENT_AUTHOR, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "randomSongName", "randomSingerName", "     ", "randomText");
            ser.deleteJoinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_COMMENT_AUTHOR, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "randomSongName", "randomSingerName", "randomLogin3", "randomText");
            ser.deleteJoinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_JOINED_USER, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "randomSongName", "randomSingerName", "randomLogin1", null);
            ser.deleteJoinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_COMMENT_TEXT, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "randomSongName", "randomSingerName", "randomLogin1", "");
            ser.deleteJoinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_COMMENT_TEXT, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "randomSongName", "randomSingerName", "randomLogin1", " ");
            ser.deleteJoinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_COMMENT_TEXT, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "randomSongName", "randomSingerName", "randomLogin1", "      ");
            ser.deleteJoinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_COMMENT_TEXT, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "randomSongName1", "randomSingerName1", "randomLogin1", "randomText");
            ser.deleteJoinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.SONG_DOES_NOT_EXISTS, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "randomSongName", "randomSingerName", "randomLogin", "randomText");
            ser.deleteJoinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_COMMENT_ATTEMPT, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin"),
                    "randomSongName", "randomSingerName", "randomLogin1", "randomText");
            ser.deleteJoinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_JOINED_USER, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "randomSongName", "randomSingerName", "randomLogin8", "randomText");
            ser.deleteJoinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.COMMENT_DOES_NOT_EXIST, ex.getErrorCode());
        }

        try {
            JoinCommentDto joinData3 = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin3"),
                    "randomSongName", "randomSingerName", "randomLogin1", "randomText");
            ser.deleteJoinComment(gson.toJson(joinData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.USER_HAS_NOT_JOINED_YET, ex.getErrorCode());
        }
        Database.getInstance().cleanBase();
    }


    @Test
    @Order(4)
    public void testChangeComment() throws ServerException {

        Server ser = new Server();
        Gson gson = new Gson();

        AddSongDto songData = new AddSongDto(new SessionData(UUID.randomUUID(), true, "randomLogin"),
                "randomSongName", "randomComposerName", "randomLyricAutorName",
                "randomSingerName", 180);

        ser.addSong(gson.toJson(songData));
        assertEquals(1, Database.getInstance().getSongsBase().size());

        AddCommentDto commentData1 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                "randomText1", "randomSongName", "randomSingerName");
        AddCommentDto commentData2 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin2"),
                "randomText2", "randomSongName", "randomSingerName");
        ser.addComment(gson.toJson(commentData1));
        ser.addComment(gson.toJson(commentData2));
        assertEquals(2, Database.getInstance().getSong("randomSongName", "randomSingerName").
                getSongsComments().size());


        JoinCommentDto joinData = new JoinCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin5"),
                "randomSongName", "randomSingerName", "randomLogin1", "randomText1");

        ser.joinComment(gson.toJson(joinData));
        assertEquals(1, Database.getInstance().getSong("randomSongName", "randomSingerName").
                getComment("randomLogin1", "randomText1").getJoinedUsers()
                .size());


        AddCommentDto changeCommentData1 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                "randomText1changed", "randomSongName", "randomSingerName");
        AddCommentDto changeCommentData2 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin2"),
                "randomText2changed", "randomSongName", "randomSingerName");
        ser.changeComment(gson.toJson(changeCommentData1));
        ser.changeComment(gson.toJson(changeCommentData2));


        Comment comment1 = new Comment("randomLogin1", "randomText1changed", new LinkedHashSet<String>());
        Comment comment2 = new Comment("community", "randomText1", new LinkedHashSet<String>());
        comment2.addJoinedUser("randomLogin5");
        Comment comment3 = new Comment("randomLogin2", "randomText2changed", new LinkedHashSet<String>());

        assertEquals(3, Database.getInstance().getSong("randomSongName", "randomSingerName").getSongsComments().size());


        assertTrue(Database.getInstance().getSong("randomSongName", "randomSingerName").checkComment("randomLogin1", "randomText1changed"));
        assertEquals(0, Database.getInstance().getSong("randomSongName", "randomSingerName").getComment("randomLogin1", "randomText1changed").getJoinedUsers().size());


        assertTrue(Database.getInstance().getSong("randomSongName", "randomSingerName").checkComment("community", "randomText1"));
        assertEquals(1, Database.getInstance().getSong("randomSongName", "randomSingerName").getComment("community", "randomText1").getJoinedUsers().size());
        assertTrue(Database.getInstance().getSong("randomSongName", "randomSingerName").getComment("community", "randomText1").getJoinedUsers().contains("randomLogin5"));

        assertTrue(Database.getInstance().getSong("randomSongName", "randomSingerName").checkComment("randomLogin2", "randomText2changed"));
        assertEquals(0, Database.getInstance().getSong("randomSongName", "randomSingerName").getComment("randomLogin2", "randomText2changed").getJoinedUsers().size());


        try {
            AddCommentDto changeCommentData3 = new AddCommentDto(new SessionData(null, true, "randomLogin1"),
                    "randomText1changed", "randomSongName", "randomSingerName");
            ser.changeComment(gson.toJson(changeCommentData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            AddCommentDto changeCommentData3 = new AddCommentDto(new SessionData(UUID.randomUUID(), false, "randomLogin1"),
                    "randomText1changed", "randomSongName", "randomSingerName");
            ser.changeComment(gson.toJson(changeCommentData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            AddCommentDto changeCommentData3 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, null),
                    "randomText1changed", "randomSongName", "randomSingerName");
            ser.changeComment(gson.toJson(changeCommentData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            AddCommentDto changeCommentData3 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "community"),
                    "randomText1changed", "randomSongName", "randomSingerName");
            ser.changeComment(gson.toJson(changeCommentData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            AddCommentDto changeCommentData3 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, ""),
                    "randomText1changed", "randomSongName", "randomSingerName");
            ser.changeComment(gson.toJson(changeCommentData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            AddCommentDto changeCommentData3 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, " "),
                    "randomText1changed", "randomSongName", "randomSingerName");
            ser.changeComment(gson.toJson(changeCommentData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            AddCommentDto changeCommentData3 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "      "),
                    "randomText1changed", "randomSongName", "randomSingerName");
            ser.changeComment(gson.toJson(changeCommentData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SESSION, ex.getErrorCode());
        }

        try {
            AddCommentDto changeCommentData3 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                    "randomText1changed", null, "randomSingerName");
            ser.changeComment(gson.toJson(changeCommentData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SONG_NAME, ex.getErrorCode());
        }

        try {
            AddCommentDto changeCommentData3 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                    "randomText1changed", "", "randomSingerName");
            ser.changeComment(gson.toJson(changeCommentData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SONG_NAME, ex.getErrorCode());
        }

        try {
            AddCommentDto changeCommentData3 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                    "randomText1changed", " ", "randomSingerName");
            ser.changeComment(gson.toJson(changeCommentData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SONG_NAME, ex.getErrorCode());
        }

        try {
            AddCommentDto changeCommentData3 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                    "randomText1changed", "       ", "randomSingerName");
            ser.changeComment(gson.toJson(changeCommentData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SONG_NAME, ex.getErrorCode());
        }

        try {
            AddCommentDto changeCommentData3 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                    "randomText1changed", "randomSongName", null);
            ser.changeComment(gson.toJson(changeCommentData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            AddCommentDto changeCommentData3 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                    "randomText1changed", "randomSongName", "");
            ser.changeComment(gson.toJson(changeCommentData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            AddCommentDto changeCommentData3 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                    "randomText1changed", "randomSongName", " ");
            ser.changeComment(gson.toJson(changeCommentData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            AddCommentDto changeCommentData3 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                    "randomText1changed", "randomSongName", "      ");
            ser.changeComment(gson.toJson(changeCommentData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            AddCommentDto changeCommentData3 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                    "randomText1changed", "randomSongName", "dhnfjnfjgf,drhdrjdjtfk");
            ser.changeComment(gson.toJson(changeCommentData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            AddCommentDto changeCommentData3 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                    "randomText1changed", "randomSongName", "dhnfjnfjgf and drhdrjdjtfk");
            ser.changeComment(gson.toJson(changeCommentData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            AddCommentDto changeCommentData3 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                    "randomText1changed", "randomSongName", "dhnfjnfjgf и drhdrjdjtfk");
            ser.changeComment(gson.toJson(changeCommentData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            AddCommentDto changeCommentData3 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                    "randomText1changed", "randomSongName", "dhnfjnfjgf & drhdrjdjtfk");
            ser.changeComment(gson.toJson(changeCommentData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            AddCommentDto changeCommentData3 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                    "randomText1changed", "randomSongName", "dhndbcfnfgvmghhjfghjgfhfkghkjhljkk.kj.kj.j.hjgggffhngmh,hrjdjtfk");
            ser.changeComment(gson.toJson(changeCommentData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_SINGER_NAME, ex.getErrorCode());
        }

        try {
            AddCommentDto changeCommentData3 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                    null, "randomSongName", "randomSingerName");
            ser.changeComment(gson.toJson(changeCommentData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_COMMENT_TEXT, ex.getErrorCode());
        }

        try {
            AddCommentDto changeCommentData3 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                    "", "randomSongName", "randomSingerName");
            ser.changeComment(gson.toJson(changeCommentData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_COMMENT_TEXT, ex.getErrorCode());
        }

        try {
            AddCommentDto changeCommentData3 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                    " ", "randomSongName", "randomSingerName");
            ser.changeComment(gson.toJson(changeCommentData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_COMMENT_TEXT, ex.getErrorCode());
        }

        try {
            AddCommentDto changeCommentData3 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                    "         ", "randomSongName", "randomSingerName");
            ser.changeComment(gson.toJson(changeCommentData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.INVALID_COMMENT_TEXT, ex.getErrorCode());
        }


        try {
            AddCommentDto changeCommentData3 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin1"),
                    "randomText1changed", "randomSongName1465757", "randomSingerName");
            ser.changeComment(gson.toJson(changeCommentData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.SONG_DOES_NOT_EXISTS, ex.getErrorCode());
        }

        try {
            AddCommentDto changeCommentData3 = new AddCommentDto(new SessionData(UUID.randomUUID(), true, "randomLogin5"),
                    "randomText1changed", "randomSongName", "randomSingerName");
            ser.changeComment(gson.toJson(changeCommentData3));
            fail();
        } catch (ServerException ex) {
            assertEquals(ServerErrorCode.COMMENT_DOES_NOT_EXIST, ex.getErrorCode());
        }
        Database.getInstance().cleanBase();
    }
}
