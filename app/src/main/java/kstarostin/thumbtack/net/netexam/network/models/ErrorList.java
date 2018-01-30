package kstarostin.thumbtack.net.netexam.network.models;

/**
 * Created by Cyril on 18.02.2017.
 */

import java.util.Arrays;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ErrorList {

    @SerializedName("errors")
    @Expose
    private List<Error> errors = null;

    public ErrorList (List <Error> errors){
        this.errors=errors;
    }

    public ErrorList (Error error){
        this(Arrays.asList(error));
    }

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

}

