package ru.itsphere.subscription.common.utils;

import java.util.Observable;

/**
 * Wrapper for information about client that notify observers which depends on from this information
 */
public class ObservableField<C> extends Observable {

    private BlockedField<C> value = new BlockedField<>();

    public C get() {
        return value.get();
    }

    public void set(C value) {
        this.value.set(value);
        setChanged();
        notifyObservers(this.value.get());
    }
}
