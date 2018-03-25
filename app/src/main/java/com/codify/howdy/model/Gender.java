package com.codify.howdy.model;

import java.io.Serializable;
import java.util.Locale;

public enum Gender implements Serializable {

    MALE,
    FEMALE;

    public static Gender valueOf(CharSequence value) {
        return valueOf(value);
    }

    @Override
    public String toString() {
        return (this + "").toLowerCase(Locale.ENGLISH);
    }
}
