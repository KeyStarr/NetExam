package kstarostin.thumbtack.net.netexam.ui.student.exam;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import kstarostin.thumbtack.net.netexam.R;
import kstarostin.thumbtack.net.netexam.network.models.QuestionList;
import kstarostin.thumbtack.net.netexam.ui.MainActivity;
import kstarostin.thumbtack.net.netexam.ui.base.BaseActivity;
import kstarostin.thumbtack.net.netexam.ui.base.RecyclerClickListener;
import kstarostin.thumbtack.net.netexam.ui.student.exam.adapters.QuestionPageAdapter;
import kstarostin.thumbtack.net.netexam.ui.student.exam.adapters.RecyclerViewStudentQuestionsAdapter;
import kstarostin.thumbtack.net.netexam.ui.utils.TimeBoardFormatUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentExamActivity extends BaseActivity {

    public static final int RESULT_CODE_EXAM_FINISH = 132;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.timer_container)
    RelativeLayout timerContainer;
    @BindView(R.id.timer_text_view)
    TextView timerView;
    @BindView(R.id.recycler_view_questions)
    RecyclerView recycler;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.question_view_pager)
    ViewPager questionViewPager;

    private Unbinder unbinder;
    private QuestionList questionList;
    private RecyclerViewStudentQuestionsAdapter recyclerAdapter;
    private QuestionPageAdapter pagerAdapter;
    private CountDownTimer timer;
    private int goodFinish = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_exam);
        unbinder= ButterKnife.bind(this);

        setToolbar();
        setNavigationDrawer();
        setQuestions();
    }

    @Override
    public void onResume(){
        super.onResume();
        resetTimer();
    }

    @Override
    public void onStart(){
        super.onStart();
        resetTimer();
    }

    private void setToolbar(){
        toolbar.setTitle("");
        View finishExam = toolbar.findViewById(R.id.ic_finish_exam);
        finishExam.setVisibility(View.VISIBLE);
        finishExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionViewPager.setCurrentItem(questionList.getQuestions().size());
            }
        });
        setSupportActionBar(toolbar);
    }


    private void setNavigationDrawer(){
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerOpened(drawerView);
                updateAnsweredQuestionIcons();
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void updateAnsweredQuestionIcons(){
        List <Integer> answers = pagerAdapter.getExamAnswers().getAnswers();
        for(int i=0 ; i<answers.size(); i++){
            updateAnsweredQuestionIcon(i, answers.get(i) > 0);
        }
    }

    private void updateAnsweredQuestionIcon(int position, boolean isDone){
        View view = recycler.getChildAt(position);
        if (view!=null)
            view.findViewById(R.id.ic_question_done).
                    setVisibility(isDone ? View.VISIBLE : View.GONE);
    }

    private void setQuestions(){
        questionList = getPreferences().getExamQuestionList();
        if (questionList != null) {
            setRecyclerView();
            setQuestionViewPager();
        } else{
            getQuestionsByRequest();
        }

    }

    private void getQuestionsByRequest(){
        final Call<QuestionList> questionListCall = getAPI().studentGetExamQuestions(
                getPreferences().getExam().getId(), getPreferences().getToken());

        questionListCall.enqueue(new Callback<QuestionList>() {
            @Override
            public void onResponse(Call<QuestionList> call, Response<QuestionList> response) {
                if (response.isSuccessful()) {
                    questionList = response.body();
                    getPreferences().setExamQuestionList(questionList);
                    setRecyclerView();
                    setQuestionViewPager();
                } else {
                    //TODO: handle errors
                }
            }

            @Override
            public void onFailure(Call<QuestionList> call, Throwable t) {
                //TODO: handle errors
            }
        });
    }

    private void setTimer(){
        long timerMilliSeconds = getPreferences().getExamEndTime() - System.currentTimeMillis();
        startTimer(timerMilliSeconds);
        timerContainer.setVisibility(View.VISIBLE);
    }

    private void startTimer(long timeInMilliSeconds){
        timer = new CountDownTimer(timeInMilliSeconds, 1000){
            @Override
            public void onTick(long time) {
                timerView.setText(TimeBoardFormatUtils.formatToTimeBoard(time));
            }
            @Override
            public void onFinish() {
                exitExamOnFinishing();
            }
        };
        timer.start();
    }

    private void setRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(linearLayoutManager);
        recyclerAdapter = new RecyclerViewStudentQuestionsAdapter(
                questionList.getQuestions().size(), this);
        setRecyclerAdapterOnClick();
        recycler.setAdapter(recyclerAdapter);
    }

    private void setRecyclerAdapterOnClick(){
        recyclerAdapter.setOnItemClickListener(new RecyclerClickListener(){
            @Override
            public void onItemClick(int position, View v) {
                drawer.closeDrawer(GravityCompat.START);
                questionViewPager.setCurrentItem(position);
            }
        });
    }

    private void setQuestionViewPager(){
        pagerAdapter = new QuestionPageAdapter(questionList,
                getPreferences().getExamAnswers(), this);
        questionViewPager.setAdapter(pagerAdapter);
        questionViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position<questionList.getQuestions().size()) {
                    questionViewPager.setCurrentItem(position);
                    recyclerAdapter.performOnClick(position);
                } else{
                    pagerAdapter.updateQuestionsSummary();
                }
            }
        });
        setFinishExamOnClick();
    }

    private void setFinishExamOnClick(){
        pagerAdapter.setFinishExamOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        exitExamOnFinishing();
                    }
                });
    }

    private void exitExamOnFinishing(){
        goodFinish = 1;
        getPreferences().setExamFinishing(true);
        setResult(RESULT_CODE_EXAM_FINISH);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void resetTimer(){
        if (timer != null)
            timer.cancel();
        setTimer();
    }

    @Override
    public void onStop(){
        getPreferences().setExamAnswers(pagerAdapter.getExamAnswers());
        examNotify();
        if (timer != null)
            timer.cancel();
        super.onStop();
    }

    private void examNotify(){
        if (goodFinish == 0){
            Intent intent = new Intent(this, MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            Notification.Builder builder = new Notification.Builder(this);
            builder.setContentIntent(contentIntent)
                    .setSmallIcon(R.drawable.ic_time)
                    .setContentTitle("АЛЛО")
                    .setContentText("Ну это, у тебя экзамен там идёт. Но ты не волнуйся, не спеши...");
            Notification notification = builder.build();
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
            notificationManagerCompat.notify(1, notification);

        }
    }
    @Override
    public void onDestroy(){
        unbinder.unbind();
        examNotify();
        super.onDestroy();
    }
}