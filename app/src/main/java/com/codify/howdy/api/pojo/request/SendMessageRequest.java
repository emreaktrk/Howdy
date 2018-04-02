package com.codify.howdy.api.pojo.request;


import java.util.Base64;

public final class SendMessageRequest {

    public String token;
    public long toUserId;
    public String text;
    public Base64 data;

}
