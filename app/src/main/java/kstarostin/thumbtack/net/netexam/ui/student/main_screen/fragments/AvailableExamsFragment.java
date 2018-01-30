package kstarostin.thumbtack.net.netexam.ui.student.main_screen.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import kstarostin.thumbtack.net.netexam.R;
import kstarostin.thumbtack.net.netexam.events.ExamFinishCancelEvent;
import kstarostin.thumbtack.net.netexam.events.ExamFinishOkEvent;
import kstarostin.thumbtack.net.netexam.events.ExamStartEvent;
import kstarostin.thumbtack.net.netexam.events.RefreshExamsClickedEvent;
import kstarostin.thumbtack.net.netexam.ui.base.RecyclerClickListener;
import kstarostin.thumbtack.net.netexam.ui.student.main_screen.adapters.RecyclerViewAvailableExamsAdapter;
import kstarostin.thumbtack.net.netexam.events.SnackbarDismissEvent;
import kstarostin.thumbtack.net.netexam.network.models.Exam;
import kstarostin.thumbtack.net.netexam.network.models.ExamsList;
import kstarostin.thumbtack.net.netexam.ui.base.BaseFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kstarostin.thumbtack.net.netexam.ui.utils.NetErrorsUtils.getErrorMessage;

/**
 * A simple {@link Fragment} subclass.
 */
public class AvailableExamsFragment extends BaseFragment {

    private static final int EXAM_EXPIRED_SNACKBAR_DURATION = 4500;
    private static final int EXAM_FINISHING_SNACKBAR_DURATION = 4500;
    public static final int EXAM_GETTING_RESULT_SNACKBAR_DURATION = 10000;
    private static final int EXAM_INFO_SNACKBAR_DURATION = 4500;

    @BindView(R.id.recycler_view_exams_list)
    RecyclerView recycler;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.available_card)
    CardView examsCardView;
    @BindView(R.id.error_container)
    RelativeLayout errorContainer;
    @BindView(R.id.error_message_textview)
    TextView errorMessage;

    private LayoutInflater mInflater;
    private Snackbar snackbarExamInfo;
    private Snackbar snackbarFinishing;
    private Snackbar snackbarGettingResult;
    private Snackbar snackbarExamExpired;

    public AvailableExamsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mInflater = inflater;
        getBaseActivity().setToolbarTitle(getString(R.string.available_exams));
        return inflater.inflate(R.layout.fragment_available_exams, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle data) {
        super.onViewCreated(view, data);
        setRecycler();
        setExamsList();
        makeSnackbars(view);
        if (getPreferences().isExamFinishing()){
            getPreferences().setExamFinishing(false);
            if (getPreferences().getExamEndTime() - System.currentTimeMillis()>-5) {
                snackbarFinishing.show();
            } else {
                snackbarExamExpired.show();
            }
        }
    }

    private void setRecycler(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(linearLayoutManager);
    }

    private void makeSnackbars(View v){
        makeSnackbarGettingResult(v);
        makeSnackbarFinishing(v);
        makeSnackbarExamExpired(v);
    }

    private void makeSnackbarGettingResult(View v){
        snackbarGettingResult = Snackbar.make(v, R.string.getting_results,
                EXAM_GETTING_RESULT_SNACKBAR_DURATION);
    }

    private void makeSnackbarFinishing(final View v){
        snackbarFinishing = Snackbar.make(v, R.string.exam_finishing,
                EXAM_FINISHING_SNACKBAR_DURATION);
        snackbarFinishing.setAction(R.string.cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBus().post(new ExamFinishCancelEvent());
            }
        });
        snackbarFinishing.addCallback(new Snackbar.Callback(){
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (event != 1){
                    //'1' stands for click on snack's action button
                    if (event == 2)
                        snackbarGettingResult.show();
                    getResults(v, event == 2);
                }
            }
        });
    }

    private void makeSnackbarExamExpired(View v){
        snackbarExamExpired = Snackbar.make(v,
                R.string.exam_expiration,
                EXAM_EXPIRED_SNACKBAR_DURATION);
        snackbarExamExpired.setAction(R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Subscribe
    public void dismissSnackbar(SnackbarDismissEvent event) {
        if (snackbarGettingResult.isShown())
            snackbarGettingResult.dismiss();
        else if (snackbarFinishing.isShown())
            snackbarFinishing.dismiss();
        else if (snackbarExamExpired.isShown())
            snackbarExamExpired.dismiss();
        else if (snackbarExamInfo != null)
            if (snackbarExamInfo.isShown())
                snackbarExamInfo.dismiss();
    }

    private void setExamsList() {
        if (!getPreferences().isUpdateAvailableExams()) {
            showExamsWithoutUpdate();
        } else {
            showExamsWithUpdate();
        }
    }

    private void showExamsWithoutUpdate() {
        setRecyclerAdapter(getPreferences().getAvailableExams());
        progressBar.setVisibility(View.GONE);
        examsCardView.setVisibility(View.VISIBLE);
    }

    private void showExamsWithUpdate() {
        String token = getPreferences().getToken();
        final Call<ExamsList> getExams = getAPI().studentGetExams(token);
        getExams.enqueue(new Callback<ExamsList>() {
            @Override
            public void onResponse(Call<ExamsList> call, Response<ExamsList> response) {
                if (response.isSuccessful()) {
                    if (response.body().getExams().size() > 0) {
                        getPreferences().setAvailableExams(response.body());
                        getPreferences().setUpdateAvailableExams(false);
                        setRecyclerAdapter(response.body());
                        examsCardView.setVisibility(View.VISIBLE);
                    } else {
                        errorMessage.setText(R.string.no_available_exams);
                        errorContainer.setVisibility(View.VISIBLE);
                    }
                    progressBar.setVisibility(View.GONE);
                } else {
                    try {
                        errorMessage.setText(getString(getErrorMessage(
                                response.errorBody().string())));
                    } catch (IOException ex) {
                        //TODO: handle this
                    }
                    errorContainer.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    //if wrong token - request new one
                    //if login & password are incorrect, go to entry screen
                }
            }

            @Override
            public void onFailure(Call<ExamsList> call, Throwable t) {
                //TODO: handle errors
                errorMessage.setText(getString(getErrorMessage(t.getClass())));
                errorContainer.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void setRecyclerAdapter(ExamsList examsList){
        final RecyclerViewAvailableExamsAdapter adapter =
                new RecyclerViewAvailableExamsAdapter(examsList);
        recycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecyclerClickListener() {

            @Override
            public void onItemClick(int position, View v) {
                makeAndShowSnackbarExamInfo(position,v, adapter);
            }
        });
    }

    @Subscribe
    public void refreshExamsToolbarClicked(RefreshExamsClickedEvent event) {
        refreshExams();
    }

    @OnClick(R.id.retry_receive_exams_button)
    public void refreshExamsButtonClicked() {
        refreshExams();
    }

    private void refreshExams() {
        getPreferences().setUpdateAvailableExams(true);
        errorContainer.setVisibility(View.GONE);
        examsCardView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        setExamsList();
    }

    private void getResults(View v, boolean showResult){
        //'4' stands for opening another snackbar
        getBus().post(new ExamFinishOkEvent(showResult));
    }

    private void makeAndShowSnackbarExamInfo(int position, View v,
                                             final RecyclerViewAvailableExamsAdapter mAdapter) {
        snackbarExamInfo = Snackbar.make(v, "", EXAM_INFO_SNACKBAR_DURATION);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbarExamInfo.getView();
        View snackView = mInflater.inflate(R.layout.available_exams_snackbar, null);
        layout.addView(snackView, 0);

        TextView snackTV = (TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
        TextView nameTV = (TextView) layout.findViewById(R.id.exam_name);
        TextView teacherTV = (TextView) layout.findViewById(R.id.exam_teacher);
        TextView questionsTV = (TextView) layout.findViewById(R.id.exam_questions);
        TextView timeTV = (TextView) layout.findViewById(R.id.exam_time);
        Button startExamButton = (Button) layout.findViewById(R.id.button_exam_start);

        final Exam exam = mAdapter.getItem(position);
        snackTV.setVisibility(View.INVISIBLE);
        nameTV.setText(exam.getName());
        String teacher = exam.getPatronymic() + ' ' + exam.getFirstName().charAt(0) +
                ". " + exam.getLastName().charAt(0) + '.';
        teacherTV.setText(teacher);
        String questions = getString(R.string.available_exams_questions) +
                exam.getQuestionsCountPerExam();
        questionsTV.setText(questions);
        String time = getString(R.string.available_exams_time) +
                exam.getTimeInMinutes() + getString(R.string.available_exam_mins);
        timeTV.setText(time);

        //Subtracted 5 seconds from actual exam time
        //For time to send results
        final long examEndTime = System.currentTimeMillis()
                + (exam.getTimeInMinutes() * 60 - 5) * 1000;
        startExamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPreferences().setExam(exam);
                getPreferences().setExamEndTime(examEndTime);
                getBus().post(new ExamStartEvent());
            }
        });
        snackbarExamInfo.show();
    }
}