package com.example.android.bakingrecipes.IdlingResource;

import android.support.test.espresso.IdlingResource;
import org.jetbrains.annotations.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class used for IdlingResource in espresso test
 */
public class BakingAppIdlingResource implements IdlingResource {

    @Nullable
    private volatile ResourceCallback callback;
    private AtomicBoolean isIdleNow = new AtomicBoolean(false);


    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return isIdleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.callback = callback;
    }

    public void setIdleState(boolean isIdleNow) {
        this.isIdleNow.set(isIdleNow);
        if (isIdleNow && callback != null) {
            callback.onTransitionToIdle();
        }
    }

}
