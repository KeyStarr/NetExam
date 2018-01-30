package kstarostin.thumbtack.net.netexam.events;

import android.support.annotation.Nullable;

import kstarostin.thumbtack.net.netexam.network.models.Credentials;
import kstarostin.thumbtack.net.netexam.network.models.User;

/**
 * Created by Cyril on 18.02.2017.
 */

public class AuthorizationEvent {
    private User user;
    private Credentials credentials;

    public AuthorizationEvent(User user, Credentials credentials){
        this.user=user;
        this.credentials=credentials;
    }

    public AuthorizationEvent(User user){
        this(user,null);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }
}
