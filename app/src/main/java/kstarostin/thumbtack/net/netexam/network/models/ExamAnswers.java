package kstarostin.thumbtack.net.netexam.network.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExamAnswers {

    @SerializedName("answers")
    @Expose
    private List<Integer> answers = null;

    public ExamAnswers(){}

    public ExamAnswers (List<Integer> answers) {
        this.answers=answers;
    }

    public List<Integer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Integer> answers) {
        this.answers = answers;
    }

    public ExamAnswers getForSending(){
        for (int i=0; i<answers.size(); i++){
            if (answers.get(i)==0){
                answers.set(i, null);
            } else{
                answers.set(i, answers.get(i)-1);
            }
        }
        return this;
    }
}