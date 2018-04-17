package istanbul.codify.muudy.ui.chat;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import istanbul.codify.muudy.BuildConfig;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.account.AccountUtils;
import istanbul.codify.muudy.api.ApiManager;
import istanbul.codify.muudy.api.pojo.ServiceConsumer;
import istanbul.codify.muudy.api.pojo.request.GetMessagesRequest;
import istanbul.codify.muudy.api.pojo.request.GetUserProfileRequest;
import istanbul.codify.muudy.api.pojo.request.SendMessageRequest;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.api.pojo.response.GetMessagesResponse;
import istanbul.codify.muudy.api.pojo.response.GetUserProfileResponse;
import istanbul.codify.muudy.api.pojo.response.SendMessageResponse;
import istanbul.codify.muudy.helper.decoration.SideSpaceItemDecoration;
import istanbul.codify.muudy.helper.decoration.VerticalSpaceItemDecoration;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.model.Chat;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.ui.base.BasePresenter;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;

import java.util.List;

final class ChatPresenter extends BasePresenter<ChatView> {

    private User mUser;

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

                            String message = findViewById(R.id.chat_message, AppCompatEditText.class).getText().toString();
                            view.onSendClicked(message);
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

        RecyclerView recycler = findViewById(R.id.chat_recycler, RecyclerView.class);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));

        int space = SizeUtils.dp2px(16);
        recycler.addItemDecoration(new SideSpaceItemDecoration(space, LinearLayoutManager.HORIZONTAL));
        recycler.addItemDecoration(new SideSpaceItemDecoration(space / 2, LinearLayoutManager.VERTICAL));
    }

    void bind(@NonNull User user) {
        mUser = user;

        findViewById(R.id.chat_username, AppCompatTextView.class).setText(user.username);
        Picasso
                .with(getContext())
                .load(BuildConfig.URL + user.imgpath1)
                .into(findViewById(R.id.chat_user_image, CircleImageView.class));
    }

    void bind(@NonNull List<Chat> chats) {
        long userId = AccountUtils.me(getContext()).iduser;

        ChatAdapter adapter = new ChatAdapter(chats, userId);
        findViewById(R.id.chat_recycler, RecyclerView.class).setAdapter(adapter);
    }

    void send(String message) {
        findViewById(R.id.chat_message, AppCompatEditText.class).setText(null);

        SendMessageRequest request = new SendMessageRequest();
        request.text = message;
        request.toUserId = mUser.iduser;
        request.token = AccountUtils.tokenLegacy(getContext());

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .sendMessage(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<SendMessageResponse>() {
                            @Override
                            protected void success(SendMessageResponse response) {
                                mView.onLoaded(response.data);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void send(Base64 data) {
        findViewById(R.id.chat_message, AppCompatEditText.class).setText(null);

        SendMessageRequest request = new SendMessageRequest();
        request.toUserId = mUser.iduser;
        request.data = data;
        request.token = AccountUtils.tokenLegacy(getContext());

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .sendMessage(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<SendMessageResponse>() {
                            @Override
                            protected void success(SendMessageResponse response) {
                                mView.onLoaded(response.data);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
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
