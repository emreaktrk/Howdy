package istanbul.codify.muudy.ui.response;

import istanbul.codify.muudy.ui.base.MvpView;

interface ResponseView extends MvpView {

    void onWordSelectClicked();

    void onSendClicked();

    void onCloseClicked();
}
