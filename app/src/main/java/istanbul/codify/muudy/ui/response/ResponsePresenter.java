package istanbul.codify.muudy.ui.response;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.style.StyleSpan;
import android.view.View;
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
import istanbul.codify.muudy.api.pojo.response.AnswerHiResponse;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.model.Word;
import istanbul.codify.muudy.ui.base.BasePresenter;

final class ResponsePresenter extends BasePresenter<ResponseView> {

    private Word mWord;

    @Override
    public void attachView(ResponseView view, View root) {
        super.attachView(view, root);

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
    }

    void bind(@NonNull User user) {
        Picasso
                .with(getContext())
                .load(BuildConfig.URL + user.imgpath1)
                .placeholder(R.drawable.ic_avatar)
                .into(findViewById(R.id.response_image, CircleImageView.class));

        findViewById(R.id.response_mention, AppCompatTextView.class).setText(
                new Spanny()
                        .append(user.username, new StyleSpan(Typeface.BOLD))
                        .append(", sana Muddy! dedi.\nHadi ona cevap ver"));
    }

    void answerHi(@NonNull Long userId) {
        if (mWord == null) {
            mView.onError(new IllegalStateException("Bir duygu secmelisiniz"));
            return;
        }

        AnswerHiRequest request = new AnswerHiRequest();
        request.token = AccountUtils.tokenLegacy(getContext());
        request.toUserId = userId;
        request.wordId = userId;

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .answerHi(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<AnswerHiResponse>() {
                            @Override
                            protected void success(AnswerHiResponse response) {
                                mView.onSent();
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void bind(Word word) {
        mWord = word;

        findViewById(R.id.response_word, AppCompatButton.class).setText(word.words_word);
    }
}
