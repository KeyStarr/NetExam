package kstarostin.thumbtack.net.netexam.network.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExamResult {

    @SerializedName("results")
    @Expose
    private List<String> results = null;
    //TODO: CHANGE TO ENUMMMM

    public List<String> getResults() {
        return results;
    }

    public String getScore(){
        int correct =0, mistakes = 0;
        for (String result : results){
            if ("YES".equals(result)){
                correct++;
            }
        }
        return String.valueOf(correct) + '/' + results.size();
    }

    public void setResults(List<String> results) {
        this.results = results;
    }

}