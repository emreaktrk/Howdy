package istanbul.codify.muudy.api.pojo.request;


public final class UploadImageRequest {

    public String token;
    public String data;

    public UploadImageRequest(String data) {
        this.data = data;
    }
}
