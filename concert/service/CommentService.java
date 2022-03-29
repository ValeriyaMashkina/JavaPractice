package net.thumbtack.school.concert.service;

import com.google.gson.Gson;
import net.thumbtack.school.concert.dao.CommentDao;
import net.thumbtack.school.concert.daoImpl.CommentDaoImpl;
import net.thumbtack.school.concert.dto.AddCommentDto;
import net.thumbtack.school.concert.dto.JoinCommentDto;
import net.thumbtack.school.concert.errors.ServerErrorCode;
import net.thumbtack.school.concert.errors.ServerException;
import net.thumbtack.school.concert.model.Checkers;
import net.thumbtack.school.concert.model.Comment;
import net.thumbtack.school.concert.model.SessionData;

import java.util.LinkedHashSet;
import java.util.List;

public class CommentService {

    public static CommentDao commentDao = new CommentDaoImpl();

    public void setCommentDao (CommentDao commentDao) {
        this.commentDao = commentDao;
    }

    public static Boolean checkComment(AddCommentDto addCommentDto) throws ServerException {
        if (Checkers.checkSessionData(addCommentDto.getSessionData()) &&
                Checkers.checkSongName(addCommentDto.getSongName()) &&
                Checkers.checkSingerName(addCommentDto.getSingerName()) &&
                Checkers.checkCommentText(addCommentDto.getText())) {
            return true;
        } else {
            return false;
        }
    }


    public static String addComment(AddCommentDto addCommentDto) throws ServerException {
        if (checkComment(addCommentDto)) {
            commentDao.addComment(addCommentDto.getSongName(), addCommentDto.getSingerName(),
                    new Comment(addCommentDto.getSessionData().getCurrentUserLogin(), addCommentDto.getText(),
                            new LinkedHashSet<String>()));
            Gson gson = new Gson();
            return gson.toJson(addCommentDto.getSessionData());
        } else {
            return null;
        }
    }


    public static String changeComment(AddCommentDto addCommentDto) throws ServerException {
        if (checkComment(addCommentDto)) {
            commentDao.changeComment(
                    addCommentDto.getSongName(),
                    addCommentDto.getSingerName(),
                    addCommentDto.getSessionData().getCurrentUserLogin(),
                    addCommentDto.getText()
            );
            Gson gson = new Gson();
            return gson.toJson(addCommentDto.getSessionData());
        } else {
            return null;
        }
    }


    public static Boolean checkJoinComment(JoinCommentDto joinCommentDto) throws ServerException {


        if (Checkers.checkSessionData(joinCommentDto.getSessionData()) &&
                Checkers.checkSongName(joinCommentDto.getSongName()) &&
                Checkers.checkSingerName(joinCommentDto.getSingerName()) &&
                Checkers.checkCommentText(joinCommentDto.getText()) &&
                Checkers.checkCommentAutor(joinCommentDto.getCommentAutor())) {
            if (joinCommentDto.getCommentAutor().equals(joinCommentDto.getSessionData().getCurrentUserLogin())) {
                throw new ServerException(ServerErrorCode.INVALID_JOINED_USER);
            } else {
                return true;
            }
        } else {
            return false;
        }
    }


    public static String joinComment(JoinCommentDto joinCommentDto) throws ServerException {

        if (checkJoinComment(joinCommentDto)) {
            commentDao.joinComment(
                    joinCommentDto.getSongName(),
                    joinCommentDto.getSingerName(),
                    joinCommentDto.getCommentAutor(),
                    joinCommentDto.getText(),
                    joinCommentDto.getSessionData().getCurrentUserLogin());
            Gson gson = new Gson();
            return gson.toJson(joinCommentDto.getSessionData());
        } else {
            return null;
        }
    }


    public static String deleteJoinComment(JoinCommentDto joinCommentDto) throws ServerException {

        if (checkJoinComment(joinCommentDto)) {
            commentDao.deleteJoinComment(
                    joinCommentDto.getSongName(),
                    joinCommentDto.getSingerName(),
                    joinCommentDto.getCommentAutor(),
                    joinCommentDto.getText(),
                    joinCommentDto.getSessionData().getCurrentUserLogin());
            Gson gson = new Gson();
            return gson.toJson(joinCommentDto.getSessionData());
        } else {
            return null;
        }
    }

}
