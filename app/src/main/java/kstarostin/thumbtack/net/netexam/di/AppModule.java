package kstarostin.thumbtack.net.netexam.di;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import kstarostin.thumbtack.net.netexam.app.Preferences;
import kstarostin.thumbtack.net.netexam.app.App;
import kstarostin.thumbtack.net.netexam.network.NetExamAPI;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Cyril on 17.02.2017.
 */

@Module
public class AppModule {
    private static final String BASE_URL = "http://82.200.2.44:8888";
    private final App app;

    public AppModule (App app){ this.app=app;}

    @Provides
    @Singleton
    public Bus provideBus(){
        return new Bus();
    }

    @Provides
    @Singleton
    public NetExamAPI provideApi(){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder().serializeNulls().create()))
                .build()
                .create(NetExamAPI.class);
    }

    @Provides
    @Singleton
    public Preferences getPreferences(){
        return new Preferences(app.getApplicationContext());
    }

    @Provides
    @Singleton
    public Gson provideGson(){
        return new Gson();
    }
}
