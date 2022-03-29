package net.thumbtack.school.concert.daoImpl;

import com.google.gson.Gson;
import net.thumbtack.school.concert.dao.SongDao;
import net.thumbtack.school.concert.database.Database;
import net.thumbtack.school.concert.errors.ServerErrorCode;
import net.thumbtack.school.concert.errors.ServerException;
import net.thumbtack.school.concert.model.SessionData;
import net.thumbtack.school.concert.model.Song;
import net.thumbtack.school.concert.model.SongRating;

import java.util.UUID;

public class SongDaoImpl implements SongDao {

    @Override
    public void addNewSong(Song newSong) throws ServerException {
        if (!Database.getInstance().checkSong(newSong.getSongName(), newSong.getSingerName())) {
            Database.getInstance().addSong(newSong);
        } else {
            throw new ServerException(ServerErrorCode.SONG_ALREADY_EXISTS);
        }
    }

    @Override
    public void deleteSong(String userLogin, String songName, String singerName) throws ServerException {
        if (!Database.getInstance().checkSong(songName, singerName)) {
            throw new ServerException(ServerErrorCode.SONG_DOES_NOT_EXISTS);
        } else {

            Song song = Database.getInstance().getSong(songName, singerName);

            if (!song.getUserWhoProposed().equals(userLogin)) {
                throw new ServerException(ServerErrorCode.SONG_DOES_NOT_EXISTS);
            } else if (song.getRatings().size() == 1) {
                Database.getInstance().deleteSong(song);
            } else {
                Database.getInstance().getSong(songName, singerName).getRatings().remove(userLogin);
                Database.getInstance().getSong(songName, singerName).setUserWhoProposed("community");
            }
        }
    }

}

