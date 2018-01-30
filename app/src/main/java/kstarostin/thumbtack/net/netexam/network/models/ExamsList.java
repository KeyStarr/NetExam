package  kstarostin.thumbtack.net.netexam.network.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExamsList {

    @SerializedName("exams")
    @Expose
    private List<Exam> exams = null;

    public List<Exam> getExams() {
        return exams;
    }

    public void setExams(List<Exam> exams) {
        this.exams = exams;
    }
}
