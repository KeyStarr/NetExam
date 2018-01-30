package kstarostin.thumbtack.net.netexam.ui.entry.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import kstarostin.thumbtack.net.netexam.R;
import kstarostin.thumbtack.net.netexam.app.App;
import kstarostin.thumbtack.net.netexam.network.models.UserInfo;
import kstarostin.thumbtack.net.netexam.ui.base.BaseFragment;
import kstarostin.thumbtack.net.netexam.ui.entry.Validator;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationStudentFragment extends BaseFragment {

    public static final String SAVED_STATE_LOGIN = "login";
    public static final String SAVED_STATE_PASSWORD= "password";
    public static final String SAVED_STATE_SEMESTER= "semester";
    public static final String SAVED_STATE_GROUP= "group";
    public static final String SAVED_STATE_NAME= "name";
    public static final String SAVED_STATE_FAMILY_NAME= "family_name";
    public static final String SAVED_STATE_PATRONYMIC= "patronymic";

    @BindView(R.id.sname)
    EditText firstName;
    @BindView(R.id.sfamily)
    EditText lastName;
    @BindView(R.id.spatronymic)
    EditText patronymic;
    @BindView(R.id.group)
    EditText group;
    @BindView(R.id.semester)
    EditText semester;
    @BindView(R.id.slogin)
    EditText login;
    @BindView(R.id.spassword1)
    EditText password1;
    @BindView(R.id.spassword2)
    EditText password2;

    private UserInfo userInfo;
    private Validator validator;

    public RegistrationStudentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.student_registration, container, false);
        userInfo=new UserInfo();
        validator = new Validator();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedState){
        if (savedState!=null){
            firstName.setText(savedState.getString(SAVED_STATE_NAME));
            lastName.setText(savedState.getString(SAVED_STATE_FAMILY_NAME));
            patronymic.setText(savedState.getString(SAVED_STATE_PATRONYMIC));
            group.setText(savedState.getString(SAVED_STATE_GROUP));
            semester.setText(savedState.getString(SAVED_STATE_SEMESTER));
            login.setText(savedState.getString(SAVED_STATE_LOGIN));
            password1.setText(savedState.getString(SAVED_STATE_PASSWORD));
        }
        super.onViewCreated(view,savedState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        //Not needed but made and left for studying purposes
        outState.putString(SAVED_STATE_NAME, firstName.getText().toString());
        outState.putString(SAVED_STATE_FAMILY_NAME,lastName.getText().toString());
        outState.putString(SAVED_STATE_PATRONYMIC,patronymic.getText().toString());
        outState.putString(SAVED_STATE_GROUP,group.getText().toString());
        outState.putString(SAVED_STATE_SEMESTER,semester.getText().toString());
        outState.putString(SAVED_STATE_LOGIN,login.getText().toString());
        outState.putString(SAVED_STATE_PASSWORD,password1.getText().toString());
    }

    public UserInfo getStudentInfo(){
        validateInfoAndSetErrors();
        if (validator.wasEverythingCorrect()) {
            obtainUserInfo();
            return userInfo;
        } else{
            return null;
        }
    }

    private void validateInfoAndSetErrors(){
        login.setError(getStringOrNull(
                validator.getErrorLogin(login.getText().toString())));
        password1.setError(getStringOrNull(validator.getErrorPassword(password1.getText().toString(),
                password2.getText().toString())));
        password2.setError(getStringOrNull(validator.getErrorPassword(password1.getText().toString(),
                password2.getText().toString())));
        firstName.setError(getStringOrNull(
                validator.getErrorFirstName(firstName.getText().toString())));
        lastName.setError(getStringOrNull(
                validator.getErrorLastName(lastName.getText().toString())));
        patronymic.setError(getStringOrNull(
                validator.getErrorPatronymic(patronymic.getText().toString())));
        semester.setError(getStringOrNull(
                validator.getErrorSemester(semester.getText().toString())));
        group.setError(getStringOrNull(
                validator.getErrorGroup(group.getText().toString())));
    }

    private void obtainUserInfo(){
        userInfo.setFirstName(firstName.getText().toString());
        userInfo.setLastName(lastName.getText().toString());
        userInfo.setPatronymic(patronymic.getText().toString());
        userInfo.setGroup(group.getText().toString());
        try {
            userInfo.setSemester(Integer.parseInt(semester.getText().toString()));
        } catch (NumberFormatException ex){
            userInfo.setSemester(null);
        }
        userInfo.setLogin(login.getText().toString());
        userInfo.setPassword(password1.getText().toString());
    }
}
