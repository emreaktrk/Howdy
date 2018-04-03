package com.codify.howdy.model;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Objects;

public final class User implements Selectable, Serializable {

    public long iduser;
    public String username;
    public String imgpath1;
    public String imgpath2;
    public String imgpath3;
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
    public long id() {
        return iduser;
    }

    @Override
    public String text() {
        return "@" + username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return iduser == user.iduser;
    }

    @Override
    public int hashCode() {

        return Objects.hash(iduser);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
