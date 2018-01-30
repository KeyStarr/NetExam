package kstarostin.thumbtack.net.netexam.network.models;

import android.content.Context;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import kstarostin.thumbtack.net.netexam.R;

public class PassedExam {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("teacher")
    @Expose
    private Teacher teacher;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("results")
    @Expose
    private List<String> results = null;

    public String getScore(Context ctx){
        int correct =0;
        for (String result : results){
            if (ctx.getString(R.string.yes).equals(result)){
                correct++;
            }
        }
        return String.valueOf(correct) + '/' + results.size();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getResults() {
        return results;
    }

    public void setResults(List<String> results) {
        this.results = results;
    }

}