package com.mercandalli.android.browser.browser;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/* package */ class BrowserUtils {

    private static String playerHtml;

    @NonNull
    /* package */ static String getHtml(@NonNull final Context context) {
        if (playerHtml == null) {
            playerHtml = readFileAssets(context);
        }
        return playerHtml;
    }

    @NonNull
    private static String readFileAssets(@NonNull final Context context) {
        try {
            final StringBuilder buf = new StringBuilder();
            final InputStream json = context.getAssets().open("browser.html");
            final BufferedReader in = new BufferedReader(new InputStreamReader(json, "UTF-8"));
            String str;
            while ((str = in.readLine()) != null) {
                buf.append(str).append('\n');
            }
            in.close();
            return buf.toString();
        } catch (IOException ignored) {
        }
        return "";
    }
}
