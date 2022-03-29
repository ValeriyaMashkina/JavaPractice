package net.thumbtack.school.concert.dto;

import net.thumbtack.school.concert.service.UserService;

import java.io.Serializable;
import java.util.Objects;

public class LoginDto implements Serializable {

    private static final long serialVersionUID = 803745049486954915L;
    private String login;
    private String password;

    public LoginDto(String login, String password) {
        setLogin(login);
        setPassword(password);
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
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
        if (!(o instanceof LoginDto)) return false;
        LoginDto loginDto = (LoginDto) o;
        return Objects.equals(login, loginDto.login) &&
                Objects.equals(password, loginDto.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password);
    }
}
