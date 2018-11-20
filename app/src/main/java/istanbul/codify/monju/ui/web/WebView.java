package istanbul.codify.monju.ui.web;

import android.webkit.SslErrorHandler;
import istanbul.codify.monju.ui.base.MvpView;

interface WebView extends MvpView {

    void onSSLError(SslErrorHandler handler);

    void onCloseClicked();
}
