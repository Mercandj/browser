package com.mercandalli.android.browser.ad_blocker;

import java.net.MalformedURLException;
import java.net.URL;

class UrlUtils {

    static String getHost(String url) throws MalformedURLException {
        return new URL(url).getHost();
    }
}