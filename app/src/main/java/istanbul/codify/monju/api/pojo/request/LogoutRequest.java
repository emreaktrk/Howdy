package istanbul.codify.monju.api.pojo.request;


public final class LogoutRequest {

    public String token;

    public LogoutRequest() {
    }

    public LogoutRequest(String token) {
        this.token = token;
    }
}
