package kstarostin.thumbtack.net.netexam.network.models;

import android.content.Context;

import kstarostin.thumbtack.net.netexam.R;

/**
 * Created by Cyril on 02.04.2017.
 */

public enum ErrorCodes {
    LOGIN_DOESNOT_EXIST(R.string.no_such_login),
    WRONG_OR_EMPTY_LOGIN(R.string.wrong_login),
    WRONG_OR_EMPTY_PASSWORD(R.string.wrong_password),
    PASSWORD_NOT_MATCH(R.string.no_such_password),
    CONNECTION_TROUBLE(R.string.connection_error),
    EMPTY_POSITION(R.string.define_position),
    EMPTY_DEPARTMENT(R.string.define_department),
    WRONG_OR_EMPTY_FIRST_NAME(R.string.wrong_name),
    WRONG_OR_EMPTY_LAST_NAME(R.string.wrong_last_name),
    WRONG_PATRONYMIC(R.string.wrong_patronymic),
    LOGIN_ALREADY_EXISTS(R.string.login_already_exists),
    EMPTY_GROUP(R.string.define_group),
    WRONG_SEMESTER(R.string.wrong_semester),
    DATABASE_ERROR(R.string.server_error),
    UNKNOWN_ERROR(R.string.unknown_error);

    final int code;

    ErrorCodes(final int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
