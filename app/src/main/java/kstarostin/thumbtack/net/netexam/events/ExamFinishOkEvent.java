package kstarostin.thumbtack.net.netexam.events;

/**
 * Created by Cyril on 02.05.2017.
 */

public class ExamFinishOkEvent {
    private boolean showResult;

    public ExamFinishOkEvent (boolean showResult){
        this.showResult = showResult;
    }

    public boolean isShowResult() {
        return showResult;
    }

    public void setShowResult(boolean showResult) {
        this.showResult = showResult;
    }
}
