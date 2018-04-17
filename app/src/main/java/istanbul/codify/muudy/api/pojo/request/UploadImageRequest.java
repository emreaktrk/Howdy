package istanbul.codify.muudy.api.pojo.request;

import java.util.Base64;

public final class UploadImageRequest {

    public String token;
    public Base64 data;

    public UploadImageRequest(Base64 data) {
        this.data = data;
    }
}
