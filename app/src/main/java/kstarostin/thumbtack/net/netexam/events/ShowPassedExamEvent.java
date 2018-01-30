package kstarostin.thumbtack.net.netexam.events;

import kstarostin.thumbtack.net.netexam.network.models.PassedExam;

/**
 * Created by Cyril on 02.05.2017.
 */

public class ShowPassedExamEvent {
    private PassedExam passedExam;

    public ShowPassedExamEvent(PassedExam passedExam){
        this.passedExam = passedExam;
    }

    public PassedExam getPassedExam() {
        return passedExam;
    }
}
