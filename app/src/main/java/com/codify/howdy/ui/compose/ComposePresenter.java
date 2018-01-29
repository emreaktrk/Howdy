package com.codify.howdy.ui.compose;

import android.view.View;

import com.codify.howdy.R;
import com.codify.howdy.logcat.Logcat;
import com.codify.howdy.model.Emotion;
import com.codify.howdy.ui.base.BasePresenter;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;

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
}
