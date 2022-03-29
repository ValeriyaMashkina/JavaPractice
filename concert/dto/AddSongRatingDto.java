package net.thumbtack.school.concert.dto;

import net.thumbtack.school.concert.model.Comment;
import net.thumbtack.school.concert.model.SessionData;
import net.thumbtack.school.concert.model.SongRating;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Objects;

public class AddSongRatingDto implements Serializable {

    private static final long serialVersionUID = 803745049486954915L;
    private SessionData sessionData;
    private String songName;
    private String singerName;
    private int rating;

    public AddSongRatingDto(SessionData sessionData, String songName,
                            String singerName, int rating) {
        setSessionData(sessionData);
        setSongName(songName);
        setSingerName(singerName);
        setRating(rating);
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddSongRatingDto)) return false;
        AddSongRatingDto that = (AddSongRatingDto) o;
        return rating == that.rating &&
                Objects.equals(sessionData, that.sessionData) &&
                Objects.equals(songName, that.songName) &&
                Objects.equals(singerName, that.singerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionData, songName, singerName, rating);
    }
}