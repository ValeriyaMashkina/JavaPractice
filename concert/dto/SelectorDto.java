package net.thumbtack.school.concert.dto;

import net.thumbtack.school.concert.model.SessionData;

import java.io.Serializable;
import java.util.Objects;

public class SelectorDto implements Serializable {

    private static final long serialVersionUID = 803745049486954915L;
    private SessionData sessionData;
    private String selector;


    public SelectorDto(SessionData sessionData, String selector) {
        setSessionData(sessionData);
        setSelector(selector);
    }

    public SessionData getSessionData() {
        return sessionData;
    }

    public void setSessionData(SessionData sessionData) {
        this.sessionData = sessionData;
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SelectorDto)) return false;
        SelectorDto that = (SelectorDto) o;
        return Objects.equals(sessionData, that.sessionData) &&
                Objects.equals(selector, that.selector);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionData, selector);
    }
}
