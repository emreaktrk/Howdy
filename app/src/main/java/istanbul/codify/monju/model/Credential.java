package istanbul.codify.monju.model;

import java.io.Serializable;

public final class Credential implements Serializable {

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
