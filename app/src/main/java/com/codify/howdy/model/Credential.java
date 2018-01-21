package com.codify.howdy.model;

public final class Credential {

    public final String mUsername;
    public final String mPassword;

    public Credential(String username, String password) {
        this.mUsername = username;
        this.mPassword = password;
    }

    @Override
    public String toString() {
        return mUsername + " : " + mPassword;
    }
}
