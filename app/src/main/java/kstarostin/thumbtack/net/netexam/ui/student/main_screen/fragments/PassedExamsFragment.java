package kstarostin.thumbtack.net.netexam.ui.student.main_screen.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import kstarostin.thumbtack.net.netexam.R;
import kstarostin.thumbtack.net.netexam.events.ShowPassedExamEvent;
import kstarostin.thumbtack.net.netexam.events.RefreshExamsClickedEvent;
import kstarostin.thumbtack.net.netexam.network.models.PassedExam;
import kstarostin.thumbtack.net.netexam.ui.base.RecyclerClickListener;
import kstarostin.thumbtack.net.netexam.ui.student.main_screen.adapters.RecyclerViewPassedExamsAdapter;
import kstarostin.thumbtack.net.netexam.app.App;
import kstarostin.thumbtack.net.netexam.network.models.PassedExamsList;
import kstarostin.thumbtack.net.netexam.ui.base.BaseFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kstarostin.thumbtack.net.netexam.ui.utils.NetErrorsUtils.getErrorMessage;

/**
 * A simple {@link Fragment} subclass.
 */
public class PassedExamsFragment extends BaseFragment {

    @BindView(R.id.recycler_view_exams_result_list)
    RecyclerView recycler;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.passed_card)
    CardView examsCardView;
    @BindView(R.id.error_container)
    RelativeLayout errorContainer;
    @BindView(R.id.error_message_textview)
    TextView errorMessage;

    public PassedExamsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getBaseActivity().setToolbarTitle(getString(R.string.passed_exams));
        return inflater.inflate(R.layout.fragment_passed_exams, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle data){
        super.onViewCreated(view,data);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(linearLayoutManager);
        setPassedExamsList();
    }


    private void setPassedExamsList(){
        if (!getPreferences().isUpdatePassedExams()){
            showPassedExamsWithoutUpdate();
        } else {
            showPassedExamsWithUpdate();
        }
    }

    private void showPassedExamsWithoutUpdate(){
        PassedExamsList resultsList = getPreferences().getPassedExams();
        setRecyclerAdapter(resultsList);
        progressBar.setVisibility(View.GONE);
        examsCardView.setVisibility(View.VISIBLE);
    }

    private void showPassedExamsWithUpdate(){
        String token = getPreferences().getToken();
        Call<PassedExamsList> getPassedExams = getAPI().studentGetResults(token);
        getPassedExams.enqueue(new Callback<PassedExamsList>() {
            @Override
            public void onResponse(Call<PassedExamsList> call, Response<PassedExamsList> response) {
                if (response.isSuccessful()) {
                    if (response.body().getExams().size()>0) {
                        getPreferences().setPassedExams(response.body());
                        getPreferences().setUpdatePassedExams(false);
                        setRecyclerAdapter(response.body());
                        examsCardView.setVisibility(View.VISIBLE);
                    } else{
                        errorMessage.setText(R.string.no_passed_exams);
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
            public void onFailure(Call<PassedExamsList> call, Throwable t) {
                //TODO: handle errors
                errorMessage.setText(getString(getErrorMessage(t.getClass())));
                errorContainer.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void setRecyclerAdapter(final PassedExamsList passedList){
        final RecyclerViewPassedExamsAdapter adapter = new RecyclerViewPassedExamsAdapter(
                getContext(),passedList);
        adapter.setRecyclerClickListener(new RecyclerClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                PassedExam passedExam = passedList.getExams().get(position);
                getBus().post(new ShowPassedExamEvent(passedExam));
            }
        });
        recycler.setAdapter(adapter);
    }

    private void refreshExams(){
        getPreferences().setUpdatePassedExams(true);
        errorContainer.setVisibility(View.GONE);
        examsCardView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        setPassedExamsList();
    }

    @Subscribe
    public void refreshExamsToolbarClicked(RefreshExamsClickedEvent event){
        refreshExams();
    }

    @OnClick(R.id.retry_receive_exams_button)
    public void retryReceiveExams(){
        refreshExams();
    }
}