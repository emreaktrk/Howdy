package com.codify.howdy.model;

public final class Credential {

    public final CharSequence mEmail;
    public final CharSequence mPassword;

    public Credential(CharSequence email, CharSequence password) {
        this.mEmail = email;
        this.mPassword = password;
    }

    @Override
    public String toString() {
        return mEmail + " : " + mPassword;
    }
}
