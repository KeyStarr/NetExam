package kstarostin.thumbtack.net.netexam.ui.entry;

import java.util.regex.Pattern;

import kstarostin.thumbtack.net.netexam.R;

/**
 * Created by Cyril on 04.03.2017.
 */

public class Validator {
    private static final int personError=R.string.person_error;
    private static final int loginError=R.string.login_error;
    private static final int semesterError=R.string.semester_error;
    private static final int groupError=R.string.group_error;
    private static final int passwordMatchError=R.string.password_match_error;
    private static final int passwordLengthError=R.string.password_length_error;

    private static final String loginRegex="[A-Za-zа-яА-Я0-9]+";
    private static final String personRegex="[а-яА-Я -]+";
    private static final String semesterRegex="[1-9]+";
    private static final String groupRegex="[а-яА-Я0-9-]+";

    private boolean isEverythingCorrect = true;

    private Integer getError(String regex, int error, String text){
        if (!Pattern.matches(regex, text)){
            isEverythingCorrect = false;
            return error;
        }
        return null;
    }

    public Integer getErrorLogin(String text){
        return getError(loginRegex, loginError, text);
    }

    public Integer getErrorPassword(String text){
        //For authorization
        if (text.length()<8){
            isEverythingCorrect = false;
            return passwordLengthError;
        }
        return null;
    }

    public Integer getErrorPassword(String p1, String p2){
        //For registration
        if (getErrorPassword(p1)!=null){
            return passwordLengthError;
        }
        else if (!p1.equals(p2)) {
            isEverythingCorrect = false;
            return passwordMatchError;
        }
        return null;
    }

    public Integer getErrorFirstName(String text){
        return getError(personRegex,personError,text);
    }

    public Integer getErrorLastName(String text){
        return getError(personRegex,personError,text);
    }

    public Integer getErrorPatronymic(String text){
        return getError(personRegex, personError, text);
    }

    public Integer getErrorGroup(String text){
        return getError(groupRegex, groupError, text);
    }

    public Integer getErrorSemester(String text){
        return getError(semesterRegex, semesterError, text);
    }

    public Integer getErrorPosition(String text){
        return getError(personRegex,personError,text);
    }

    public Integer getErrorDepartment(String text){
        return getError(personRegex, personError, text);
    }

    public boolean wasEverythingCorrect() {
        boolean buff = isEverythingCorrect;
        isEverythingCorrect = true;
        return buff;
    }
}
