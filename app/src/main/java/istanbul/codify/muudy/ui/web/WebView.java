package istanbul.codify.muudy.ui.web;

import android.webkit.SslErrorHandler;
import istanbul.codify.muudy.ui.base.MvpView;

interface WebView extends MvpView {

    void onSSLError(SslErrorHandler handler);

    void onCloseClicked();
}
