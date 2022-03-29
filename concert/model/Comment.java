package net.thumbtack.school.concert.model;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

public class Comment implements Serializable {

    private static final long serialVersionUID = 803745049486954915L;
    private String loginCommentAutor;
    private String text;
    private LinkedHashSet<String> joinedUsers;


    public Comment(String loginCommentAutor, String text, LinkedHashSet<String> joinedUsers) {
        setLoginCommentAutor(loginCommentAutor);
        setText(text);
        setJoinedUsers(joinedUsers);
    }

    public Comment returnThisComment() {
        return this;
    }

    public void setLoginCommentAutor(String loginCommentAutor) {
        this.loginCommentAutor = loginCommentAutor;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setJoinedUsers(LinkedHashSet<String> joinedUsers) {
        this.joinedUsers = joinedUsers;
    }

    public void addJoinedUser(String newUserLogin) {
        this.joinedUsers.add(newUserLogin);
    }

    public void removeJoinedUser(String userLogin) {
        this.joinedUsers.remove(userLogin);
    }

    public String getCommentAutor() {
        return this.loginCommentAutor;
    }

    public String getText() {
        return this.text;
    }

    public LinkedHashSet<String> getJoinedUsers() {
        return this.joinedUsers;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        Comment comment = (Comment) o;
        return Objects.equals(loginCommentAutor, comment.loginCommentAutor) &&
                Objects.equals(text, comment.text) &&
                Objects.equals(joinedUsers, comment.joinedUsers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loginCommentAutor, text, joinedUsers);
    }
}
