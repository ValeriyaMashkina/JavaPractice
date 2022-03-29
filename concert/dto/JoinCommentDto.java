package net.thumbtack.school.concert.dto;

import net.thumbtack.school.concert.model.SessionData;

import java.io.Serializable;
import java.util.Objects;

public class JoinCommentDto implements Serializable {

    private static final long serialVersionUID = 803745049486954915L;
    private SessionData sessionData;
    private String songName;
    private String singerName;
    private String commentAutor;
    private String text;

    public JoinCommentDto(SessionData sessionData, String songName, String singerName, String commentAutor, String text) {
        setSessionData(sessionData);
        setSongName(songName);
        setSingerName(singerName);
        setCommentAutor(commentAutor);
        setText(text);
    }

    public SessionData getSessionData() {
        return sessionData;
    }

    public void setSessionData(SessionData sessionData) {
        this.sessionData = sessionData;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getCommentAutor() {
        return commentAutor;
    }

    public void setCommentAutor(String commentAutor) {
        this.commentAutor = commentAutor;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JoinCommentDto)) return false;
        JoinCommentDto that = (JoinCommentDto) o;
        return Objects.equals(sessionData, that.sessionData) &&
                Objects.equals(songName, that.songName) &&
                Objects.equals(singerName, that.singerName) &&
                Objects.equals(commentAutor, that.commentAutor) &&
                Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionData, songName, singerName, commentAutor, text);
    }
}
