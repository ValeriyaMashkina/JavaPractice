package net.thumbtack.school.concert.model;

import net.thumbtack.school.concert.errors.ServerException;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
    private static final long serialVersionUID = 803745049486954915L;
    private String surname;
    private String name;
    private String login;
    private String password;

    public User(String surname, String name, String login, String password) {
        setSurname(surname);
        setName(name);
        setLogin(login);
        setPassword(password);
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSurname() {
        return this.surname;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setLogin(String login) {
        this.login = login;
    }


    public String getLogin() {
        return this.login;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() // пароли будут в чистом виде храниться в БД???
    {
        return this.password;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(surname, user.surname) &&
                Objects.equals(name, user.name) &&
                Objects.equals(login, user.login) &&
                Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(surname, name, login, password);
    }
}
