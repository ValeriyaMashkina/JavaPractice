package net.thumbtack.school.concert.service;


import com.google.gson.Gson;


import net.thumbtack.school.concert.dao.ReportDao;
import net.thumbtack.school.concert.daoImpl.ReportDaoImpl;
import net.thumbtack.school.concert.dto.SelectorDto;
import net.thumbtack.school.concert.errors.ServerException;
import net.thumbtack.school.concert.model.Checkers;
import net.thumbtack.school.concert.model.SessionData;

public class ReportService {

    public static ReportDao requestDao = new ReportDaoImpl();

    public void setReportDao (ReportDao requestDao) {
        this.requestDao = requestDao;
    }

    public static String showAllAddedSongs(SessionData sessionData) throws ServerException {

        if (Checkers.checkSessionData(sessionData)) {
            Gson gson = new Gson();
            return gson.toJson(requestDao.showAllAddedSongs());
        } else {
            return null;
        }
    }

    public static String showCurrentProgramm(SessionData sessionData) throws ServerException {

        if (Checkers.checkSessionData(sessionData)) {
            Gson gson = new Gson();
            return gson.toJson(requestDao.showCurrentProgramm());
        } else {
            return null;
        }
    }


    public static String showSongsSelectedByComposer(SelectorDto selectorDto) throws ServerException {

        if (Checkers.checkSessionData(selectorDto.getSessionData())) {
            Gson gson = new Gson();
            return gson.toJson(requestDao.showSongsSelectedByComposer(selectorDto.getSelector()));
        } else {
            return null;
        }
    }

    public static String showSongsSelectedByLyricAutor(SelectorDto selectorDto) throws ServerException {

        if (Checkers.checkSessionData(selectorDto.getSessionData())) {
            Gson gson = new Gson();
            return gson.toJson(requestDao.showSongsSelectedByLyricAutor(selectorDto.getSelector()));
        } else {
            return null;
        }
    }

    public static String showSongsSelectedBySinger(SelectorDto selectorDto) throws ServerException {
        if (Checkers.checkSessionData(selectorDto.getSessionData())) {
            Gson gson = new Gson();
            return gson.toJson(requestDao.showSongsSelectedBySinger(selectorDto.getSelector()));
        } else {
            return null;
        }
    }

}
