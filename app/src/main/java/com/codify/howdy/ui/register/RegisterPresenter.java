package com.codify.howdy.ui.register;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.View;

import com.codify.howdy.R;
import com.codify.howdy.logcat.Logcat;
import com.codify.howdy.model.Email;
import com.codify.howdy.model.Gender;
import com.codify.howdy.model.RegisterForm;
import com.codify.howdy.ui.base.BasePresenter;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.Calendar;

final class RegisterPresenter extends BasePresenter<RegisterView> {

    @SuppressLint("CutPasteId")
    @Override
    public void attachView(RegisterView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.register_close))
                        .subscribe(o -> {
                            Logcat.v("Close clicked");
                            view.onCloseClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.register_birthday, TextInputEditText.class))
                        .subscribe(o -> {
                            Calendar calendar = Calendar.getInstance();
                            DatePickerDialog dialog = new DatePickerDialog(getContext(), (datePicker, year, month, day) -> {
                                TextInputEditText birthday = findViewById(R.id.register_birthday);
                                birthday.setText(
                                        new StringBuilder()
                                                .append(year)
                                                .append(".")
                                                .append(month + 1)
                                                .append(".")
                                                .append(day));
                            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                            dialog.show();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.register_register))
                        .filter(o -> {
                            TextInputEditText password = findViewById(R.id.register_password);
                            TextInputEditText confirm = findViewById(R.id.register_password_confirm);
                            return TextUtils.equals(password.getText(), confirm.getText());
                        })
                        .filter(o -> {
                            CharSequence email = findViewById(R.id.register_email, TextInputEditText.class).getText();
                            return new Email(email).isValid();
                        })
                        .subscribe(o -> {
                            RegisterForm form = new RegisterForm();
                            form.mFullname = findViewById(R.id.register_fullname, TextInputEditText.class).getText();
                            form.mGender = Gender.valueOf(findViewById(R.id.register_gender, TextInputEditText.class).getText());
                            form.mBirthday = findViewById(R.id.register_birthday, TextInputEditText.class).getText();
                            form.mEmail = new Email(findViewById(R.id.register_email, TextInputEditText.class).getText());
                            form.mUsername = findViewById(R.id.register_username, TextInputEditText.class).getText();
                            form.mPassword = findViewById(R.id.register_password, TextInputEditText.class).getText();
                            form.mPasswordConfirm = findViewById(R.id.register_password_confirm, TextInputEditText.class).getText();

                            Logcat.v("Register clicked");
                            view.onRegisterClicked(form);
                        }));
    }
}
