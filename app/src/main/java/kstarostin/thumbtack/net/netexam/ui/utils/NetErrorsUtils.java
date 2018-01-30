package kstarostin.thumbtack.net.netexam.ui.utils;

import com.google.gson.Gson;

import kstarostin.thumbtack.net.netexam.network.models.Error;
import kstarostin.thumbtack.net.netexam.network.models.ErrorList;

import static kstarostin.thumbtack.net.netexam.network.models.ErrorCodes.CONNECTION_TROUBLE;
import static kstarostin.thumbtack.net.netexam.network.models.ErrorCodes.UNKNOWN_ERROR;


public class NetErrorsUtils{

    public static int getErrorMessage(String json){
        ErrorList errorList = (new Gson()).fromJson(json, ErrorList.class);
        return errorList.getErrors().get(0).getErrorCode().getCode();
    }

    public static int getErrorMessage(Class T){
        Error err = null;
        if (T == java.net.SocketTimeoutException.class){
            err = new Error(CONNECTION_TROUBLE);
        }
        //TODO: handle other exceptions.
        try {
            return err.getErrorCode().getCode();
        } catch (NullPointerException ex){
            return (new Error(UNKNOWN_ERROR)).getErrorCode().getCode();
        }
    }
}