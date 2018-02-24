package com.codify.howdy.ui.chat;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.StringUtils;
import com.codify.howdy.R;
import com.codify.howdy.account.AccountUtils;
import com.codify.howdy.api.ApiManager;
import com.codify.howdy.api.pojo.ServiceConsumer;
import com.codify.howdy.api.pojo.request.GetMessagesRequest;
import com.codify.howdy.api.pojo.request.GetUserProfileRequest;
import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.api.pojo.response.GetMessagesResponse;
import com.codify.howdy.api.pojo.response.GetUserProfileResponse;
import com.codify.howdy.logcat.Logcat;
import com.codify.howdy.model.Chat;
import com.codify.howdy.model.User;
import com.codify.howdy.ui.base.BasePresenter;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;

final class ChatPresenter extends BasePresenter<ChatView> {

    @Override
    public void attachView(ChatView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.chat_back))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Back clicked");
                            view.onBackClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.chat_send))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Send clicked");
                            view.onSendClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.chat_media))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Media clicked");
                            view.onMediaClicked();
                        }));

        mDisposables.add(
                RxTextView
                        .textChanges(findViewById(R.id.chat_message))
                        .flatMap((Function<CharSequence, ObservableSource<String>>) content -> Observable.just(StringUtils.isEmpty(content) ? "" : content.toString().trim()))
                        .flatMap((Function<String, ObservableSource<Boolean>>) message -> Observable.just(!StringUtils.isEmpty(message)))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(enabled -> findViewById(R.id.chat_send).setEnabled(enabled)));
    }

    void bind(@NonNull User user) {
        findViewById(R.id.chat_username, AppCompatTextView.class).setText(user.username);
    }

    void bind(@NonNull List<Chat> chats) {
        ChatAdapter adapter = new ChatAdapter(chats);
        findViewById(R.id.chat_recycler, RecyclerView.class).setAdapter(adapter);
    }

    void send(String text) {

    }

    public void getUser(Long userId) {
        GetUserProfileRequest request = new GetUserProfileRequest(userId);
        request.token = AccountUtils.tokenLegacy(getContext());

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .getUserProfile(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<GetUserProfileResponse>() {
                            @Override
                            protected void success(GetUserProfileResponse response) {
                                mView.onLoaded(response.data.user);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    public void getMessages(Long userId) {
        GetMessagesRequest request = new GetMessagesRequest();
        request.token = AccountUtils.tokenLegacy(getContext());
        request.otherUserId = userId;

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .getMessages(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<GetMessagesResponse>() {
                            @Override
                            protected void success(GetMessagesResponse response) {
                                mView.onLoaded(response.data);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }
}