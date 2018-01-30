package kstarostin.thumbtack.net.netexam.ui.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.otto.Bus;

import javax.inject.Inject;

import butterknife.ButterKnife;
import kstarostin.thumbtack.net.netexam.app.App;
import kstarostin.thumbtack.net.netexam.app.Preferences;
import kstarostin.thumbtack.net.netexam.network.NetExamAPI;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {

    @Inject
    NetExamAPI netExamAPI;
    @Inject
    Bus bus;
    @Inject
    Preferences preferences;
    @Inject
    Gson gson;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getApp(this).getAppComponent().inject(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        bus.register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        bus.unregister(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    protected void showToast(String text, int length){
        Toast.makeText(this.getContext(), text, length).show();
    }

    protected void changeFragment(int containerID, Fragment frag){
        getChildFragmentManager()
                .beginTransaction()
                .replace(containerID, frag)
                .commit();

    }
    protected Preferences getPreferences() {
        return preferences;
    }

    protected BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    protected Bus getBus() {
        return bus;
    }

    protected NetExamAPI getAPI(){
        return netExamAPI;
    }

    protected Gson getGson() { return gson; }

    protected String getStringOrNull(Integer id){
        return id != null ? getString(id) : null;
    }
}
