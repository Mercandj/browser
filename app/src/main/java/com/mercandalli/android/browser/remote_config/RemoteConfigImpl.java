package com.mercandalli.android.browser.remote_config;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.mercandalli.android.browser.BuildConfig;
import com.mercandalli.android.browser.main_thread.MainThreadPost;
import com.mercandalli.android.browser.update.UpdateManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;

/**
 * This class is used to get the remote configuration from firebase
 */
class RemoteConfigImpl implements RemoteConfig {

    private static final HashMap<String, Object> defaultMap = new HashMap<>();
    private static final String FIREBASE_KEY_FULL_VERSION_AVAILABLE_PERCENT = "full_version_available_percent";
    private static final String FIREBASE_KEY_ON_BOARDING_STORE_PAGE_AVAILABLE_PERCENT = "on_boarding_store_page_available_percent";

    static {
        defaultMap.put(FIREBASE_KEY_FULL_VERSION_AVAILABLE_PERCENT, 0);
        defaultMap.put(FIREBASE_KEY_ON_BOARDING_STORE_PAGE_AVAILABLE_PERCENT, 0);
    }

    private final MainThreadPost mainThreadPost;
    private final FirebaseRemoteConfig firebaseRemoteConfig;
    @FloatRange(from = 0F, to = 1F)
    private final float randomFullVersionAvailablePercent;
    @FloatRange(from = 0F, to = 1F)
    private final float randomOnBoardingStorePageAvailablePercent;
    private final List<RemoteConfigListener> listeners = new ArrayList<>();
    private boolean initialized = false;

    /* package */ RemoteConfigImpl(
            UpdateManager updateManager,
            MainThreadPost mainThreadPost,
            @FloatRange(from = 0F, to = 1F) float randomFullVersionAvailablePercent,
            @FloatRange(from = 0F, to = 1F) float randomOnBoardingStorePageAvailablePercent
    ) {
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        this.mainThreadPost = mainThreadPost;
        this.randomFullVersionAvailablePercent = randomFullVersionAvailablePercent;
        this.randomOnBoardingStorePageAvailablePercent = randomOnBoardingStorePageAvailablePercent;
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        firebaseRemoteConfig.setConfigSettings(configSettings);
        firebaseRemoteConfig.setDefaults(defaultMap);
        boolean firstLaunchAfterUpdate = updateManager.isFirstLaunchAfterUpdate();
        boolean bypassCache = BuildConfig.DEBUG || firstLaunchAfterUpdate;
        (bypassCache ? firebaseRemoteConfig.fetch(0) : firebaseRemoteConfig.fetch())
                .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                firebaseRemoteConfig.activateFetched();
                                initialized = true;
                                notifyInitialized();
                            }
                        }
                );
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public boolean isFullVersionAvailable() {
        return randomFullVersionAvailablePercent <= firebaseRemoteConfig.getDouble(FIREBASE_KEY_FULL_VERSION_AVAILABLE_PERCENT);
    }

    @Override
    public boolean isOnBoardingStoreAvailable() {
        return randomOnBoardingStorePageAvailablePercent <= firebaseRemoteConfig.getDouble(FIREBASE_KEY_ON_BOARDING_STORE_PAGE_AVAILABLE_PERCENT);
    }

    @Override
    public void registerListener(@NonNull RemoteConfigListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public void unregisterListener(@NonNull RemoteConfigListener listener) {
        listeners.remove(listener);
    }

    private void notifyInitialized() {
        if (!mainThreadPost.isOnMainThread()) {
            mainThreadPost.post(this::notifyInitialized);
            return;
        }
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onInitialized();
        }
    }
}
