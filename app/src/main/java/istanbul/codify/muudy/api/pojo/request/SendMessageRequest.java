package istanbul.codify.muudy.api.pojo.request;


import android.util.Base64;

public final class SendMessageRequest {

    public String token;
    public long toUserId;
    public String text;
    public Base64 data;

}
