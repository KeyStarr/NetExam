package kstarostin.thumbtack.net.netexam.ui.entry.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.BindView;
import kstarostin.thumbtack.net.netexam.R;
import kstarostin.thumbtack.net.netexam.network.models.UserInfo;
import kstarostin.thumbtack.net.netexam.ui.base.BaseFragment;
import kstarostin.thumbtack.net.netexam.ui.entry.Validator;

import static kstarostin.thumbtack.net.netexam.ui.entry.fragments.RegistrationStudentFragment.SAVED_STATE_FAMILY_NAME;
import static kstarostin.thumbtack.net.netexam.ui.entry.fragments.RegistrationStudentFragment.SAVED_STATE_LOGIN;
import static kstarostin.thumbtack.net.netexam.ui.entry.fragments.RegistrationStudentFragment.SAVED_STATE_NAME;
import static kstarostin.thumbtack.net.netexam.ui.entry.fragments.RegistrationStudentFragment.SAVED_STATE_PASSWORD;
import static kstarostin.thumbtack.net.netexam.ui.entry.fragments.RegistrationStudentFragment.SAVED_STATE_PATRONYMIC;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationProfessorFragment extends BaseFragment {


    public static final String SAVED_STATE_POSITION= "position";
    public static final String SAVED_STATE_DEPARTMENT= "department";

    @BindView(R.id.pname)
    EditText firstName;
    @BindView(R.id.pfamily)
    EditText lastName;
    @BindView(R.id.ppatronymic)
    EditText patronymic;
    @BindView(R.id.department)
    EditText department;
    @BindView(R.id.position)
    EditText position;
    @BindView(R.id.plogin)
    EditText login;
    @BindView(R.id.ppassword1)
    EditText password1;
    @BindView(R.id.ppassword2)
    EditText password2;

    private UserInfo userInfo;
    private Validator validator;

    public RegistrationProfessorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.professor_registration, container, false);
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
            department.setText(savedState.getString(SAVED_STATE_DEPARTMENT));
            position.setText(savedState.getString(SAVED_STATE_POSITION));
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
        outState.putString(SAVED_STATE_DEPARTMENT,department.getText().toString());
        outState.putString(SAVED_STATE_POSITION,position.getText().toString());
        outState.putString(SAVED_STATE_LOGIN,login.getText().toString());
        outState.putString(SAVED_STATE_PASSWORD,password1.getText().toString());
    }

    public UserInfo getProfessorInfo(){
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
        department.setError(getStringOrNull(
                validator.getErrorDepartment(department.getText().toString())));
        position.setError(getStringOrNull(
                validator.getErrorPosition(position.getText().toString())));
    }

    private void obtainUserInfo(){
        userInfo.setFirstName(firstName.getText().toString());
        userInfo.setLastName(lastName.getText().toString());
        userInfo.setPatronymic(patronymic.getText().toString());
        userInfo.setDepartment(department.getText().toString());
        userInfo.setPosition(position.getText().toString());
        userInfo.setLogin(login.getText().toString());
        userInfo.setPassword(password1.getText().toString());
    }
}
