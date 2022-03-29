package net.thumbtack.school.concert.service;

import com.google.gson.Gson;
import net.thumbtack.school.concert.dao.SongRatingDao;
import net.thumbtack.school.concert.daoImpl.SongRatingDaoImpl;
import net.thumbtack.school.concert.dto.AddSongRatingDto;
import net.thumbtack.school.concert.dto.ChangeSongRatingDto;
import net.thumbtack.school.concert.dto.DeleteSongRatingDto;
import net.thumbtack.school.concert.errors.ServerErrorCode;
import net.thumbtack.school.concert.errors.ServerException;
import net.thumbtack.school.concert.model.Checkers;
import net.thumbtack.school.concert.model.SessionData;
import net.thumbtack.school.concert.model.SongRating;

public class SongRatingService {

    public static SongRatingDao songRatingDao = new SongRatingDaoImpl();

    public void setSongRatingDao (SongRatingDao songRatingDao) {
        this.songRatingDao = songRatingDao;
    }

    public static String addSongRating(AddSongRatingDto addSongRatingDto) throws ServerException {

        if (Checkers.checkSessionData(addSongRatingDto.getSessionData()) &&
                Checkers.checkSongName(addSongRatingDto.getSongName()) &&
                Checkers.checkSingerName(addSongRatingDto.getSingerName()) &&
                Checkers.checkRating(addSongRatingDto.getRating())) {
            songRatingDao.addNewSongRating
                    (new SongRating(addSongRatingDto.getSessionData().getCurrentUserLogin(), addSongRatingDto.getRating()),
                            addSongRatingDto.getSongName(), addSongRatingDto.getSingerName());
            Gson gson = new Gson();
            return gson.toJson(addSongRatingDto.getSessionData());
        } else {
            return null;
        }
    }


    public static String changeSongRating(ChangeSongRatingDto changeSongRatingDto) throws ServerException {

        if (Checkers.checkSessionData(changeSongRatingDto.getSessionData()) &&
                Checkers.checkSongName(changeSongRatingDto.getSongName()) &&
                Checkers.checkSingerName(changeSongRatingDto.getSingerName()) &&
                Checkers.checkRating(changeSongRatingDto.getRating())) {
            songRatingDao.changeSongRating(changeSongRatingDto.getSongName(), changeSongRatingDto.getSingerName(),
                    changeSongRatingDto.getSessionData().getCurrentUserLogin(), changeSongRatingDto.getRating());
            Gson gson = new Gson();
            return gson.toJson(changeSongRatingDto.getSessionData());
        } else {
            return null;
        }
    }

    public static String deleteSongRating(DeleteSongRatingDto deleteSongRatingDto) throws ServerException {

        if (Checkers.checkSessionData(deleteSongRatingDto.getSessionData()) &&
                Checkers.checkSongName(deleteSongRatingDto.getSongName()) &&
                Checkers.checkSingerName(deleteSongRatingDto.getSingerName())
        ) {
            songRatingDao.deleteSongRating(deleteSongRatingDto.getSongName(), deleteSongRatingDto.getSingerName(),
                    deleteSongRatingDto.getSessionData().getCurrentUserLogin());
            Gson gson = new Gson();
            return gson.toJson(deleteSongRatingDto.getSessionData());
        } else {
            return null;
        }
    }
}
