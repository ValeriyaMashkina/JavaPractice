package net.thumbtack.school.concert.dto;

import net.thumbtack.school.concert.model.Comment;
import net.thumbtack.school.concert.model.SessionData;
import net.thumbtack.school.concert.model.SongRating;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Objects;

public class DeleteSongDto implements Serializable {
    private static final long serialVersionUID = 803745049486954915L;
    private SessionData sessionData;
    private String songName;
    private String singerName;

    public DeleteSongDto(SessionData sessionData,
                         String songName,
                         String singerName) {
        setSessionData(sessionData);
        setSongName(songName);
        setSingerName(singerName);
    }

    public SessionData getSessionData() {
        return this.sessionData;
    }

    public void setSessionData(SessionData sessionData) {
        this.sessionData = sessionData;
    }

    public String getSongName() {
        return this.songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSingerName() {
        return this.singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeleteSongDto)) return false;
        DeleteSongDto that = (DeleteSongDto) o;
        return Objects.equals(sessionData, that.sessionData) &&
                Objects.equals(songName, that.songName) &&
                Objects.equals(singerName, that.singerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionData, songName, singerName);
    }
}
