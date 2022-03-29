package net.thumbtack.school.concert.database;

import com.google.gson.Gson;


import net.thumbtack.school.concert.model.Song;

import net.thumbtack.school.concert.model.User;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


public class Database implements Serializable {
    private static final long serialVersionUID = 803745049486954915L;
    private static volatile Database INSTANCE = new Database();

    private Database() {
    }

    public static Database getInstance() {
        return INSTANCE;
    }

    private LinkedHashMap<String, Song> SongsBase = new LinkedHashMap<>();
    private LinkedHashMap<String, User> UsersBase = new LinkedHashMap<>();
    private HashMap<String, HashSet<Song>> songsByComposer = new HashMap<>();
    private HashMap<String, HashSet<Song>> songsByLyricAutor = new HashMap<>();
    private HashMap<String, HashSet<Song>> songsBySinger = new HashMap<>();
    private HashMap<String, HashSet<Song>> songsByCurrentUser = new HashMap<>();

    public LinkedHashMap<String, Song> getSongsBase() {
        return this.SongsBase;
    }

    public LinkedHashMap<String, User> getUsersBase() {
        return this.UsersBase;
    }

    public HashMap<String, HashSet<Song>> getSongsByComposer() {
        return songsByComposer;
    }

    public HashMap<String, HashSet<Song>> getSongsByLyricAutor() {
        return songsByLyricAutor;
    }

    public HashMap<String, HashSet<Song>> getSongsBySinger() {
        return songsBySinger;
    }

    public HashMap<String, HashSet<Song>> getSongsByCurrentUser() {
        return songsByCurrentUser;
    }


    public Boolean checkSong(String songName, String singerName) {
        return getSongsBase().containsKey(songName + singerName);
    }

    public Boolean checkSong(String userlogin, String songName, String singerName) {
        return getSongsBase().containsKey(songName + singerName) &&
                getSongsBase().get(songName + singerName).getUserWhoProposed().equals(userlogin);
    }


    public Song getSong(String songName, String singerName) {
        return getSongsBase().get(songName + singerName);
    }

    public Song getSong(String userlogin, String songName, String singerName) {
        Song song = getSongsBase().get(songName + singerName);
        if (song.getUserWhoProposed().equals(userlogin)) {
            return song;
        } else return null;
    }


    public Song getSong(Song song) {
        return getSongsBase().get(song.getSongName() + song.getSingerName());
    }

    public Boolean checkLogin(User user) {
        return getUsersBase().containsKey(user.getLogin());
    }

    public Boolean checkLogin(String userLogin) {
        return getUsersBase().containsKey(userLogin);
    }

    public User getUser(String userLogin) {
        return getUsersBase().get(userLogin);
    }

    public void serializeDatabaseToJsonFile(String fileName) throws IOException {
        Gson gson = new Gson();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(fileName)))) {
            gson.toJson(INSTANCE, bw);
        }
    }

    public void deserializeDatabaseFromJsonFile(String fileName) throws IOException {
        Gson gson = new Gson();
        try (BufferedReader br = new BufferedReader(new FileReader(new File(fileName)))) {
            INSTANCE = gson.fromJson(br, Database.class);
        }
    }


    public void addSong(Song song) {
        getSongsBase().put(song.getSongName() + song.getSingerName(), song);

        if (getSongsByComposer().get(song.getComposerName()) == null) {
            HashSet<Song> set = new HashSet<Song>();
            set.add(song);
            getSongsByComposer().put(song.getComposerName(), set);
        } else {
            getSongsByComposer().get(song.getComposerName()).add(song);
        }

        if (getSongsByLyricAutor().get(song.getLyricAutorName()) == null) {
            HashSet<Song> set = new HashSet<Song>();
            set.add(song);
            getSongsByLyricAutor().put(song.getLyricAutorName(), set);
        } else {
            getSongsByLyricAutor().get(song.getLyricAutorName()).add(song);
        }

        if (getSongsBySinger().get(song.getSingerName()) == null) {
            HashSet<Song> set = new HashSet<Song>();
            set.add(song);
            getSongsBySinger().put(song.getSingerName(), set);
        } else {
            getSongsBySinger().get(song.getSingerName()).add(song);
        }

        if (getSongsByCurrentUser().get(song.getUserWhoProposed()) == null) {
            HashSet<Song> set = new HashSet<Song>();
            set.add(song);
            getSongsByCurrentUser().put(song.getUserWhoProposed(), set);
        } else {
            getSongsByCurrentUser().get(song.getUserWhoProposed()).add(song);
        }
    }

    public void addUser(User user) {
        getUsersBase().put(user.getLogin(), user);
    }

    public void deleteUser(User user) {
        getUsersBase().remove(user.getLogin());
    }

    public void deleteUser(String login) {
        getUsersBase().remove(login);
    }

    public void deleteSong(String songName, String singerName) {

        Song songToDelete = getSongsBase().get(songName + singerName);
        getSongsByComposer().get(songToDelete.getComposerName()).remove(songToDelete);
        getSongsByLyricAutor().get(songToDelete.getLyricAutorName()).remove(songToDelete);
        getSongsBySinger().get(songToDelete.getSingerName()).remove(songToDelete);
        getSongsByCurrentUser().get(songToDelete.getUserWhoProposed()).remove(songToDelete);
        getSongsBase().remove(songName + singerName);
    }

    public void deleteSong(Song song) {
        getSongsByComposer().get(song.getComposerName()).remove(song);
        getSongsByLyricAutor().get(song.getLyricAutorName()).remove(song);
        getSongsBySinger().get(song.getSingerName()).remove(song);
        getSongsByCurrentUser().get(song.getUserWhoProposed()).remove(song);
        getSongsBase().remove(song.getSongName() + song.getSingerName());
    }


    public void deleteSongWithoutSongsByCurrentUser(Song song) {
        getSongsByComposer().get(song.getComposerName()).remove(song);
        getSongsByLyricAutor().get(song.getLyricAutorName()).remove(song);
        getSongsBySinger().get(song.getSingerName()).remove(song);
        getSongsBase().remove(song.getSongName() + song.getSingerName());
    }

    public void cleanBase() {
        getSongsBase().clear();
        getUsersBase().clear();
        getSongsByComposer().clear();
        getSongsByLyricAutor().clear();
        getSongsBySinger().clear();
        getSongsByCurrentUser().clear();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Database)) return false;
        Database database = (Database) o;
        return Objects.equals(SongsBase, database.SongsBase) &&
                Objects.equals(UsersBase, database.UsersBase) &&
                Objects.equals(songsByComposer, database.songsByComposer) &&
                Objects.equals(songsByLyricAutor, database.songsByLyricAutor) &&
                Objects.equals(songsBySinger, database.songsBySinger) &&
                Objects.equals(songsByCurrentUser, database.songsByCurrentUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(SongsBase, UsersBase, songsByComposer, songsByLyricAutor, songsBySinger, songsByCurrentUser);
    }
}
