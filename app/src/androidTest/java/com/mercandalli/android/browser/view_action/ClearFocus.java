package com.mercandalli.android.browser.view_action;

import android.view.View;

import org.hamcrest.Matcher;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.core.AllOf.allOf;

public class ClearFocus implements ViewAction {

    @Override
    public Matcher<View> getConstraints() {
        return allOf(isDisplayed(), isAssignableFrom(View.class));
    }

    @Override
    public String getDescription() {
        return "Clear focus on the given view";
    }

    @Override
    public void perform(UiController uiController, View view) {
        view.clearFocus();
    }
}