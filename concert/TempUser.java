package net.thumbtack.school.concert;
import com.google.gson.Gson;
import net.thumbtack.school.concert.dto.RegisterUserDto;
import net.thumbtack.school.concert.errors.ServerException;
import net.thumbtack.school.concert.model.ProgrammRecord;
import net.thumbtack.school.concert.model.SessionData;
import net.thumbtack.school.concert.model.User;
import net.thumbtack.school.concert.server.Server;

import java.util.LinkedList;
import java.util.UUID;


public class TempUser {

    Gson gson = new Gson();
    private Server server;

    public void setServer(Server server) { this.server = server; }

    public String tempUserBehave (User user) throws ServerException {

        String res = server.registerUser(gson.toJson(new RegisterUserDto(user.getSurname(), user.getName(),
               user.getLogin(), user.getPassword())));

        if (res.contains("error")) { return res; }

        server.showCurrentProgramm(gson.toJson(new SessionData(UUID.randomUUID(), true, user.getLogin())));

        LinkedList<ProgrammRecord> currentProgramm = gson.fromJson
                (server.showCurrentProgramm(gson.toJson(new SessionData(UUID.randomUUID(), true, user.getLogin()))),
                        LinkedList.class);

        server.deleteUser(gson.toJson(new SessionData(UUID.randomUUID(), true, user.getLogin())));

        return Integer.toString(currentProgramm.size());
    }



}
