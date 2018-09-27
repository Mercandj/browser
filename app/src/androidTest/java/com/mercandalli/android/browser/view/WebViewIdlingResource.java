package com.mercandalli.android.browser.view;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.test.espresso.IdlingResource;

import static androidx.test.espresso.intent.Checks.checkNotNull;

public class WebViewIdlingResource extends WebChromeClient implements IdlingResource {

    private static final int FINISHED = 100;

    private WebView webView;
    private ResourceCallback callback;

    public WebViewIdlingResource(WebView webView) {
        this.webView = checkNotNull(webView,
                String.format("Trying to instantiate a \'%s\' with a null WebView", getName()));
        // Shall we save the original client? Atm it's not used though.
        this.webView.setWebChromeClient(this);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        //if (newProgress == FINISHED && view.getTitle() != null && callback != null) {
        //    callback.onTransitionToIdle();
        //}
        if (callback != null) {
            callback.onTransitionToIdle();
        }
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        if (webView.getProgress() == FINISHED && callback != null) {
            callback.onTransitionToIdle();
        }
    }

    @Override
    public String getName() {
        return "WebView idling resource";
    }

    @Override
    public boolean isIdleNow() {
        // The webView hasn't been injected yet, so we're idling
        if (webView == null) return true;
        return webView.getProgress() == FINISHED && webView.getTitle() != null;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        this.callback = resourceCallback;
    }
}