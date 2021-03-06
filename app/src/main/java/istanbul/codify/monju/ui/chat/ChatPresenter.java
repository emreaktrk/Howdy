package istanbul.codify.monju.ui.chat;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.LinearLayout;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.marchinram.rxgallery.RxGallery;
import com.squareup.picasso.Picasso;
import com.tbruyelle.rxpermissions2.RxPermissions;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import istanbul.codify.monju.BuildConfig;
import istanbul.codify.monju.R;
import istanbul.codify.monju.account.AccountUtils;
import istanbul.codify.monju.api.ApiManager;
import istanbul.codify.monju.api.pojo.ServiceConsumer;
import istanbul.codify.monju.api.pojo.request.GetMessagesRequest;
import istanbul.codify.monju.api.pojo.request.GetUserProfileRequest;
import istanbul.codify.monju.api.pojo.request.SendMessageRequest;
import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.api.pojo.response.GetMessagesResponse;
import istanbul.codify.monju.api.pojo.response.GetUserProfileResponse;
import istanbul.codify.monju.api.pojo.response.SendMessageResponse;
import istanbul.codify.monju.helper.decoration.SideSpaceItemDecoration;
import istanbul.codify.monju.logcat.Logcat;
import istanbul.codify.monju.model.Chat;
import istanbul.codify.monju.model.User;
import istanbul.codify.monju.ui.base.BasePresenter;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

final class ChatPresenter extends BasePresenter<ChatView> {

    public User getmUser() {
        return mUser;
    }

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
                RxView
                        .clicks(findViewById(R.id.chat_user_image))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Profile clicked");

                            view.onProfileClicked(mUser.iduser);
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.chat_username))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Profile clicked");

                            view.onProfileClicked(mUser.iduser);
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

        int space = SizeUtils.dp2px(0);
        recycler.addItemDecoration(new SideSpaceItemDecoration(space, LinearLayoutManager.HORIZONTAL));
        recycler.addItemDecoration(new SideSpaceItemDecoration(space / 2, LinearLayoutManager.VERTICAL));
    }


    void bind(@NonNull User user) {
        mUser = user;

        findViewById(R.id.chat_username, AppCompatTextView.class).setText(user.username);
        Picasso
                .with(getContext())
                .load(BuildConfig.URL + user.imgpath1)
                .placeholder(R.drawable.ic_avatar)
                .into(findViewById(R.id.chat_user_image, CircleImageView.class));
    }

    void bind(@NonNull List<Chat> chats) {
        long userId = AccountUtils.me(getContext()).iduser;

        ChatAdapter adapter = new ChatAdapter(chats, userId);
        mDisposables.add(
                adapter
                        .imageClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cell -> {
                            Logcat.v("Image clicked");
                            mView.onImageClicked(cell);
                        }));
        findViewById(R.id.chat_recycler, RecyclerView.class).setAdapter(adapter);

        if (chats.size() > 0) {
            Chat lastMessage = chats.get(0);

            if (lastMessage.message_fromuserid == userId) {
                if (lastMessage.message_isreaded == 1) {
                    findViewById(R.id.chat_message_readed_text, AppCompatTextView.class).setText("Okundu");
                } else {
                    findViewById(R.id.chat_message_readed_text, AppCompatTextView.class).setText("Gönderildi");
                }
                findViewById(R.id.chat_message_readed_container, LinearLayout.class).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.chat_message_readed_container, LinearLayout.class).setVisibility(View.GONE);
            }
        }else{
            findViewById(R.id.chat_message_readed_container, LinearLayout.class).setVisibility(View.GONE);
        }
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
                                mView.onMessageSent(response.data.r);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void send(byte[] bytes) {
        findViewById(R.id.chat_message, AppCompatEditText.class).setText(null);

        SendMessageRequest request = new SendMessageRequest();
        request.toUserId = mUser.iduser;
        request.data = Base64.encodeToString(bytes, Base64.DEFAULT);
        request.token = AccountUtils.tokenLegacy(getContext());

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .sendMessage(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<SendMessageResponse>() {
                            @Override
                            protected void success(SendMessageResponse response) {
                                mView.onMessageSent(response.data.r);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void send(Uri uri) {
        try {
            InputStream input = getContext().getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            byte[] bytes = output.toByteArray();

            send(bytes);
        } catch (Exception e) {
            Logcat.e(e);

            mView.onError(new Throwable("Fotograf yuklenemedi."));
        }
    }

    void selectPhoto(@NonNull AppCompatActivity activity) {
        mDisposables.add(
                new RxPermissions(activity)
                        .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .flatMap((Function<Boolean, ObservableSource<List<Uri>>>) granted -> granted ? RxGallery.gallery(activity, false).toObservable() : Observable.empty())
                        .subscribe(uris -> {
                            Uri uri = uris.get(0);
                            Logcat.v("Selected uri for photo is " + uri.toString());

                            mView.onPhotoSelected(uri);
                        })
        );
    }

    void capturePhoto(@NonNull AppCompatActivity activity) {
        mDisposables.add(
                new RxPermissions(activity)
                        .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .flatMap((Function<Boolean, ObservableSource<Uri>>) granted -> granted ? RxGallery.photoCapture(activity).toObservable() : Observable.empty())
                        .subscribe(uri -> {
                            Logcat.v("Selected uri for photo is " + uri.toString());

                            mView.onPhotoSelected(uri);
                        })
        );
    }

    void getUser(Long userId) {
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

    void getMessages(Long userId) {
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
