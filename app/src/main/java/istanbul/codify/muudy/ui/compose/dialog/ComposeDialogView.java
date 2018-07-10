package istanbul.codify.muudy.ui.compose.dialog;

import istanbul.codify.muudy.ui.base.MvpView;


interface ComposeDialogView extends MvpView {

    void onShareClicked();

    void onFacebookClicked();

    void onTwitterClicked();

    void onCloseClicked();
}
