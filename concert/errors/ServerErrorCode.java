package net.thumbtack.school.concert.errors;

public enum ServerErrorCode {

    LOGIN_ALREADY_EXISTS("Данный логин занят"),
    INVALID_USER_SURNAME("Вы не ввели фамилию"),
    INVALID_USER_NAME("Вы не ввели имя"),
    INVALID_USER_LOGIN("Не введен логин, либо его длина менее 5 символов"),
    INVALID_USER_PASSWORD("Не введен пароль, либо его длина менее 5 символов"),

    SONG_ALREADY_EXISTS("Данная песня уже предложена другим пользователем, " +
            "если хотите её услышать в эфире, то можете проголосовать за неё"),
    SONG_DOES_NOT_EXISTS("Вы собираетесь удалить песню, не предложенную вами"),
    USER_ALREADY_RATED("Пользователь уже оценил данную песню"),
    USER_HAS_NOT_RATED("Пользователь еще не оценил данную песню"),
    INVALID_RATING("Некорректная оценка песни - меньше 1 или больше 5"),

    INVALID_SONG_NAME("Вы не ввели название песни"),
    INVALID_SONG_COMPOSER_NAME("Вы не ввели имя композитора"),
    INVALID_LYRIC_AUTOR_NAME("Вы не ввели имя автора слов"),
    INVALID_SINGER_NAME("Вы не ввели имя исполнителя песни, либо исполнителей больше одного"),
    INVALID_SONG_DURATION("Некорреткная длительность песни (не указана, равна 0, или меньше 0)"),

    INVALID_LIST_OF_SONGS_RATINGS_OR_COMMENTS("При добавлении песни произошла ошибка при создании пустого списка " +
            "комментариев и списка оценок (1 оценка пользователя)"),



    LOGIN_DOES_NOT_EXIST("Данный логин отсутсвует в базе"),
    INCORRECT_PASSWORD("Неверный пароль"),
    INVALID_SESSION("Вы либо не вошли в личный кабинет, либо ваша сессия завершена"),

    INVALID_COMMENT_TEXT("Пустой комментарий"),
    INVALID_COMMENT_ATTEMPT("Пользователь не может оставить комментарий на предложенную им же песню"),
    INVALID_COMMENT_AUTHOR("Отсутствует имя автора комментария"),
    INVALID_JOINED_USER("Пользователь не может присоединиться к своему коммертарию или комментарию на предложенную им песню"),
    USER_HAS_NOT_JOINED_YET("Пользователь еще не присоединился к комментарию"),
    USER_HAS_ALREADY_JOINED("Пользователь уже присоединился к комментарию"),
    COMMENT_DOES_NOT_EXIST("Комментария не существует"),
    USER_ALREADY_LEFT_COMMENT("Пользователь уже комментировал эту песню"),
    SONGS_BASE_IS_EMPTY("Отсутствуют заявленные в концерт песни"),
    NO_SONGS_FOUND("Песни с указанным признаком не найдены");

    private String errorString;

    ServerErrorCode(String error) {
        this.errorString = error;
    }
}
