package com.codify.howdy.ui.compose;

import com.codify.howdy.model.Emotion;
import com.codify.howdy.ui.base.MvpView;


interface ComposeView extends MvpView {

    void onEmotionClicked(Emotion emotion);

    void onSendClicked();
}
