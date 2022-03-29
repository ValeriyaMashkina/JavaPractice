package net.thumbtack.school.concert.dao;

import net.thumbtack.school.concert.dto.LoginDto;
import net.thumbtack.school.concert.errors.ServerException;
import net.thumbtack.school.concert.model.SessionData;
import net.thumbtack.school.concert.model.User;

public interface UserDao {
    String registrateNewUser(User newUser) throws ServerException;

    String loginUser(LoginDto userData) throws ServerException;

    void deleteUser(SessionData SessionData) throws ServerException;
}
