package net.thumbtack.school.concert.model;

import net.thumbtack.school.concert.errors.ServerErrorCode;
import net.thumbtack.school.concert.errors.ServerException;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class SessionData implements Serializable {
    private static final long serialVersionUID = 803745049486954915L;
    private UUID token;
    private Boolean valid;
    private String currentUserLogin;


    public SessionData(UUID token, Boolean valid, String currentUserLogin) {
        setToken(token);
        setValid(valid);
        setCurrentUserLogin(currentUserLogin);
    }


    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public String getCurrentUserLogin() {
        return currentUserLogin;
    }

    public void setCurrentUserLogin(String currentUserLogin) {
        this.currentUserLogin = currentUserLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SessionData)) return false;
        SessionData that = (SessionData) o;
        return Objects.equals(token, that.token) &&
                Objects.equals(valid, that.valid) &&
                Objects.equals(currentUserLogin, that.currentUserLogin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, valid, currentUserLogin);
    }
}
