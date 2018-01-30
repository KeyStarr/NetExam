package kstarostin.thumbtack.net.netexam.ui.entry;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import kstarostin.thumbtack.net.netexam.R;
import kstarostin.thumbtack.net.netexam.events.RegistrationFragmentOpenEvent;
import kstarostin.thumbtack.net.netexam.ui.entry.fragments.RegistrationFragment;
import kstarostin.thumbtack.net.netexam.events.AuthorizationEvent;
import kstarostin.thumbtack.net.netexam.events.RegistrationEvent;
import kstarostin.thumbtack.net.netexam.network.models.Credentials;
import kstarostin.thumbtack.net.netexam.network.models.User;
import kstarostin.thumbtack.net.netexam.ui.base.BaseActivity;
import kstarostin.thumbtack.net.netexam.ui.entry.fragments.AuthorizationFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EntryActivity extends BaseActivity {

    public static final String BACKSTACK_REGISTRATION = "registration";
    public static final int RESULT_CODE_AUTHORIZED = 131;
    public static final int RESULT_CODE_REFRESH_TOKEN_NET_ERROR = 158;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_progress_bar)
    ProgressBar progressBar;

    private Unbinder unbinder;
    private boolean isAuthorized = false;
    private Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState=savedInstanceState;
        isAuthorized = isAuthorized();
        if (!isAuthorized) {
            setLoginScreen();
        } else {
            handleReAuthorization();
        }
    }

    private void setLoginScreen(){
        setContentView(R.layout.activity_entry);
        unbinder = ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            changeFragment(false, R.id.entry_container,
                    new AuthorizationFragment(), false, null,null);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            //If savedInstanceState!=null
            //For back arrow to appear
            //if registration fragment was opened
            onBackStackChanged();
        }
    }

    @Override
    public void onDestroy() {
        if (!isAuthorized)
            unbinder.unbind();
        super.onDestroy();
    }

    @Subscribe
    public void authorization(AuthorizationEvent event) {
        saveData(event.getUser(), event.getCredentials());
        sendResult(RESULT_CODE_AUTHORIZED);
    }

    @Subscribe
    public void registration(RegistrationEvent event) {
        saveData(event.getUser(), null);
        sendResult(RESULT_CODE_AUTHORIZED);
    }

    @Subscribe
    public void registrationOpen(RegistrationFragmentOpenEvent event) {
        changeFragment(true, R.id.entry_container, new RegistrationFragment(),
                true, BACKSTACK_REGISTRATION, null);
    }

    private void sendResult(int result) {
        setResult(result);
        this.finish();
    }

    private void saveData(User user, Credentials cred) {
        getPreferences().setUser(user);
        getPreferences().setToken(user.getToken());
        if (cred != null) {
            getPreferences().setCredentials(cred);
        }
    }

    private boolean isAuthorized() {
        //Decided to create a method for this
        //Because in production conditions for
        //Being authorized may be different
        return getPreferences().getCredentials() != null;
    }

    private void handleReAuthorization(){
        Credentials cred = getPreferences().getCredentials();
        Call<User> authorize = getAPI().authorize(cred);
        authorize.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    getPreferences().setToken(response.body().getToken());
                    sendResult(RESULT_CODE_AUTHORIZED);
                } else{
                    getPreferences().clearCredentials();
                    setLoginScreen();
                    Toast.makeText(getBaseContext(),
                            R.string.token_refresh_error, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                sendResult(RESULT_CODE_REFRESH_TOKEN_NET_ERROR);
            }
        });
    }

    public void showToolbarProgressBar(boolean show){
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
