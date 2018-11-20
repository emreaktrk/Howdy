package istanbul.codify.monju.api.pojo.request;


import istanbul.codify.monju.model.ProfileImageType;

public final class UploadImageRequest {

    public String token;
    public String data;
    public ProfileImageType profileImageType;

    public UploadImageRequest(String data) {
        this.data = data;
    }
}
