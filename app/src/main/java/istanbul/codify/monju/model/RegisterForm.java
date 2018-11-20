package istanbul.codify.monju.model;

import java.io.Serializable;

public final class RegisterForm implements Serializable{

    public CharSequence mFullname;
    public Gender mGender;
    public CharSequence mBirthday;
    public Email mEmail;
    public CharSequence mUsername;
    public CharSequence mPhone;
    public CharSequence mPassword;
    public CharSequence mPasswordConfirm;
}
