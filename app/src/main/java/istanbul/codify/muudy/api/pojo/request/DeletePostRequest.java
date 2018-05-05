package istanbul.codify.muudy.api.pojo.request;


public final class DeletePostRequest {

    public String token;
    public long postid;

    public DeletePostRequest() {
    }

    public DeletePostRequest(String token) {
        this.token = token;
    }
}
