package kstarostin.thumbtack.net.netexam.ui.base;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.squareup.otto.Bus;

import javax.inject.Inject;

import kstarostin.thumbtack.net.netexam.app.App;
import kstarostin.thumbtack.net.netexam.network.NetExamAPI;
import kstarostin.thumbtack.net.netexam.app.Preferences;

public class BaseActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    @Inject
    Preferences preferences;
    @Inject
    NetExamAPI netExamAPI;
    @Inject
    Bus bus;
    @Inject
    Gson gson;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getApp(this).getAppComponent().inject(this);
        getSupportFragmentManager().addOnBackStackChangedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bus.register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        bus.unregister(this);
    }

    @Override
    public void onBackStackChanged() {
        if (getSupportActionBar() != null) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            } else {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setToolbarTitle(String title){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null) {
            actionBar.setTitle(title);
        }
    }

    protected void changeFragment(boolean replace, int containerId,Fragment frag,
                               boolean addToBackStack,@Nullable String key,
                               @Nullable Bundle args) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        if (args!=null){
            frag.setArguments(args);
        }
        if (replace) {
            transaction.replace(containerId, frag);
        } else {
            transaction.add(containerId,frag);
        }
        if (addToBackStack){
                transaction.addToBackStack(key);
        }
        transaction.commit();
    }

    protected void setResultAndFinish(int resultCode){
        setResult(resultCode);
        finish();
    }

    protected Bus getBus() {
        return bus;
    }

    protected Preferences getPreferences() {
        return preferences;
    }

    protected NetExamAPI getAPI(){
        return netExamAPI;
    }

    protected Gson getGson() { return gson; }
}
