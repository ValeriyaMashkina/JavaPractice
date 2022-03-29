package net.thumbtack.school.concert.service;

import com.google.gson.Gson;
import net.thumbtack.school.concert.dao.UserDao;
import net.thumbtack.school.concert.daoImpl.UserDaoImpl;
import net.thumbtack.school.concert.dto.LoginDto;
import net.thumbtack.school.concert.dto.RegisterUserDto;
import net.thumbtack.school.concert.errors.ServerErrorCode;
import net.thumbtack.school.concert.errors.ServerException;
import net.thumbtack.school.concert.model.Checkers;
import net.thumbtack.school.concert.model.SessionData;
import net.thumbtack.school.concert.model.User;

import java.util.List;
import java.util.UUID;

public class UserService {

    public static UserDao userDao = new UserDaoImpl();


    public void setUserDao (UserDao userDao) {
        this.userDao = userDao;
    }

    public static String registerUser(RegisterUserDto userdata) throws ServerException {

        if (Checkers.checkSurname(userdata.getSurname()) && Checkers.checkName(userdata.getName()) &&
                Checkers.checkLogin(userdata.getLogin()) && Checkers.checkPassword(userdata.getPassword())) {
            return userDao.registrateNewUser(new User(userdata.getSurname(), userdata.getName(), userdata.getLogin(), userdata.getPassword()));
        } else {
            return null;
        }
    }

    public static String loginUser(LoginDto userdata) throws ServerException {

        if (Checkers.checkLogin(userdata.getLogin()) && Checkers.checkPassword(userdata.getPassword())) {
            return userDao.loginUser(userdata);
        } else {
            return null;
        }
    }


    public static String logoutUser(SessionData sessionData) throws ServerException {
        Gson gson = new Gson();
        return gson.toJson(new SessionData(null, false, null));
    }

    public static String deleteUser(SessionData sessionData) throws ServerException {
        new UserDaoImpl().deleteUser(sessionData);
        Gson gson = new Gson();
        return gson.toJson(new SessionData(null, false, null));
    }


}
