package istanbul.codify.muudy.ui.web;

import android.annotation.SuppressLint;
import android.net.http.SslError;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebViewClient;
import com.blankj.utilcode.util.StringUtils;
import com.jakewharton.rxbinding2.view.RxView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.model.URL;
import istanbul.codify.muudy.model.Web;
import istanbul.codify.muudy.ui.base.BasePresenter;

final class WebPresenter extends BasePresenter<WebView> {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void attachView(WebView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.web_close))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Close clicked");

                            view.onCloseClicked();
                        }));

        findViewById(R.id.web_web, android.webkit.WebView.class)
                .getSettings()
                .setJavaScriptEnabled(true);

        findViewById(R.id.web_web, android.webkit.WebView.class)
                .setWebViewClient(new WebViewClient() {
                    @Override
                    public void onReceivedSslError(android.webkit.WebView view, SslErrorHandler handler, SslError error) {
                        mView.onSSLError(handler);
                    }
                });
    }

    void bind(@NonNull Web web) {
        findViewById(R.id.web_title, AppCompatTextView.class).setText(web.title);
        if (!StringUtils.isEmpty(web.url)) {
            findViewById(R.id.web_web, android.webkit.WebView.class).loadUrl(web.url);
        }
    }
}
