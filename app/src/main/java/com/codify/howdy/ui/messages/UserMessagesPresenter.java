package com.codify.howdy.ui.messages;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.codify.howdy.R;
import com.codify.howdy.account.AccountUtils;
import com.codify.howdy.api.ApiManager;
import com.codify.howdy.api.pojo.ServiceConsumer;
import com.codify.howdy.api.pojo.request.GetUserChatWallRequest;
import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.api.pojo.response.GetUserChatWallResponse;
import com.codify.howdy.logcat.Logcat;
import com.codify.howdy.model.UserMessage;
import com.codify.howdy.ui.base.BasePresenter;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

final class UserMessagesPresenter extends BasePresenter<UserMessagesView> {

    @Override
    public void attachView(UserMessagesView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.user_messages_close))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Close clicked");
                            view.onCloseClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.user_messages_new))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("New clicked");
                            view.onNewClicked();
                        }));

        findViewById(R.id.user_search_recycler, RecyclerView.class).setLayoutManager(new LinearLayoutManager(getContext()));
    }

    void getMessages() {
        GetUserChatWallRequest request = new GetUserChatWallRequest();
        request.token = AccountUtils.tokenLegacy(getContext());

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .getUserChatWall(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<GetUserChatWallResponse>() {
                            @Override
                            protected void success(GetUserChatWallResponse response) {
                                mView.onLoaded(response.data);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void bind(List<UserMessage> messages) {
        UserMessagesAdapter adapter = new UserMessagesAdapter(messages);
        mDisposables.add(
                adapter
                        .itemClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(message -> {
                            Logcat.v("User message clicked");
                            mView.onUserMessageClicked(message);
                        }));
        findViewById(R.id.user_search_recycler, RecyclerView.class).setAdapter(adapter);
    }
}
