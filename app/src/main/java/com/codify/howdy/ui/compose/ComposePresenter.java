package com.codify.howdy.ui.compose;

import android.view.View;

import com.codify.howdy.R;
import com.codify.howdy.api.ApiManager;
import com.codify.howdy.api.pojo.ServiceConsumer;
import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.api.pojo.response.GetWordsResponse;
import com.codify.howdy.api.pojo.response.LoginResponse;
import com.codify.howdy.logcat.Logcat;
import com.codify.howdy.model.Emotion;
import com.codify.howdy.ui.base.BasePresenter;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;

final class ComposePresenter extends BasePresenter<ComposeView> {

    @Override
    public void attachView(ComposeView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.compose_send))
                        .subscribe(o -> {
                            Logcat.v("Send clicked");
                            view.onSendClicked();
                        }));
    }

    void bind(ArrayList<Emotion> list) {
        EmotionAdapter adapter = new EmotionAdapter(list);
        mDisposables.add(
                adapter
                        .itemClicks()
                        .subscribe(emotion -> {
                            Logcat.v("Emotion clicked");
                            mView.onEmotionClicked(emotion);
                        }));
    }

    void getWords() {
        mDisposables.add(
                ApiManager
                        .getInstance()
                        .getWords()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<GetWordsResponse>() {
                            @Override
                            protected void success(GetWordsResponse response) {
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
