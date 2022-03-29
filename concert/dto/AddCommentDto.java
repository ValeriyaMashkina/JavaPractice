package net.thumbtack.school.concert.dto;

import net.thumbtack.school.concert.model.Comment;
import net.thumbtack.school.concert.model.SessionData;
import net.thumbtack.school.concert.model.SongRating;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

public class AddCommentDto implements Serializable {

    private static final long serialVersionUID = 803745049486954915L;
    private SessionData sessionData;
    private String text;
    private String songName;
    private String singerName;

    public AddCommentDto(SessionData sessionData, String text, String songName, String singerName) {
        setSessionData(sessionData);
        setText(text);
        setSongName(songName);
        setSingerName(singerName);
    }

    public SessionData getSessionData() {
        return sessionData;
    }

    public void setSessionData(SessionData sessionData) {
        this.sessionData = sessionData;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddCommentDto)) return false;
        AddCommentDto that = (AddCommentDto) o;
        return Objects.equals(sessionData, that.sessionData) &&
                Objects.equals(text, that.text) &&
                Objects.equals(songName, that.songName) &&
                Objects.equals(singerName, that.singerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionData, text, songName, singerName);
    }
}
