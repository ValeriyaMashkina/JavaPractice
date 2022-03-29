package net.thumbtack.school.concert.dao;

import net.thumbtack.school.concert.errors.ServerException;
import net.thumbtack.school.concert.model.SongRating;


public interface SongRatingDao {

    void addNewSongRating(SongRating newSongRating, String songName, String singerName) throws ServerException;

    void changeSongRating(String songName, String singerName, String userLogin, int rating) throws ServerException;

    void deleteSongRating(String songName, String singerName, String userLogin) throws ServerException;

}
