package net.thumbtack.school.concert.model;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Objects;

public class ProgrammRecord implements Serializable {

    private static final long serialVersionUID = 803745049486954915L;
    private String songName;
    private String composerName;
    private String lyricAutorName;
    private String singerName;
    private String userWhoProposed;
    private Double averageRating;
    private LinkedHashSet<Comment> songsComments;

    public ProgrammRecord(String songName, String composerName, String lyricAutorName,
                          String singerName, String userWhoProposed, Double averageRating,
                          LinkedHashSet<Comment> songsComments) {
        setSongName(songName);
        setComposerName(composerName);
        setLyricAutorName(lyricAutorName);
        setSingerName(singerName);
        setUserWhoProposed(userWhoProposed);
        setAverageRating(averageRating);
        setSongsComments(songsComments);
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getComposerName() {
        return composerName;
    }

    public void setComposerName(String composerName) {
        this.composerName = composerName;
    }

    public String getLyricAutorName() {
        return lyricAutorName;
    }

    public void setLyricAutorName(String lyricAutorName) {
        this.lyricAutorName = lyricAutorName;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getUserWhoProposed() {
        return userWhoProposed;
    }

    public void setUserWhoProposed(String userWhoProposed) {
        this.userWhoProposed = userWhoProposed;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public LinkedHashSet<Comment> getSongsComments() {
        return songsComments;
    }

    public void setSongsComments(LinkedHashSet<Comment> songsComments) {
        this.songsComments = songsComments;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProgrammRecord)) return false;
        ProgrammRecord that = (ProgrammRecord) o;
        return Objects.equals(songName, that.songName) &&
                Objects.equals(composerName, that.composerName) &&
                Objects.equals(lyricAutorName, that.lyricAutorName) &&
                Objects.equals(singerName, that.singerName) &&
                Objects.equals(userWhoProposed, that.userWhoProposed) &&
                Objects.equals(averageRating, that.averageRating) &&
                Objects.equals(songsComments, that.songsComments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(songName, composerName, lyricAutorName, singerName, userWhoProposed, averageRating, songsComments);
    }
}
