package kstarostin.thumbtack.net.netexam.ui.student.main_screen;

/**
 * Created by Cyril on 11.03.2017.
 */

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationManagerCompat;
import android.view.View;

import com.squareup.otto.Subscribe;

import kstarostin.thumbtack.net.netexam.R;
import kstarostin.thumbtack.net.netexam.events.ExamFinishCancelEvent;
import kstarostin.thumbtack.net.netexam.events.ExamFinishOkEvent;
import kstarostin.thumbtack.net.netexam.events.ExamStartEvent;
import kstarostin.thumbtack.net.netexam.events.LogoutEvent;
import kstarostin.thumbtack.net.netexam.events.ShowPassedExamEvent;
import kstarostin.thumbtack.net.netexam.events.SnackbarDismissEvent;
import kstarostin.thumbtack.net.netexam.network.models.ExamResult;
import kstarostin.thumbtack.net.netexam.network.models.PassedExam;
import kstarostin.thumbtack.net.netexam.ui.MainActivity;
import kstarostin.thumbtack.net.netexam.ui.base.BaseActivity;
import kstarostin.thumbtack.net.netexam.ui.student.main_screen.fragments.ExamResultFragment;
import kstarostin.thumbtack.net.netexam.ui.student.main_screen.fragments.NavigationFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kstarostin.thumbtack.net.netexam.ui.student.main_screen.fragments.AvailableExamsFragment.EXAM_GETTING_RESULT_SNACKBAR_DURATION;

public class StudentMainScreenActivity extends BaseActivity {

    private static final String BACKSTACK_EXAM_RESULT = "back_exam_result";
    public static final String BUNDLE_EXAM_RESULT = "exam_result";
    public static final String BUNDLE_PASSED_EXAM = "exam_passed";
    public static final int RESULT_CODE_STUDENT_EXAM_START = 164;
    public static final int RESULT_CODE_STUDENT_LOGOUT= 139;
    public static final int RESULT_CODE_EXAM_FINISH_CANCEL = 133;

    private static final int ERROR_SNACKBAR_DURATION = 6500;
    private Snackbar gettingResultsSnackbar;
    private int goodFinish = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_main_activity);
        changeFragment(true,R.id.container, new NavigationFragment(), false, null, null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Subscribe
    public void logout(LogoutEvent event){
        getPreferences().clearAll();
        goodFinish = 1;
        setResultAndFinish(RESULT_CODE_STUDENT_LOGOUT);
    }


    @Subscribe
    public void examStartEvent (ExamStartEvent event){
        goodFinish = 1;
        setResultAndFinish(RESULT_CODE_STUDENT_EXAM_START);
    }

    @Subscribe
    public void examFinishCancel(ExamFinishCancelEvent event){
        goodFinish = 1;
        setResultAndFinish(RESULT_CODE_EXAM_FINISH_CANCEL);
    }

    @Subscribe
    public void showPassedExam(ShowPassedExamEvent event){
        Bundle args = new Bundle();
        args.putString(BUNDLE_PASSED_EXAM,
                getGson().toJson(event.getPassedExam(),PassedExam.class));
        changeFragment(true,R.id.container,new ExamResultFragment(),
                true,BACKSTACK_EXAM_RESULT,args);
    }

    @Subscribe
    public void examFinishOk (final ExamFinishOkEvent event){
        requestExamResults(event.isShowResult());
    }

    private void requestExamResults(final boolean showResult){
        Call<ExamResult> sendSolution = getAPI().studentSendExamSolution(
                getPreferences().getExam().getId(), getPreferences().getToken(),
                getPreferences().getExamAnswers().getForSending());
        sendSolution.enqueue(new Callback<ExamResult>() {
            @Override
            public void onResponse(Call<ExamResult> call, Response<ExamResult> response) {
                if (response.isSuccessful()) {
                    if (showResult) {
                        getBus().post(new SnackbarDismissEvent());
                        if (gettingResultsSnackbar != null) {
                            gettingResultsSnackbar.dismiss();
                        }
                        showExamResult(response.body());
                    }
                }
                getPreferences().clearExam();
                //TODO: WORK WITH SERVER ERROR RESPONSES
            }

            @Override
            public void onFailure(Call<ExamResult> call, Throwable t) {
                String errorText;
                if (t.getClass() == java.net.SocketTimeoutException.class ||
                        t.getClass() == java.net.ConnectException.class){
                    errorText = getString(R.string.error_result_check_internet);
                } else{
                    errorText = getString(R.string.unknown_error_relaunch_app);
                }
                makeAndShowErrorSnackbar(errorText, showResult);
            }
        });
    }

    private void makeAndShowErrorSnackbar(String errorText, final boolean showResult){
        View v = findViewById(R.id.main_group);
        Snackbar errorSnackbar = Snackbar.make(v,
                errorText, ERROR_SNACKBAR_DURATION);
        errorSnackbar.setAction(R.string.retry_snackbar, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeAndShowGettingResultsSnackbar(v);
                requestExamResults(showResult);
            }
        });
        errorSnackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (event != 1 && event != 4){
                    setResultAndFinish(RESULT_CODE_EXAM_FINISH_CANCEL);
                }
            }
        });
        errorSnackbar.show();
    }

    private void makeAndShowGettingResultsSnackbar(View v){
        gettingResultsSnackbar = Snackbar.make(v, R.string.getting_results,
                EXAM_GETTING_RESULT_SNACKBAR_DURATION);
        gettingResultsSnackbar.show();
    }

    private void showExamResult(ExamResult examResult){
        Bundle args = new Bundle();
        args.putString(BUNDLE_EXAM_RESULT, getGson().toJson(examResult, ExamResult.class));
        changeFragment(true, R.id.container, new ExamResultFragment(),
                true, BACKSTACK_EXAM_RESULT, args);
    }
}
