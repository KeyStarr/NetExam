package kstarostin.thumbtack.net.netexam.events;

import kstarostin.thumbtack.net.netexam.network.models.User;

/**
 * Created by Cyril on 04.03.2017.
 */

public class RegistrationEvent {
    private User user;

    public RegistrationEvent(User user){
        this.user=user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
