package net.thumbtack.school.concert.errors;

public class ServerException extends Exception {

    private ServerErrorCode code;

    public ServerErrorCode getErrorCode() {
        return this.code;
    }

    public ServerException(ServerErrorCode errorCode) {
        this.code = errorCode;
    }
}