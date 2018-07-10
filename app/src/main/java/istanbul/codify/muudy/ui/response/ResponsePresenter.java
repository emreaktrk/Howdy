package istanbul.codify.muudy.ui.response;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.LinearLayout;
import com.binaryfork.spanny.Spanny;
import com.jakewharton.rxbinding2.view.RxView;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import istanbul.codify.muudy.BuildConfig;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.account.AccountUtils;
import istanbul.codify.muudy.api.ApiManager;
import istanbul.codify.muudy.api.pojo.ServiceConsumer;
import istanbul.codify.muudy.api.pojo.request.AnswerHiRequest;
import istanbul.codify.muudy.api.pojo.request.GetUserProfileRequest;
import istanbul.codify.muudy.api.pojo.response.AnswerHiResponse;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.api.pojo.response.GetUserProfileResponse;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.model.Notification;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.model.Word;
import istanbul.codify.muudy.ui.base.BasePresenter;

final class ResponsePresenter extends BasePresenter<ResponseView> {

    public Word getmWord() {
        return mWord;
    }

    private Word mWord;
    private User mUser;

    ResponseView mView;
    @Override
    public void attachView(ResponseView view, View root) {
        super.attachView(view, root);
        mView = view;
        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.response_close))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Back clicked");
                            view.onCloseClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.response_send))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Send clicked");
                            view.onSendClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.response_word))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Word select clicked");
                            view.onWordSelectClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.response_image))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Word select clicked");
                            view.onProfilePhotoClicked(mUser);
                        }));

    findViewById(R.id.response_container, ConstraintLayout.class).setVisibility(View.GONE);
    }

    void addClickToWordContainer() {
        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.response_word_container))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Word select clicked");
                            mView.onWordSelectClicked();
                        }));
    }

    void getUserProfile(Long userId, String notificationMessage) {
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
                                mView.onLoaded(response.data.user, notificationMessage);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void getUserProfileForAnswer(Long userId, String notificationMessage, String wordText, String wordImage) {
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
                                mView.onLoaded(response.data.user, notificationMessage, wordText, wordImage);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void bind(@NonNull Notification notification, Boolean isAnswer) {
        User user = notification.fromUser;
        mUser = user;
        Picasso
                .with(getContext())
                .load(BuildConfig.URL + user.imgpath1)
                .placeholder(R.drawable.ic_avatar)
                .into(findViewById(R.id.response_image, CircleImageView.class));

        findViewById(R.id.response_username, AppCompatTextView.class).setText(user.username);

        findViewById(R.id.response_mention, AppCompatTextView.class).setText(
                new Spanny(notification.notification_msg).findAndSpan(user.username, new Spanny.GetSpan() {
                    @Override
                    public Object getSpan() {
                        return new StyleSpan(Typeface.BOLD);
                    }
                }));

        if (isAnswer) {
            findViewById(R.id.response_word_container).setVisibility(View.VISIBLE);
            findViewById(R.id.response_word, AppCompatButton.class).setVisibility(View.GONE);
            findViewById(R.id.response_selected_word, AppCompatTextView.class).setText(notification.notification_answerhi_word_text);
            findViewById(R.id.response_send, AppCompatButton.class).setVisibility(View.GONE);
            Picasso
                    .with(getContext())
                    .load(BuildConfig.URL + notification.notification_answerhi_word_img)
                    .placeholder(R.drawable.ic_avatar)
                    .into(findViewById(R.id.response_word_image, AppCompatImageView.class));
        } else {
            findViewById(R.id.response_send, AppCompatButton.class).setVisibility(View.VISIBLE);
        }

        findViewById(R.id.response_container, ConstraintLayout.class).setVisibility(View.VISIBLE);
    }

    void bind(@NonNull User user, String text) {
        mUser = user;
        Picasso
                .with(getContext())
                .load(BuildConfig.URL + user.imgpath1)
                .placeholder(R.drawable.ic_avatar)
                .into(findViewById(R.id.response_image, CircleImageView.class));

        findViewById(R.id.response_username, AppCompatTextView.class).setText(user.username);

        findViewById(R.id.response_mention, AppCompatTextView.class).setText(
                new Spanny(text).findAndSpan(user.username, new Spanny.GetSpan() {
                    @Override
                    public Object getSpan() {
                        return new StyleSpan(Typeface.BOLD);
                    }
                }));
        findViewById(R.id.response_container, ConstraintLayout.class).setVisibility(View.VISIBLE);

    }

    void bind(@NonNull User user, String text, String wordText, String wordImage) {
        mUser = user;
        Picasso
                .with(getContext())
                .load(BuildConfig.URL + user.imgpath1)
                .placeholder(R.drawable.ic_avatar)
                .into(findViewById(R.id.response_image, CircleImageView.class));

        findViewById(R.id.response_username, AppCompatTextView.class).setText(user.username);

        findViewById(R.id.response_mention, AppCompatTextView.class).setText(
                new Spanny(text).findAndSpan(user.username, new Spanny.GetSpan() {
                    @Override
                    public Object getSpan() {
                        return new StyleSpan(Typeface.BOLD);
                    }
                }));


        findViewById(R.id.response_send, AppCompatButton.class).setVisibility(View.GONE);
        findViewById(R.id.response_word, AppCompatButton.class).setVisibility(View.GONE);
        findViewById(R.id.response_word_container).setVisibility(View.VISIBLE);
        findViewById(R.id.response_selected_word, AppCompatTextView.class).setText(wordText);

        Picasso
                .with(getContext())
                .load(BuildConfig.URL + wordImage)
                .placeholder(R.drawable.ic_avatar)
                .into(findViewById(R.id.response_word_image, AppCompatImageView.class));
        findViewById(R.id.response_container, ConstraintLayout.class).setVisibility(View.VISIBLE);

    }

    void answerHi(@NonNull Long userId) {
        if (mWord == null) {
            mView.onError(new IllegalStateException("Bir duygu secmelisiniz"));
            return;
        }

        AnswerHiRequest request = new AnswerHiRequest();
        request.token = AccountUtils.tokenLegacy(getContext());
        request.toUserId = userId;
        request.wordId = mWord.idwords;

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .answerHi(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<AnswerHiResponse>() {
                            @Override
                            protected void success(AnswerHiResponse response) {
                                if(response.data.r.equals("ok")) {
                                    mView.onSent();
                                }
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);
                                mView.onSent();
                                mView.onError(error);
                            }
                        }));
    }

    void bind(Word word) {
        mWord = word;

        findViewById(R.id.response_word_container, LinearLayout.class).setVisibility(View.VISIBLE);
        findViewById(R.id.response_word, AppCompatButton.class).setVisibility(View.GONE);
        findViewById(R.id.response_selected_word, AppCompatTextView.class).setText(word.words_word);

        Picasso
                .with(getContext())
                .load(BuildConfig.URL + word.words_emoji_url)
                .placeholder(R.drawable.ic_avatar)
                .into(findViewById(R.id.response_word_image, AppCompatImageView.class));

        addClickToWordContainer();
    }



    User getUser() {
        return mUser;
    }
}
