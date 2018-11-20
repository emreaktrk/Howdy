package istanbul.codify.monju.api.pojo.request;


public final class DeletePostRequest {

    public String token;
    public long postid;

    public DeletePostRequest() {
    }

    public DeletePostRequest(String token) {
        this.token = token;
    }
}
