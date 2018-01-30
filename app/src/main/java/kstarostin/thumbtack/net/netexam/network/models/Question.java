package kstarostin.thumbtack.net.netexam.network.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Question {

    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("number")
    @Expose
    private Integer number;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("answers")
    @Expose
    private List<String> answers = null;
    @SerializedName("correct")
    @Expose
    private Integer correct;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public Integer getCorrect() {
        return correct;
    }

    public void setCorrect(Integer correct) {
        this.correct = correct;
    }

}