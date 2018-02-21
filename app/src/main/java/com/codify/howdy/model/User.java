package com.codify.howdy.model;

import com.google.gson.Gson;

import java.io.Serializable;

public final class User implements Serializable {

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

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
