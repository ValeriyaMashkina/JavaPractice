package net.thumbtack.school.concert.daoImpl;

import com.google.gson.Gson;
import net.thumbtack.school.concert.dao.UserDao;
import net.thumbtack.school.concert.database.Database;
import net.thumbtack.school.concert.dto.LoginDto;
import net.thumbtack.school.concert.errors.ServerErrorCode;
import net.thumbtack.school.concert.errors.ServerException;
import net.thumbtack.school.concert.model.*;

import java.util.*;
import java.util.stream.Collectors;

public class UserDaoImpl implements UserDao {

    @Override
    public String registrateNewUser(User newUser) throws ServerException {
        if (!Database.getInstance().checkLogin(newUser)) {
            Database.getInstance().getUsersBase().put(newUser.getLogin(), newUser);
            Gson gson = new Gson();
            return gson.toJson(new SessionData(UUID.randomUUID(), true, newUser.getLogin()));
        } else {
            throw new ServerException(ServerErrorCode.LOGIN_ALREADY_EXISTS);
        }
    }

    @Override
    public String loginUser(LoginDto userData) throws ServerException {
        String login = userData.getLogin();
        String password = userData.getPassword();

        if (Database.getInstance().checkLogin(login)) {
            if (Database.getInstance().getUser(login).getPassword().equals(password)) {
                Gson gson = new Gson();
                return gson.toJson(new SessionData(UUID.randomUUID(), true, login));
            } else {
                throw new ServerException(ServerErrorCode.INCORRECT_PASSWORD);
            }
        } else {
            throw new ServerException(ServerErrorCode.LOGIN_DOES_NOT_EXIST);
        }
    }

    @Override
    public void deleteUser(SessionData sessionData) {
        String login = sessionData.getCurrentUserLogin();
        if (Database.getInstance().getSongsBase() == null) {
            Database.getInstance().deleteUser(login);
        } else {

            HashSet<Song> songsByCurrentUser = Database.getInstance().getSongsByCurrentUser().get(sessionData.getCurrentUserLogin());

            if (songsByCurrentUser != null && !songsByCurrentUser.isEmpty()) {
                HashSet<Song> songsToChange = new HashSet<>();

                for (Song song : songsByCurrentUser) {
                    if (song.getSongsComments().isEmpty() && song.getRatings().isEmpty()) {
                        Database.getInstance().deleteSongWithoutSongsByCurrentUser(song);
                    } else {
                        Database.getInstance().getSongsBase().get(song.getSongName() + song.getSingerName()).setUserWhoProposed("community");
                        Database.getInstance().getSongsByComposer().get(song.getComposerName()).
                                forEach(s -> {
                                    if (s.equals(song)) {
                                        s.setUserWhoProposed("community");
                                    }
                                });
                        Database.getInstance().getSongsByLyricAutor().get(song.getLyricAutorName()).
                                forEach(s -> {
                                    if (s.equals(song)) {
                                        s.setUserWhoProposed("community");
                                    }
                                });
                        Database.getInstance().getSongsBySinger().get(song.getSingerName()).
                                forEach(s -> {
                                    if (s.equals(song)) {
                                        s.setUserWhoProposed("community");
                                    }
                                });

                        songsToChange.add(song);

                    }
                }

                Database.getInstance().getSongsByCurrentUser().remove(sessionData.getCurrentUserLogin());

                if (Database.getInstance().getSongsByCurrentUser().get("community") == null) {
                    Database.getInstance().getSongsByCurrentUser().put("community", songsToChange);
                } else {
                    Database.getInstance().getSongsByCurrentUser().get("community").addAll(songsToChange);
                }
            }


            Collection<Song> allSongs = Database.getInstance().getSongsBase().values();
            for (Song otherSong : allSongs) {

                otherSong.getRatings().remove(login);
                if (otherSong.checkComment(sessionData.getCurrentUserLogin())) {
                    otherSong.getComment(sessionData.getCurrentUserLogin()).setLoginCommentAutor("community");
                }
            }

            Database.getInstance().deleteUser(login);
        }

    }

}
