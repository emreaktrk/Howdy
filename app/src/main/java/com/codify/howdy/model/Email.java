package com.codify.howdy.model;

import android.text.Editable;
import android.text.TextUtils;

import java.util.regex.Pattern;

public final class Email {

    public CharSequence mValue;

    public Email(CharSequence value) {
        mValue = value;
    }

    public Email(Editable value) {
        mValue = value;
    }

    public boolean isValid() {
        if (TextUtils.isEmpty(mValue)) {
            return false;
        }

        String emailRegex = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+";
        return Pattern
                .compile(emailRegex)
                .matcher(mValue)
                .matches();
    }
}
