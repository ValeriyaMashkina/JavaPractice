package net.thumbtack.school.concert.dao;

import net.thumbtack.school.concert.model.Song;

import net.thumbtack.school.concert.errors.ServerException;
import net.thumbtack.school.concert.model.SongRating;


public interface SongDao {
    void addNewSong(Song newSong) throws ServerException;

    void deleteSong(String userLogin, String songName, String singerName) throws ServerException;
}
