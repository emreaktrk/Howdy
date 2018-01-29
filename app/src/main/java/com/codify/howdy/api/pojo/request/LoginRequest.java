package com.codify.howdy.api.pojo.request;

import com.codify.howdy.model.Credential;

public final class LoginRequest {

    public CharSequence email;
    public CharSequence pass;

    public LoginRequest(String email, String pass) {
        this.email = email;
        this.pass = pass;
    }

    public LoginRequest(Credential credential) {
        this.email = credential.mEmail;
        this.pass = credential.mPassword;
    }
}
