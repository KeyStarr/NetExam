package kstarostin.thumbtack.net.netexam.ui.entry.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

import kstarostin.thumbtack.net.netexam.R;
import kstarostin.thumbtack.net.netexam.events.RegistrationFragmentOpenEvent;
import kstarostin.thumbtack.net.netexam.ui.base.BaseFragment;
import kstarostin.thumbtack.net.netexam.ui.entry.EntryActivity;
import kstarostin.thumbtack.net.netexam.ui.entry.Validator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import kstarostin.thumbtack.net.netexam.events.AuthorizationEvent;
import kstarostin.thumbtack.net.netexam.network.models.Credentials;
import kstarostin.thumbtack.net.netexam.network.models.User;

import static kstarostin.thumbtack.net.netexam.ui.entry.fragments.RegistrationStudentFragment.SAVED_STATE_LOGIN;
import static kstarostin.thumbtack.net.netexam.ui.entry.fragments.RegistrationStudentFragment.SAVED_STATE_PASSWORD;
import static kstarostin.thumbtack.net.netexam.ui.utils.NetErrorsUtils.getErrorMessage;


/**
 * A simple {@link Fragment} subclass.
 */
public class AuthorizationFragment extends BaseFragment {

    @BindView(R.id.rememberMe)
    CheckBox rememberMe;
    @BindView(R.id.loginField)
    EditText loginInput;
    @BindView(R.id.passwordField)
    EditText passwordInput;
    @BindView(R.id.login_TIL)
    TextInputLayout loginLayout;
    @BindView(R.id.password_TIL)
    TextInputLayout passwordLayout;

    private Validator validator;

    public AuthorizationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        validator = new Validator();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getBaseActivity().setToolbarTitle(getString(R.string.authorization));
        return inflater.inflate(R.layout.fragment_authorization, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedState){
        super.onViewCreated(view,savedState);
        if (savedState!=null) {
            loginInput.setText(savedState.getString(SAVED_STATE_LOGIN));
            passwordInput.setText(savedState.getString(SAVED_STATE_PASSWORD));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        //Not needed but made and left for studying purposes
        if (loginInput!=null && passwordInput!=null) {
            outState.putString(SAVED_STATE_LOGIN, loginInput.getText().toString());
            outState.putString(SAVED_STATE_PASSWORD, passwordInput.getText().toString());
        }
        super.onSaveInstanceState(outState);
    }

    @OnClick(R.id.button_registration)
    public void register(){
        getBus().post(new RegistrationFragmentOpenEvent());
    }

    @OnClick(R.id.button_login)
    public void loginOnClick(){
        loginLayout.setError(getStringOrNull(
                validator.getErrorLogin(loginInput.getText().toString())));
        passwordLayout.setError(getStringOrNull(
                validator.getErrorPassword(passwordInput.getText().toString())));
        if (validator.wasEverythingCorrect()){
            getEntryActivity().showToolbarProgressBar(true);
            authorize();
        }
    }

    private void authorize(){
        final Credentials cred = new Credentials(loginInput.getText().toString(),
                passwordInput.getText().toString());
        Call<User> authorization = getAPI().authorize(cred);
        authorization.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                getEntryActivity().showToolbarProgressBar(false);
                if (response.isSuccessful()) {
                    User user = response.body();
                    if (rememberMe.isChecked()) {
                        getBus().post(new AuthorizationEvent(user, cred));
                    } else {
                        getBus().post(new AuthorizationEvent(user));
                    }
                } else {
                    try {
                        showToast(getString(getErrorMessage(response.errorBody().string())),
                                Toast.LENGTH_SHORT);
                    } catch (IOException ex) {
                        //TODO: IOException handling
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                getEntryActivity().showToolbarProgressBar(false);
                showToast(getString(getErrorMessage(t.getClass())), Toast.LENGTH_SHORT);
            }
        });
    }

    private EntryActivity getEntryActivity(){
        return (EntryActivity)getActivity();
    }
}
