package kstarostin.thumbtack.net.netexam.ui.entry.fragments;


import android.os.Bundle;
import android.os.DropBoxManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import kstarostin.thumbtack.net.netexam.R;
import kstarostin.thumbtack.net.netexam.app.App;
import kstarostin.thumbtack.net.netexam.events.RegistrationEvent;
import kstarostin.thumbtack.net.netexam.network.models.User;
import kstarostin.thumbtack.net.netexam.network.models.UserInfo;
import kstarostin.thumbtack.net.netexam.ui.base.BaseActivity;
import kstarostin.thumbtack.net.netexam.ui.base.BaseFragment;
import kstarostin.thumbtack.net.netexam.ui.entry.EntryActivity;
import kstarostin.thumbtack.net.netexam.ui.entry.adapters.RegistrationPageAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kstarostin.thumbtack.net.netexam.ui.utils.NetErrorsUtils.getErrorMessage;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment extends BaseFragment {

    private enum UserType{
        STUDENT,
        PROFESSOR
    }

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    public RegistrationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getBaseActivity().setToolbarTitle(getString(R.string.registration));
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        viewPager.setAdapter(new RegistrationPageAdapter(getChildFragmentManager(), getContext()));
    }

    @OnClick(R.id.button_register)
    public void confirm(){
        int item=viewPager.getCurrentItem();
        Fragment page=this.getChildFragmentManager()
                .findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + item);
        UserType userType=null;
        UserInfo userInfo=null;
        switch(item){
            case 0:
                userInfo=((RegistrationStudentFragment)page).getStudentInfo();
                userType=UserType.STUDENT;
                break;
            case 1:
                userInfo=((RegistrationProfessorFragment)page).getProfessorInfo();
                userType=UserType.PROFESSOR;
                break;
        }
        if (userInfo!=null) {
            getEntryActivity().showToolbarProgressBar(true);
            requestRegistration(userType, userInfo);
        }
    }

    private void requestRegistration(UserType type, UserInfo userInfo){
        Call <User> registration=null;
        switch(type){
            case STUDENT:
                registration=getAPI().registerStudent(userInfo);
                break;
            case PROFESSOR:
                registration=getAPI().registerProfessor(userInfo);
                break;
        }
        registration.enqueue(new Callback<User>(){
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                getEntryActivity().showToolbarProgressBar(false);
                if (response.isSuccessful()) {
                    User user = response.body();
                    getBus().post(new RegistrationEvent(user));
                } else {
                    try {
                        showToast(getString(getErrorMessage(response.errorBody().string())),
                                Toast.LENGTH_LONG);
                    } catch (IOException ex){
                        //TODO: What to do if fails
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                getEntryActivity().showToolbarProgressBar(false);
                showToast(getString(getErrorMessage(t.getClass())), Toast.LENGTH_LONG);
            }
        });
    }

    private EntryActivity getEntryActivity(){
        return (EntryActivity)getActivity();
    }
}




