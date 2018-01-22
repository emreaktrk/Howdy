package com.codify.howdy.model;

public final class Credential {

    public final CharSequence mUsername;
    public final CharSequence mPassword;

    public Credential(CharSequence username, CharSequence password) {
        this.mUsername = username;
        this.mPassword = password;
    }

    @Override
    public String toString() {
        return mUsername + " : " + mPassword;
    }
}
