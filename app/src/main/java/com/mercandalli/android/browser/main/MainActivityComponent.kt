package com.mercandalli.android.browser.main

import dagger.Component

@MainActivityContract.Scope
@Component(
        dependencies = arrayOf(MainComponent::class),
        modules = arrayOf(MainActivityModule::class))
internal interface MainActivityComponent {

    fun provideMainActivityUserAction(): MainActivityContract.UserAction
}
