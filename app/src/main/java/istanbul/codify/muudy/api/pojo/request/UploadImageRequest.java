package istanbul.codify.muudy.api.pojo.request;


import istanbul.codify.muudy.model.ProfileImageType;

public final class UploadImageRequest {

    public String token;
    public String data;
    public ProfileImageType profileImageType;

    public UploadImageRequest(String data) {
        this.data = data;
    }
}
