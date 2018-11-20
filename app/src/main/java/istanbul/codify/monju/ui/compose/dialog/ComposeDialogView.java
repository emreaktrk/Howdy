package istanbul.codify.monju.ui.compose.dialog;

import istanbul.codify.monju.ui.base.MvpView;


interface ComposeDialogView extends MvpView {

    void onShareClicked();

    void onFacebookClicked();

    void onTwitterClicked();

    void onCloseClicked();
}
