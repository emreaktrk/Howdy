package com.codify.howdy.model;

import com.google.gson.Gson;

public final class User {

    public long iduser;
    public String username;
    public String imgpath;
    public String email;
    public String unicode;
    public String tokenstring;
    public int postcount;
    public int followercount;
    public int totallike;
    public String coverimgpath;
    public int isdmclosed;
    public String bio;
    public int isbanned;
    public String namesurname;
    public Gender gender;
    public String birthDate;

    @Override public String toString() {
        return new Gson().toJson(this);
    }
}
