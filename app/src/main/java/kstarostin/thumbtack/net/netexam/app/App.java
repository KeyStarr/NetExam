package kstarostin.thumbtack.net.netexam.app;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import kstarostin.thumbtack.net.netexam.di.AppComponent;
import kstarostin.thumbtack.net.netexam.di.AppModule;
import kstarostin.thumbtack.net.netexam.di.DaggerAppComponent;

/**
 * Created by Cyril on 17.02.2017.
 */

public class App extends Application{

    AppComponent appComponent;

    public static App getApp(Activity activity) {
        return (App) activity.getApplication();
    }

    public static App getApp(Fragment fragment) {
        final FragmentActivity activity = fragment.getActivity();
        if (activity != null)
            return (App) activity.getApplication();
        throw new IllegalStateException("Fragment must be attached to activity!");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .build();
    }


    public AppComponent getAppComponent() {
        return appComponent;
    }
}
