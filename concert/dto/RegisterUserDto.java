package net.thumbtack.school.concert.dto;

import java.io.Serializable;
import java.util.Objects;

public class RegisterUserDto implements Serializable {

    private static final long serialVersionUID = 803745049486954915L;
    private String surname;
    private String name;
    private String login;
    private String password;

    public RegisterUserDto(String surname, String name, String login, String password) {
        setSurname(surname);
        setName(name);
        setLogin(login);
        setPassword(password);
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegisterUserDto)) return false;
        RegisterUserDto that = (RegisterUserDto) o;
        return Objects.equals(surname, that.surname) &&
                Objects.equals(name, that.name) &&
                Objects.equals(login, that.login) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(surname, name, login, password);
    }

}
