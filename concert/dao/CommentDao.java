package net.thumbtack.school.concert.dao;

import net.thumbtack.school.concert.errors.ServerException;
import net.thumbtack.school.concert.model.Comment;

public interface CommentDao {
    void addComment(String songName, String singerName, Comment comment) throws ServerException;

    void changeComment(String songName, String singerName, String userLogin, String text) throws ServerException;

    void joinComment(String songName, String singerName, String commentAutor, String text, String joinedUser) throws ServerException;

    void deleteJoinComment(String songName, String singerName, String commentAutor, String text, String joinedUser) throws ServerException;
}
