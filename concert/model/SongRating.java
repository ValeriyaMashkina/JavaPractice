package net.thumbtack.school.concert.model;

import java.io.Serializable;
import java.util.Objects;

public class SongRating implements Serializable {
    private static final long serialVersionUID = 803745049486954915L;

    private String userWhoRated;
    private int rating;


    public SongRating(String userWhoRated, int rating) {
        setUserWhoRated(userWhoRated);
        setRating(rating);
    }

    public void setUserWhoRated(String userWhoRated) {
        this.userWhoRated = userWhoRated;
    }

    public String getUserWhoRated() {
        return userWhoRated;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SongRating)) return false;
        SongRating that = (SongRating) o;
        return rating == that.rating &&
                Objects.equals(userWhoRated, that.userWhoRated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userWhoRated, rating);
    }
}
