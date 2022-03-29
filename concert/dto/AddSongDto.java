package net.thumbtack.school.concert.dto;

import net.thumbtack.school.concert.model.SessionData;
import net.thumbtack.school.concert.model.Comment;
import net.thumbtack.school.concert.model.SongRating;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Objects;

public class AddSongDto implements Serializable {

    private static final long serialVersionUID = 803745049486954915L;
    private SessionData sessionData;
    private String songName; // "", " ", null - проверяется в DAO Song
    private String composerName; // "", " ", null - проверяется в DAO Song
    private String lyricAutorName; // "", " ", null - проверяется в DAO Song
    private String singerName; // всегда один!!! - проверяется в DAO Song
    private int duration; // 0 < проверяется в DAO Song
    private String userWhoProposed;
    private LinkedHashSet<Comment> songsComments = new LinkedHashSet<Comment>();
    private LinkedHashMap<String, SongRating> ratings = new LinkedHashMap<String, SongRating>();


    public AddSongDto(SessionData sessionData,
                      String songName,
                      String composerName,
                      String lyricAutorName,
                      String singerName,
                      int duration) {
        setSessionData(sessionData);
        setSongName(songName);
        setComposerName(composerName);
        setLyricAutorName(lyricAutorName);
        setSingerName(singerName);
        setDuration(duration);
        setUserWhoProposed(sessionData.getCurrentUserLogin());
        setSongsComments(new LinkedHashSet<Comment>());

        LinkedHashMap<String, SongRating> rate = new LinkedHashMap<String, SongRating>();
        rate.put(sessionData.getCurrentUserLogin(), new SongRating(sessionData.getCurrentUserLogin(), 5));
        setRatings(rate);
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

    public String getComposerName() {
        return this.composerName;
    }

    public void setComposerName(String composerName) {
        this.composerName = composerName;
    }

    public String getLyricAutorName() {
        return this.lyricAutorName;
    }

    public void setLyricAutorName(String lyricAutorName) {
        this.lyricAutorName = lyricAutorName;
    }

    public String getSingerName() {
        return this.singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getUserWhoProposed() {
        return this.userWhoProposed;
    }

    public void setUserWhoProposed(String userWhoProposed) {
        this.userWhoProposed = userWhoProposed;
    }

    public LinkedHashSet<Comment> getSongsComments() {
        return this.songsComments;
    }

    public void setSongsComments(LinkedHashSet<Comment> songsComments) {
        this.songsComments = songsComments;
    }

    public LinkedHashMap<String, SongRating> getRatings() {
        return this.ratings;
    }

    public void setRatings(LinkedHashMap<String, SongRating> ratings) {
        this.ratings = ratings;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddSongDto)) return false;
        AddSongDto that = (AddSongDto) o;
        return duration == that.duration &&
                Objects.equals(sessionData, that.sessionData) &&
                Objects.equals(songName, that.songName) &&
                Objects.equals(composerName, that.composerName) &&
                Objects.equals(lyricAutorName, that.lyricAutorName) &&
                Objects.equals(singerName, that.singerName) &&
                Objects.equals(userWhoProposed, that.userWhoProposed) &&
                Objects.equals(songsComments, that.songsComments) &&
                Objects.equals(ratings, that.ratings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionData, songName, composerName,
                lyricAutorName, singerName, duration,
                userWhoProposed, songsComments, ratings);
    }
}
