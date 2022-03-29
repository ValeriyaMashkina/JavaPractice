package net.thumbtack.school.concert.server;

import com.google.gson.Gson;
import net.thumbtack.school.concert.database.Database;
import net.thumbtack.school.concert.dto.*;
import net.thumbtack.school.concert.errors.ServerException;
import net.thumbtack.school.concert.service.*;
import net.thumbtack.school.concert.model.SessionData;

import java.io.IOException;

public class Server {

    public static boolean on = false;

    public void startServer(String savedDataFileName) throws IOException {
        if (savedDataFileName != null && !savedDataFileName.trim().isEmpty()) {
            on = true;
            Database.getInstance().deserializeDatabaseFromJsonFile(savedDataFileName);
        } else {
            Database.getInstance().cleanBase();
        }
    }


    public void stopServer(String savedDataFileName) throws IOException {
        if (savedDataFileName != null && !savedDataFileName.trim().isEmpty()) {
            Database.getInstance().serializeDatabaseToJsonFile(savedDataFileName);
            on = false;
        }
    }


    public String registerUser(String requestJsonString) throws ServerException {
        Gson gson = new Gson();
        RegisterUserDto userData = gson.fromJson(requestJsonString, RegisterUserDto.class);
        return UserService.registerUser(userData);
    }


    public String loginUser(String requestJsonString) throws ServerException {
        Gson gson = new Gson();
        LoginDto userData = gson.fromJson(requestJsonString, LoginDto.class);
        return UserService.loginUser(userData);
    }

    public String logoutUser(String requestJsonString) throws ServerException {
        Gson gson = new Gson();
        SessionData sessionData = gson.fromJson(requestJsonString, SessionData.class);
        return UserService.logoutUser(sessionData);
    }

    public String deleteUser(String requestJsonString) throws ServerException {
        Gson gson = new Gson();
        SessionData sessionData = gson.fromJson(requestJsonString, SessionData.class);
        return UserService.deleteUser(sessionData);
    }

    public String addSong(String requestJsonString) throws ServerException {
        Gson gson = new Gson();
        AddSongDto addSongDto = gson.fromJson(requestJsonString, AddSongDto.class);
        return SongService.addSong(addSongDto);
    }

    public String deleteSong(String requestJsonString) throws ServerException {
        Gson gson = new Gson();
        DeleteSongDto deleteSongDto = gson.fromJson(requestJsonString, DeleteSongDto.class);
        return SongService.deleteSong(deleteSongDto);
    }

    public String addSongRating(String requestJsonString) throws ServerException {
        Gson gson = new Gson();
        AddSongRatingDto addSongRatingDto = gson.fromJson(requestJsonString, AddSongRatingDto.class);
        return SongRatingService.addSongRating(addSongRatingDto);
    }

    public String changeSongRating(String requestJsonString) throws ServerException {
        Gson gson = new Gson();
        ChangeSongRatingDto changeSongRatingDto = gson.fromJson(requestJsonString, ChangeSongRatingDto.class);
        return SongRatingService.changeSongRating(changeSongRatingDto);
    }

    public String deleteSongRating(String requestJsonString) throws ServerException {
        Gson gson = new Gson();
        DeleteSongRatingDto deleteSongRatingDto = gson.fromJson(requestJsonString, DeleteSongRatingDto.class);
        return SongRatingService.deleteSongRating(deleteSongRatingDto);
    }

    public String addComment(String requestJsonString) throws ServerException {
        Gson gson = new Gson();
        AddCommentDto addCommentDto = gson.fromJson(requestJsonString, AddCommentDto.class);
        return CommentService.addComment(addCommentDto);
    }

    public String changeComment(String requestJsonString) throws ServerException {
        Gson gson = new Gson();
        AddCommentDto addCommentDto = gson.fromJson(requestJsonString, AddCommentDto.class);
        return CommentService.changeComment(addCommentDto);
    }


    public String joinComment(String requestJsonString) throws ServerException {
        Gson gson = new Gson();
        JoinCommentDto joinCommentDto = gson.fromJson(requestJsonString, JoinCommentDto.class);
        return CommentService.joinComment(joinCommentDto);
    }

    public String deleteJoinComment(String requestJsonString) throws ServerException {
        Gson gson = new Gson();
        JoinCommentDto joinCommentDto = gson.fromJson(requestJsonString, JoinCommentDto.class);
        return CommentService.deleteJoinComment(joinCommentDto);
    }


    public String showAllAddedSongs(String requestJsonString) throws ServerException {
        Gson gson = new Gson();
        SessionData sessionData = gson.fromJson(requestJsonString, SessionData.class);
        return ReportService.showAllAddedSongs(sessionData);
    }

    public String showSongsSelectedByComposer(String requestJsonString) throws ServerException {
        Gson gson = new Gson();
        SelectorDto requestData = gson.fromJson(requestJsonString, SelectorDto.class);
        return ReportService.showSongsSelectedByComposer(requestData);
    }

    public String showSongsSelectedByLyricAutor(String requestJsonString) throws ServerException {
        Gson gson = new Gson();
        SelectorDto requestData = gson.fromJson(requestJsonString, SelectorDto.class);
        return ReportService.showSongsSelectedByLyricAutor(requestData);
    }

    public String showSongsSelectedBySinger(String requestJsonString) throws ServerException {
        Gson gson = new Gson();
        SelectorDto requestData = gson.fromJson(requestJsonString, SelectorDto.class);
        return ReportService.showSongsSelectedBySinger(requestData);
    }

    public String showCurrentProgramm(String requestJsonString) throws ServerException {
        Gson gson = new Gson();
        SessionData sessionData = gson.fromJson(requestJsonString, SessionData.class);
        return ReportService.showCurrentProgramm(sessionData);
    }

}
