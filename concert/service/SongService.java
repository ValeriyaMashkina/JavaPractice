package net.thumbtack.school.concert.service;

import com.google.gson.Gson;
import net.thumbtack.school.concert.dao.SongDao;
import net.thumbtack.school.concert.dto.AddSongDto;
import net.thumbtack.school.concert.dto.DeleteSongDto;
import net.thumbtack.school.concert.daoImpl.SongDaoImpl;
import net.thumbtack.school.concert.errors.ServerErrorCode;
import net.thumbtack.school.concert.errors.ServerException;
import net.thumbtack.school.concert.model.*;

import java.util.LinkedHashSet;


public class SongService {

    public static SongDao songDao = new SongDaoImpl();

    public void setSongDao (SongDao songDao) {
        this.songDao = songDao;
    }

    public static String addSong(AddSongDto songData) throws ServerException {


        if (Checkers.checkSessionData(songData.getSessionData()) &&
                Checkers.checkSongName(songData.getSongName()) &&
                Checkers.checkComposerName(songData.getComposerName()) &&
                Checkers.checkLyricAutorName(songData.getLyricAutorName()) &&
                Checkers.checkSingerName(songData.getSingerName()) &&
                Checkers.checkDuration(songData.getDuration())
        ) {
            if (songData.getUserWhoProposed() == null ||
                    songData.getUserWhoProposed().equals("") ||
                    songData.getUserWhoProposed().equals(" ") ||
                    songData.getUserWhoProposed().trim().isEmpty() ||
                    songData.getUserWhoProposed().equals("community") ||
                    !songData.getUserWhoProposed().equals(songData.getSessionData().getCurrentUserLogin())) {
                throw new ServerException(ServerErrorCode.INVALID_USER_NAME);
            } else if (songData.getSongsComments() == null ||
                    songData.getSongsComments().size() != 0 ||
                    songData.getRatings() == null ||
                    songData.getRatings().size() != 1 ||
                    !songData.getRatings().containsKey(songData.getUserWhoProposed())) {
                throw new ServerException(ServerErrorCode.INVALID_LIST_OF_SONGS_RATINGS_OR_COMMENTS);
            } else {
                songDao.addNewSong
                        (new Song(songData.getSongName(), songData.getComposerName(), songData.getLyricAutorName(),
                                songData.getSingerName(), songData.getDuration(), songData.getUserWhoProposed(),
                                songData.getSongsComments(), songData.getRatings()));


                Gson gson = new Gson();
                return gson.toJson(songData.getSessionData());
            }
        } else {
            return null;
        }
    }





    public static String deleteSong(DeleteSongDto songData) throws ServerException {

        if (Checkers.checkSessionData(songData.getSessionData()) &&
                Checkers.checkSongName(songData.getSongName()) &&
                Checkers.checkSingerName(songData.getSingerName())) {
            songDao.deleteSong(songData.getSessionData().getCurrentUserLogin(), songData.getSongName(), songData.getSingerName());
            Gson gson = new Gson();
            return gson.toJson(songData.getSessionData());
        } else {
            return null;
        }
    }
}
