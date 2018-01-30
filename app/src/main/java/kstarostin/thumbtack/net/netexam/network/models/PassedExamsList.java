package kstarostin.thumbtack.net.netexam.network.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PassedExamsList {

    @SerializedName("exams")
    @Expose
    private List<PassedExam> exams = null;

    public List<PassedExam> getExams() {
        return exams;
    }

    public void setExams(List<PassedExam> exams) {
        this.exams = exams;
    }

}