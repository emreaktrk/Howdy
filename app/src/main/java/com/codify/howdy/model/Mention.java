package com.codify.howdy.model;

public final class Mention {

    public User mUser;
    public boolean isAdded;

    public Mention(User user, boolean added) {
        mUser = user;
        isAdded = added;
    }
}
