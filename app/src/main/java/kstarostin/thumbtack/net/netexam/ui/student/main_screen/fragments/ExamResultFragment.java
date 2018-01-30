package kstarostin.thumbtack.net.netexam.ui.student.main_screen.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import kstarostin.thumbtack.net.netexam.R;
import kstarostin.thumbtack.net.netexam.app.App;
import kstarostin.thumbtack.net.netexam.network.models.Exam;
import kstarostin.thumbtack.net.netexam.network.models.ExamResult;
import kstarostin.thumbtack.net.netexam.network.models.PassedExam;
import kstarostin.thumbtack.net.netexam.ui.base.BaseFragment;
import kstarostin.thumbtack.net.netexam.ui.student.main_screen.adapters.RecyclerViewExamResultAdapter;

import static kstarostin.thumbtack.net.netexam.ui.student.main_screen.StudentMainScreenActivity.BUNDLE_EXAM_RESULT;
import static kstarostin.thumbtack.net.netexam.ui.student.main_screen.StudentMainScreenActivity.BUNDLE_PASSED_EXAM;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExamResultFragment extends BaseFragment {

    @BindView(R.id.recycler_protocol)
    RecyclerView recyclerProtocol;
    @BindView(R.id.teacher_fio)
    TextView teacherTV;
    @BindView(R.id.subject_name)
    TextView subjectTV;
    @BindView(R.id.result_score)
    TextView resultTV;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private ExamResult examResult;
    private PassedExam passedExam;

    public ExamResultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String passedExamJson = getArguments().getString(BUNDLE_PASSED_EXAM, null);
        if (passedExamJson != null) {
            passedExam = getGson().fromJson(passedExamJson, PassedExam.class);
        } else{
            examResult = getGson().fromJson(getArguments().getString(BUNDLE_EXAM_RESULT),
                    ExamResult.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exam_result, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle data) {
        super.onViewCreated(view, data);
        setToolbar();
        setRecyclerProtocol();
        setAdditionalExamInfo();
    }

    private void setToolbar(){
        getBaseActivity().setSupportActionBar(toolbar);
        getBaseActivity().setToolbarTitle(getString(R.string.results));
    }

    private void setRecyclerProtocol(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerProtocol.setLayoutManager(linearLayoutManager);
        RecyclerViewExamResultAdapter adapter;
        if (examResult == null){
            adapter = new RecyclerViewExamResultAdapter(transformResults(passedExam.getResults()));
        } else {
            adapter = new RecyclerViewExamResultAdapter(transformResults(examResult.getResults()));
        }
        recyclerProtocol.setAdapter(adapter);
    }

    private void setAdditionalExamInfo(){
        if (examResult == null) {
            teacherTV.setText(passedExam.getTeacher().getShortFIO());
            subjectTV.setText(passedExam.getName());
            resultTV.setText(passedExam.getScore(getContext()));
        } else {
            Exam exam = getPreferences().getExam();
            String teacher = exam.getLastName() + ' ' +
                    exam.getFirstName().charAt(0) + ". " +
                    (exam.getPatronymic()!=null ? exam.getPatronymic().charAt(0) + "." : "");
            teacherTV.setText(teacher);
            subjectTV.setText(exam.getName());
            resultTV.setText(examResult.getScore());
        }
    }

    private List<String> transformResults(List<String> results){
        for (int i=0; i<results.size(); i++){
            String res = results.get(i);
            results.set(i, res.equals("YES") ? getString(R.string.yes)
                    : res.equals("NO") ? getString(R.string.no) : getString(R.string.skipped));
        }
        return results;
    }
}
