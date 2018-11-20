package istanbul.codify.monju.api.pojo.request;

import istanbul.codify.monju.model.RegisterForm;

public final class RegisterRequest {

    public CharSequence email;
    public CharSequence namesurname;
    public CharSequence username;
    public CharSequence pass;
    public CharSequence gender;
    public CharSequence birtDate;

    public RegisterRequest(RegisterForm form) {
        this.email          = form.mEmail.mValue.toString();
        this.namesurname    = form.mFullname.toString();
        this.username       = form.mUsername.toString();
        this.pass           = form.mPassword.toString();
        this.gender         = form.mGender.name();
        this.birtDate       = form.mBirthday.toString();
    }
}
