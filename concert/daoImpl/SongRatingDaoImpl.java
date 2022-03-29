package net.thumbtack.school.concert.daoImpl;

import net.thumbtack.school.concert.dao.SongRatingDao;
import net.thumbtack.school.concert.database.Database;
import net.thumbtack.school.concert.errors.ServerErrorCode;
import net.thumbtack.school.concert.errors.ServerException;
import net.thumbtack.school.concert.model.Song;
import net.thumbtack.school.concert.model.SongRating;

public class SongRatingDaoImpl implements SongRatingDao {

    @Override
    public void addNewSongRating(SongRating newSongRating, String songName, String singerName) throws ServerException {

        if (Database.getInstance().checkSong(songName, singerName)) {

            Song song = Database.getInstance().getSong(songName, singerName);
            if (song.getRatings().containsKey(newSongRating.getUserWhoRated())) {
                throw new ServerException(ServerErrorCode.USER_ALREADY_RATED);
            } else {
                song.getRatings().put(newSongRating.getUserWhoRated(), newSongRating);
            }

        } else {
            throw new ServerException(ServerErrorCode.SONG_DOES_NOT_EXISTS);
        }

    }

    @Override
    public void changeSongRating(String songName, String singerName, String userLogin, int rating) throws ServerException {
        if (Database.getInstance().checkSong(songName, singerName)) {
            Song song = Database.getInstance().getSong(songName, singerName);

            if (song.checkSongRating(userLogin)) {
                song.getSongRating(userLogin).setRating(rating);
            } else {
                throw new ServerException(ServerErrorCode.USER_HAS_NOT_RATED);
            }
        } else {
            throw new ServerException(ServerErrorCode.SONG_DOES_NOT_EXISTS);
        }
    }

    @Override
    public void deleteSongRating(String songName, String singerName, String userLogin) throws ServerException {
        if (Database.getInstance().checkSong(songName, singerName)) {
            Song song = Database.getInstance().getSong(songName, singerName);

            if (song.checkSongRating(userLogin)) {
                song.getRatings().remove(userLogin);
            } else {
                throw new ServerException(ServerErrorCode.USER_HAS_NOT_RATED);
            }
        } else {
            throw new ServerException(ServerErrorCode.SONG_DOES_NOT_EXISTS);
        }
    }
}
