package istanbul.codify.muudy.api.pojo.request;


public final class ForgotPasswordRequest {

    public CharSequence email;

    public ForgotPasswordRequest(String email) {
        this.email =email;
    }
}
