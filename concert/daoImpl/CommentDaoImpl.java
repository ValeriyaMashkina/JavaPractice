package net.thumbtack.school.concert.daoImpl;

import net.thumbtack.school.concert.dao.CommentDao;
import net.thumbtack.school.concert.database.Database;
import net.thumbtack.school.concert.errors.ServerErrorCode;
import net.thumbtack.school.concert.errors.ServerException;
import net.thumbtack.school.concert.model.Comment;
import net.thumbtack.school.concert.model.Song;

import java.util.LinkedHashSet;

public class CommentDaoImpl implements CommentDao {

    @Override
    public void addComment(String songName, String singerName, Comment comment) throws ServerException {
        if (!Database.getInstance().checkSong(songName, singerName)) {
            throw new ServerException(ServerErrorCode.SONG_DOES_NOT_EXISTS);
        } else {
            Song song = Database.getInstance().getSong(songName, singerName);

            if (song.getUserWhoProposed().equals(comment.getCommentAutor())) {
                throw new ServerException(ServerErrorCode.INVALID_COMMENT_ATTEMPT);
            } else {
                if (song.checkComment(comment.getCommentAutor())) {
                    throw new ServerException(ServerErrorCode.USER_ALREADY_LEFT_COMMENT);
                } else {
                    song.getSongsComments().add(comment);
                }
            }
        }
    }

    @Override
    public void changeComment(String songName, String singerName, String userLogin, String text) throws ServerException {
        if (!Database.getInstance().checkSong(songName, singerName)) {
            throw new ServerException(ServerErrorCode.SONG_DOES_NOT_EXISTS);
        } else {
            Song song = Database.getInstance().getSong(songName, singerName);
            if (!song.checkComment(userLogin)) {
                throw new ServerException(ServerErrorCode.COMMENT_DOES_NOT_EXIST);
            } else {
                Comment comment = song.getComment(userLogin);
                if (comment.getJoinedUsers().isEmpty() || comment.getJoinedUsers().size() == 0) {
                    comment.setText(text);
                } else {
                    comment.setLoginCommentAutor("community");
                    song.getSongsComments().add(new Comment(userLogin,
                            text, new LinkedHashSet<String>()));
                }
            }
        }
    }

    @Override
    public void joinComment(String songName, String singerName, String commentAutor, String text, String joinedUser) throws ServerException {
        if (!Database.getInstance().checkSong(songName, singerName)) {
            throw new ServerException(ServerErrorCode.SONG_DOES_NOT_EXISTS);
        } else {

            Song song = Database.getInstance().getSong(songName, singerName);
            if (song.getUserWhoProposed().equals(commentAutor)) {
                throw new ServerException(ServerErrorCode.INVALID_COMMENT_ATTEMPT);
            } else if (song.getUserWhoProposed().equals(joinedUser)) {
                throw new ServerException(ServerErrorCode.INVALID_JOINED_USER);
            } else if (!song.checkComment(commentAutor, text)) {
                throw new ServerException(ServerErrorCode.COMMENT_DOES_NOT_EXIST);
            } else if (song.getComment(commentAutor, text).getJoinedUsers().contains(joinedUser)) {
                throw new ServerException(ServerErrorCode.USER_HAS_ALREADY_JOINED);
            } else {
                song.getComment(commentAutor, text).addJoinedUser(joinedUser);
            }
        }
    }


    @Override
    public void deleteJoinComment(String songName, String singerName, String commentAutor, String text, String joinedUser) throws ServerException {
        if (!Database.getInstance().checkSong(songName, singerName)) {
            throw new ServerException(ServerErrorCode.SONG_DOES_NOT_EXISTS);
        } else {

            Song song = Database.getInstance().getSong(songName, singerName);
            if (song.getUserWhoProposed().equals(commentAutor)) {
                throw new ServerException(ServerErrorCode.INVALID_COMMENT_ATTEMPT);
            } else if (song.getUserWhoProposed().equals(joinedUser)) {
                throw new ServerException(ServerErrorCode.INVALID_JOINED_USER);
            } else if (!song.checkComment(commentAutor, text)) {
                throw new ServerException(ServerErrorCode.COMMENT_DOES_NOT_EXIST);
            } else if (!song.getComment(commentAutor, text).getJoinedUsers().contains(joinedUser)) {
                throw new ServerException(ServerErrorCode.USER_HAS_NOT_JOINED_YET);
            } else {
                song.getComment(commentAutor, text).removeJoinedUser(joinedUser);
            }
        }
    }
}
