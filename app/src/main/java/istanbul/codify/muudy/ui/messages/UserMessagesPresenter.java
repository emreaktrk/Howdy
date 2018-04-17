package istanbul.codify.muudy.ui.messages;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import istanbul.codify.muudy.R;
import istanbul.codify.muudy.account.AccountUtils;
import istanbul.codify.muudy.api.ApiManager;
import istanbul.codify.muudy.api.pojo.ServiceConsumer;
import istanbul.codify.muudy.api.pojo.request.GetUserChatWallRequest;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.api.pojo.response.GetUserChatWallResponse;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.model.UserMessage;
import istanbul.codify.muudy.ui.base.BasePresenter;
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