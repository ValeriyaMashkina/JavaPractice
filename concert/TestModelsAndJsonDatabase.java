package net.thumbtack.school.concert;

import net.thumbtack.school.concert.database.Database;
import net.thumbtack.school.concert.model.*;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import net.jcip.annotations.NotThreadSafe;


import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@NotThreadSafe
public class TestModelsAndJsonDatabase {

    public void fillTheBase() {
        User user1 = new User("Меркьюри", "Фредди", "rock-forever", "dont_stop_meNOW");
        User user2 = new User("Озборн", "Оззи", "voodo-dancer", "ordinary_man");
        User user3 = new User("Мэрлин", "Мэнсон", "beautiful-people", "fgsgsde");
        User user4 = new User("Петров", "Олег", "just-listener", "ndndcnggdt");
        User user5 = new User("Меркьюри", "Фредди", "rock-forever", "dont_stop_meNOW");

        Database.getInstance().addUser(user1);
        Database.getInstance().addUser(user2);
        Database.getInstance().addUser(user3);
        Database.getInstance().addUser(user4);

        Song song1 = new Song("Interstellar", "Brock", "Brock", "LeBrock",
                180, "rock-forever", new LinkedHashSet<Comment>(), new LinkedHashMap<String, SongRating>());
        Song song2 = new Song("Coma White", "M.M.", "M.M.", "Marilyn Manson",
                163, "voodo-dancer", new LinkedHashSet<Comment>(), new LinkedHashMap<String, SongRating>());
        Song song3 = new Song("Cant go to hell", "S.S.", "S.S.", "Sin Shake Sin",
                195, "voodo-dancer", new LinkedHashSet<Comment>(), new LinkedHashMap<String, SongRating>());
        Database.getInstance().addSong(song1);
        Database.getInstance().addSong(song2);
        Database.getInstance().addSong(song3);

        SongRating songRating1 = new SongRating("voodo-dancer", 5); // song1,
        SongRating songRating2 = new SongRating("rock-forever", 4); // song1
        SongRating songRating3 = new SongRating("rock-forever", 4); // song2

        Database.getInstance().getSong(song1).addRating(songRating1);
        Database.getInstance().getSong(song1).addRating(songRating2);
        Database.getInstance().getSong(song2).addRating(songRating3);

        Comment comment1 = new Comment("beautiful-people", "nice", null); // song 1
        Comment comment2 = new Comment("just-listener", "lol", null); // song 1
        Comment comment3 = new Comment("just-listener", "lol", null); // song 2

        Database.getInstance().getSong(song1).addComment(comment1);
        Database.getInstance().getSong(song1).addComment(comment2);
        Database.getInstance().getSong(song2).addComment(comment3);
    }

    @Test
    @Order(1)
    public void testAddUser() {

        User user1 = new User("Меркьюри", "Фредди", "rock-forever", "dont_stop_meNOW");
        User user2 = new User("Озборн", "Оззи", "voodo-dancer", "ordinary_man");
        User user3 = new User("Меркьюри", "Фредди", "rock-forever", "dont_stop_meNOW");

        Database.getInstance().addUser(user1);
        Database.getInstance().addUser(user2);
        Database.getInstance().addUser(user3);
        assertEquals(2, Database.getInstance().getUsersBase().size());
        Database.getInstance().cleanBase();
    }

    @Test
    @Order(2)
    public void testAddSong() {

        Song song1 = new Song("Interstellar", "Brock", "Brock", "LeBrock",
                180, "rock-forever", new LinkedHashSet<Comment>(), new LinkedHashMap<String, SongRating>());
        Song song2 = new Song("Coma White", "M.M.", "M.M.", "Marilyn Manson",
                163, "voodo-dancer", new LinkedHashSet<Comment>(), new LinkedHashMap<String, SongRating>());
        Song song3 = new Song("Cant go to hell", "S.S.", "S.S.", "Sin Shake Sin",
                195, "voodo-dancer", new LinkedHashSet<Comment>(), new LinkedHashMap<String, SongRating>());

        Database.getInstance().addSong(song1);
        Database.getInstance().addSong(song2);
        Database.getInstance().addSong(song3);
        assertEquals(3, Database.getInstance().getSongsBase().size());
        Database.getInstance().cleanBase();
    }


    @Test
    @Order(3)
    public void testSongRating() {

        Song song1 = new Song("Interstellar", "Brock", "Brock", "LeBrock",
                180, "rock-forever", new LinkedHashSet<Comment>(), new LinkedHashMap<String, SongRating>());
        Song song2 = new Song("Coma White", "M.M.", "M.M.", "Marilyn Manson",
                163, "voodo-dancer", new LinkedHashSet<Comment>(), new LinkedHashMap<String, SongRating>());
        Song song3 = new Song("Cant go to hell", "S.S.", "S.S.", "Sin Shake Sin",
                195, "voodo-dancer", new LinkedHashSet<Comment>(), new LinkedHashMap<String, SongRating>());

        Database.getInstance().addSong(song1);
        Database.getInstance().addSong(song2);
        Database.getInstance().addSong(song3);

        SongRating songRating1 = new SongRating("voodo-dancer", 5); // song1
        SongRating songRating2 = new SongRating("rock-forever", 4); // song1
        SongRating songRating3 = new SongRating("rock-forever", 4); // song2

        Database.getInstance().getSong(song1).addRating(songRating1);
        Database.getInstance().getSong(song1).addRating(songRating2);
        Database.getInstance().getSong(song2).addRating(songRating3);

        assertEquals(2, Database.getInstance().getSong(song1).getRatings().size());
        assertEquals(1, Database.getInstance().getSong(song2).getRatings().size());
        Database.getInstance().cleanBase();
    }


    @Test
    @Order(4)
    public void testComment() {

        User user1 = new User("Меркьюри", "Фредди", "rock-forever", "dont_stop_meNOW");
        User user2 = new User("Озборн", "Оззи", "voodo-dancer", "ordinary_man");
        User user3 = new User("Мэрлин", "Мэнсон", "beautiful-people", "fgsgsde");
        User user4 = new User("Петров", "Олег", "just-listener", "ndndcnggdt");

        Database.getInstance().addUser(user1);
        Database.getInstance().addUser(user2);
        Database.getInstance().addUser(user3);
        Database.getInstance().addUser(user4);

        Song song1 = new Song("Interstellar", "Brock", "Brock", "LeBrock",
                180, "rock-forever", new LinkedHashSet<Comment>(), new LinkedHashMap<String, SongRating>());
        Song song2 = new Song("Coma White", "M.M.", "M.M.", "Marilyn Manson",
                163, "voodo-dancer", new LinkedHashSet<Comment>(), new LinkedHashMap<String, SongRating>());
        Song song3 = new Song("Cant go to hell", "S.S.", "S.S.", "Sin Shake Sin",
                195, "voodo-dancer", new LinkedHashSet<Comment>(), new LinkedHashMap<String, SongRating>());

        Database.getInstance().addSong(song1);
        Database.getInstance().addSong(song2);
        Database.getInstance().addSong(song3);

        Comment comment1 = new Comment("beautiful-people", "nice", null); // song 1
        Comment comment2 = new Comment("just-listener", "lol", null); // song 1
        Comment comment3 = new Comment("just-listener", "lol", null); // song 2

        Database.getInstance().getSong(song1).addComment(comment1);
        Database.getInstance().getSong(song1).addComment(comment2);
        Database.getInstance().getSong(song2).addComment(comment3);

        assertEquals(2, Database.getInstance().getSong(song1).getSongsComments().size());
        assertEquals(1, Database.getInstance().getSong(song2).getSongsComments().size());

        Database.getInstance().cleanBase();
    }

    @Test
    @Order(5)
    public void DataBaseToJson() throws IOException {
        fillTheBase();
        Database.getInstance().serializeDatabaseToJsonFile("test.txt");
        Database.getInstance().cleanBase();
        assertEquals(0, Database.getInstance().getUsersBase().size());
        assertEquals(0, Database.getInstance().getSongsBase().size());
        Database.getInstance().deserializeDatabaseFromJsonFile("test.txt");
        assertEquals(4, Database.getInstance().getUsersBase().size());
        assertEquals(3, Database.getInstance().getSongsBase().size());

        LinkedHashMap<String, Song> sBase = new LinkedHashMap<>();
        LinkedHashMap<String, User> uBase = new LinkedHashMap<>();

        User user1 = new User("Меркьюри", "Фредди", "rock-forever", "dont_stop_meNOW");
        User user2 = new User("Озборн", "Оззи", "voodo-dancer", "ordinary_man");
        User user3 = new User("Мэрлин", "Мэнсон", "beautiful-people", "fgsgsde");
        User user4 = new User("Петров", "Олег", "just-listener", "ndndcnggdt");
        uBase.put(user1.getLogin(), user1);
        uBase.put(user2.getLogin(), user2);
        uBase.put(user3.getLogin(), user3);
        uBase.put(user4.getLogin(), user4);


        SongRating songRating1 = new SongRating("voodo-dancer", 5); // song1,
        SongRating songRating2 = new SongRating("rock-forever", 4); // song1
        SongRating songRating3 = new SongRating("rock-forever", 4); // song2

        Comment comment1 = new Comment("beautiful-people", "nice", null); // song 1
        Comment comment2 = new Comment("just-listener", "lol", null); // song 1
        Comment comment3 = new Comment("just-listener", "lol", null); // song 2

        Song song1 = new Song("Interstellar", "Brock", "Brock", "LeBrock",
                180, "rock-forever", new LinkedHashSet<Comment>(), new LinkedHashMap<String, SongRating>());
        song1.addComment(comment1);
        song1.addComment(comment2);
        song1.addRating(songRating1);
        song1.addRating(songRating2);
        Song song2 = new Song("Coma White", "M.M.", "M.M.", "Marilyn Manson",
                163, "voodo-dancer", new LinkedHashSet<Comment>(), new LinkedHashMap<String, SongRating>());
        song2.addComment(comment3);
        song2.addRating(songRating3);

        Song song3 = new Song("Cant go to hell", "S.S.", "S.S.", "Sin Shake Sin",
                195, "voodo-dancer", new LinkedHashSet<Comment>(), new LinkedHashMap<String, SongRating>());

        sBase.put(song1.getSongName() + song1.getSingerName(), song1);
        sBase.put(song2.getSongName() + song2.getSingerName(), song2);
        sBase.put(song3.getSongName() + song3.getSingerName(), song3);


        assertEquals(uBase, Database.getInstance().getUsersBase());
        assertEquals(sBase, Database.getInstance().getSongsBase());

        Database.getInstance().cleanBase();
    }


    @Test
    @Order(6)
    public void testUniqueDatabase() {

        Database base = Database.getInstance();
        fillTheBase();
        assertEquals(4, base.getInstance().getUsersBase().size());
        assertEquals(3, base.getInstance().getSongsBase().size());

        Database.getInstance().cleanBase();

        assertEquals(0, base.getInstance().getUsersBase().size());
        assertEquals(0, base.getInstance().getSongsBase().size());


        User user1 = new User("Меркьюри", "Фредди", "rock-forever", "dont_stop_meNOW");
        User user2 = new User("Озборн", "Оззи", "voodo-dancer", "ordinary_man");
        User user3 = new User("Мэрлин", "Мэнсон", "beautiful-people", "fgsgsde");
        User user4 = new User("Петров", "Олег", "just-listener", "ndndcnggdt");

        base.getInstance().addUser(user1);
        base.getInstance().addUser(user2);
        base.getInstance().addUser(user3);
        base.getInstance().addUser(user4);

        Song song1 = new Song("Interstellar", "Brock", "Brock", "LeBrock",
                180, "rock-forever", new LinkedHashSet<Comment>(), new LinkedHashMap<String, SongRating>());
        Song song2 = new Song("Coma White", "M.M.", "M.M.", "Marilyn Manson",
                163, "voodo-dancer", new LinkedHashSet<Comment>(), new LinkedHashMap<String, SongRating>());
        Song song3 = new Song("Cant go to hell", "S.S.", "S.S.", "Sin Shake Sin",
                195, "voodo-dancer", new LinkedHashSet<Comment>(), new LinkedHashMap<String, SongRating>());

        base.getInstance().addSong(song1);
        base.getInstance().addSong(song2);
        base.getInstance().addSong(song3);

        assertEquals(4, Database.getInstance().getUsersBase().size());
        assertEquals(3, Database.getInstance().getSongsBase().size());

        base.getInstance().cleanBase();

        assertEquals(0, Database.getInstance().getUsersBase().size());
        assertEquals(0, Database.getInstance().getSongsBase().size());
    }

}


