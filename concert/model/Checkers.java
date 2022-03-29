package net.thumbtack.school.concert.model;

import net.thumbtack.school.concert.errors.ServerErrorCode;
import net.thumbtack.school.concert.errors.ServerException;

public class Checkers {

    public static Boolean checkSessionData(SessionData sessionData) throws ServerException {

        if (sessionData.getToken() == null ||
                sessionData.getValid() == false ||
                sessionData.getCurrentUserLogin() == null ||
                sessionData.getCurrentUserLogin().equals("community") ||
                sessionData.getCurrentUserLogin().equals(" ") ||
                sessionData.getCurrentUserLogin().equals("") ||
                sessionData.getCurrentUserLogin().trim().isEmpty()) {
            throw new ServerException(ServerErrorCode.INVALID_SESSION);
        } else {
            return true;
        }
    }


    public static Boolean checkSongName(String songName) throws ServerException {

        if (songName == null ||
                songName.equals("") ||
                songName.equals(" ") ||
                songName.trim().isEmpty()) {
            throw new ServerException(ServerErrorCode.INVALID_SONG_NAME);
        } else {
            return true;
        }
    }


    public static Boolean checkSingerName(String singerName) throws ServerException {

        if (singerName == null || singerName.equals("") ||
                singerName.equals(" ") || singerName.contains(",") ||
                singerName.contains("&") || singerName.contains(" and ") ||
                singerName.contains(" и ") || singerName.length() > 25 ||
                singerName.trim().isEmpty()) {
            throw new ServerException(ServerErrorCode.INVALID_SINGER_NAME);
        } else {
            return true;
        }
    }

    public static Boolean checkCommentText(String commentText) throws ServerException {

        if (commentText == null || commentText.equals("") ||
                commentText.equals(" ") || commentText.trim().isEmpty()) {
            throw new ServerException(ServerErrorCode.INVALID_COMMENT_TEXT);
        } else {
            return true;
        }
    }

    public static Boolean checkCommentAutor(String commentAutor) throws ServerException {
        if (commentAutor == null || commentAutor.equals("") ||
                commentAutor.equals(" ") || commentAutor.trim().isEmpty()) {
            throw new ServerException(ServerErrorCode.INVALID_COMMENT_AUTHOR);
        } else {
            return true;
        }
    }

    public static Boolean checkRating(int rating) throws ServerException {
        if (rating < 1 || rating > 5) {
            throw new ServerException(ServerErrorCode.INVALID_RATING);
        } else {
            return true;
        }
    }

    public static Boolean checkComposerName(String composerName) throws ServerException {
        if (composerName == null ||
                composerName.equals("") ||
                composerName.equals(" ") ||
                composerName.trim().isEmpty()) {
            throw new ServerException(ServerErrorCode.INVALID_SONG_COMPOSER_NAME);
        } else {
            return true;
        }
    }

    public static Boolean checkLyricAutorName(String lyricAutorName) throws ServerException {
        if (lyricAutorName == null || lyricAutorName.equals("") ||
                lyricAutorName.equals(" ") || lyricAutorName.trim().isEmpty()) {
            throw new ServerException(ServerErrorCode.INVALID_LYRIC_AUTOR_NAME);
        } else {
            return true;
        }
    }

    public static Boolean checkDuration(int duration) throws ServerException {
        if (duration <= 0) {
            throw new ServerException(ServerErrorCode.INVALID_SONG_DURATION);
        } else {
            return true;
        }
    }


    public static Boolean checkSurname(String surname) throws ServerException {
        if (surname == null || surname.trim().isEmpty()) {
            throw new ServerException(ServerErrorCode.INVALID_USER_SURNAME);
        } else {
            return true;
        }
    }

    public static Boolean checkName(String name) throws ServerException {
        if (name == null || name.trim().isEmpty()) {
            throw new ServerException(ServerErrorCode.INVALID_USER_NAME);
        } else {
            return true;
        }
    }

    public static Boolean checkLogin(String login) throws ServerException {
        if (login == null || login.equals("community") || login.trim().isEmpty() || login.length() < 5) {
            throw new ServerException(ServerErrorCode.INVALID_USER_LOGIN);
        } else {
            return true;
        }
    }

    public static Boolean checkPassword(String password) throws ServerException {
        if (password == null || password.trim().isEmpty() || password.length() < 5) {
            throw new ServerException(ServerErrorCode.INVALID_USER_PASSWORD);
        } else {
            return true;
        }
    }
}
