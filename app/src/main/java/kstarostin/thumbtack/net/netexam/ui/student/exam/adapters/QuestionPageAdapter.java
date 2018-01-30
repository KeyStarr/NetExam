package kstarostin.thumbtack.net.netexam.ui.student.exam.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import kstarostin.thumbtack.net.netexam.R;
import kstarostin.thumbtack.net.netexam.network.models.ExamAnswers;
import kstarostin.thumbtack.net.netexam.network.models.Question;
import kstarostin.thumbtack.net.netexam.network.models.QuestionList;
import kstarostin.thumbtack.net.netexam.ui.student.exam.ToggleableRadioButton;

/**
 * Created by Cyril on 18.04.2017.
 */

public class QuestionPageAdapter extends PagerAdapter {

    private ViewGroup container;
    private LayoutInflater inflater;
    private QuestionList questionsList;
    private RadioGroup answersGroup;
    private ExamAnswers answers;
    private View.OnClickListener finishExamListener;
    private Context ctx;

    public QuestionPageAdapter(QuestionList questionsList, ExamAnswers answers,
                               AppCompatActivity ctx) {
        this.questionsList = questionsList;
        this.answers = answers != null ? answers : createAnswers();
        this.ctx = ctx;
        inflater = LayoutInflater.from(ctx);
    }

    private ExamAnswers createAnswers(){
        ExamAnswers answers = new ExamAnswers();
        answers.setAnswers(new ArrayList<Integer>());
        while (answers.getAnswers().size() < questionsList.getQuestions().size()) {
            answers.getAnswers().add(0);
        }
        return answers;
    }

    @Override
    public int getCount() {
        return questionsList.getQuestions().size()+1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        this.container = container;
        ViewGroup currentView = position < questionsList.getQuestions().size()
                ? setQuestion(container,position)
                : setEndExamLayout(container, position);
        container.addView(currentView);
        return currentView;
    }

    private ViewGroup setQuestion(ViewGroup container, int position){
        int rootLayout = R.layout.student_question;
        ViewGroup currentView = (ViewGroup) inflater.inflate(rootLayout, container, false);
        setQuestionText(currentView, position);
        setAnswersGroup(currentView, position);
        return currentView;
    }

    private ViewGroup setEndExamLayout(ViewGroup container, int position){
        int rootLayout = R.layout.student_exam_end;
        ViewGroup currentView = (ViewGroup) inflater.inflate(rootLayout, container, false);
        TextView questionSummary = (TextView) currentView.findViewById(R.id.question_summary);
        questionSummary.setText(getQuestionsSummary());
        Button finishExam = (Button) currentView.findViewById(R.id.end_exam_button);
        finishExam.setOnClickListener(finishExamListener);
       return currentView;
    }

    private void setQuestionText(View currentView, int position){
        TextView questionText = (TextView) currentView.findViewById(R.id.question_text);
        questionText.setText(questionsList.getQuestions().get(position).getQuestion());
    }

    private void setAnswersGroup(View currentView, int position){
        answersGroup = ((RadioGroup) currentView.findViewById(R.id.answers_container));
        Question question = questionsList.getQuestions().get(position);
        for (int i = 0; i < question.getAnswers().size(); i++) {
            ToggleableRadioButton b = new ToggleableRadioButton(answersGroup.getContext(), null, R.attr.answerButtonStyle);
            b.setText(question.getAnswers().get(i));
            b.setId(i + 1);
            answersGroup.addView(b);
        }
        setAnswerGroupOnCheck(position);
        if (answers.getAnswers().get(position) != 0) {
            answersGroup.check(answers.getAnswers().get(position));
        }
    }

    private void setAnswerGroupOnCheck(final int questionNum) {
        answersGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId > 0) {
                    if (answers.getAnswers().get(questionNum) == checkedId){
                        answers.getAnswers().set(questionNum, 0);
                    }
                    else {
                        answers.getAnswers().set(questionNum, checkedId);
                    }
                }
            }
        });
    }

    public void updateQuestionsSummary(){
        TextView questionSummary = (TextView) container.findViewById(R.id.question_summary);
        questionSummary.setText(getQuestionsSummary());
    }

    private String getQuestionsSummary(){
        int answered = 0;
        for (int answer : answers.getAnswers())
            if (answer!=0)
                answered++;
        return ctx.getString(R.string.end_exam_questions)
                + answered + "/" + questionsList.getQuestions().size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void setFinishExamOnClickListener (View.OnClickListener listener){
        finishExamListener = listener;
    }

    public ExamAnswers getExamAnswers(){
        return answers;
    }
}
