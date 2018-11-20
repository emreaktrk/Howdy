package istanbul.codify.monju.ui.feedback;

import android.support.design.widget.TextInputEditText;
import android.view.View;
import com.blankj.utilcode.util.StringUtils;
import com.jakewharton.rxbinding2.view.RxView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import istanbul.codify.monju.R;
import istanbul.codify.monju.account.AccountUtils;
import istanbul.codify.monju.api.ApiManager;
import istanbul.codify.monju.api.pojo.ServiceConsumer;
import istanbul.codify.monju.api.pojo.request.SendFeedbackRequest;
import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.api.pojo.response.SendFeedbackResponse;
import istanbul.codify.monju.logcat.Logcat;
import istanbul.codify.monju.ui.base.BasePresenter;

final class FeedbackPresenter extends BasePresenter<FeedbackView> {

    @Override
    public void attachView(FeedbackView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.feedback_back))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Back clicked");

                            view.onBackClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.feedback_send))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Send clicked");

                            view.onSendClicked();
                        }));
    }

    void send() {
        String feedback = findViewById(R.id.feedback_text, TextInputEditText.class).getText().toString().trim();
        if (StringUtils.isEmpty(feedback)) {
            mView.onError(new IllegalStateException("Lutfen geri bildirim alanini doldurunuz"));
            return;
        }
        SendFeedbackRequest request = new SendFeedbackRequest();
        request.token = AccountUtils.tokenLegacy(getContext());
        request.text = feedback;

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .sendFeedback(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<SendFeedbackResponse>() {
                            @Override
                            protected void success(SendFeedbackResponse response) {
                                mView.onLoaded();
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }
}
