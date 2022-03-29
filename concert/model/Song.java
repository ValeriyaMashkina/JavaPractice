package net.thumbtack.school.concert.model;

import net.thumbtack.school.concert.errors.ServerException;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Objects;

public class Song implements Serializable {
    private static final long serialVersionUID = 803745049486954915L;

    private String songName;
    private String composerName;
    private String lyricAutorName;
    private String singerName;

    private String userWhoProposed;

    private LinkedHashMap<String, SongRating> ratings = new LinkedHashMap<>();
    private LinkedHashSet<Comment> songsComments = new LinkedHashSet<>();

    private int duration;

    public Song(String songName, String composerName, String lyricAutorName, String singerName, int duration, String userWhoProposed,
                LinkedHashSet<Comment> comments, LinkedHashMap<String, SongRating> ratings) {
        setSongName(songName);
        setComposerName(composerName);
        setLyricAutorName(lyricAutorName);
        setSingerName(singerName);
        setDuration(duration);
        setUserWhoProposed(userWhoProposed);
        setSongsComments(comments);
        setRatings(ratings);
    }


    public Boolean checkSongRating(String userLogin) {
        return this.getRatings().containsKey(userLogin);
    }

    public Boolean checkComment(String userLogin, String text) {
        for (Comment i : this.getSongsComments()) {
            if (i.getCommentAutor().equals(userLogin) && i.getText().equals(text)) {
                return true;
            }
        }
        return false;
    }

    public Boolean checkComment(String userLogin) {
        for (Comment i : this.getSongsComments()) {
            if (i.getCommentAutor().equals(userLogin)) {
                return true;
            }
        }
        return false;
    }

    public Comment getComment(String userLogin, String text) {
        for (Comment i : this.getSongsComments()) {
            if (i.getCommentAutor().equals(userLogin) && i.getText().equals(text)) {
                return i;
            }
        }
        return null;
    }

    public Comment getComment(String userLogin) {
        for (Comment i : this.getSongsComments()) {
            if (i.getCommentAutor().equals(userLogin)) {
                return i;
            }
        }
        return null;
    }


    public SongRating getSongRating(String userLogin) {
        return this.getRatings().get(userLogin);
    }

    public void removeSongRating(SongRating songRating) {
        this.getRatings().remove(songRating.getUserWhoRated());
    }


    public void setUserWhoProposed(String userWhoProposed) {
        this.userWhoProposed = userWhoProposed;
    }

    public String getUserWhoProposed() {
        return this.userWhoProposed;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongName() {
        return this.songName;
    }

    public void setComposerName(String composerName) {
        this.composerName = composerName;
    }

    public String getComposerName() {
        return this.composerName;
    }

    public void setLyricAutorName(String lyricAutorName) {
        this.lyricAutorName = lyricAutorName;
    }

    public String getLyricAutorName() {
        return this.lyricAutorName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getSingerName() {
        return this.singerName;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setSongsComments(LinkedHashSet<Comment> comments) {
        this.songsComments = comments;
    }

    public void setRatings(LinkedHashMap<String, SongRating> ratings) {
        this.ratings = ratings;
    }

    public LinkedHashSet<Comment> getSongsComments() {
        return this.songsComments;
    }

    public LinkedHashMap<String, SongRating> getRatings() {
        return this.ratings;
    }

    public void addComment(Comment comment) {
        this.songsComments.add(comment);
    }

    public void addRating(SongRating rating) {
        this.ratings.put(rating.getUserWhoRated(), rating);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Song)) return false;
        Song song = (Song) o;
        return duration == song.duration &&
                Objects.equals(songName, song.songName) &&
                Objects.equals(composerName, song.composerName) &&
                Objects.equals(lyricAutorName, song.lyricAutorName) &&
                Objects.equals(singerName, song.singerName) &&
                Objects.equals(userWhoProposed, song.userWhoProposed) &&
                Objects.equals(songsComments, song.songsComments) &&
                Objects.equals(ratings, song.ratings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(songName, composerName, lyricAutorName, singerName, duration, userWhoProposed, songsComments, ratings);
    }
}
