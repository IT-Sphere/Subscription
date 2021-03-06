package ru.itsphere.subscription.common.utils;

/**
 * When some thread invokes method 'get' it can be stopped,
 * if an other thread hasn't switched field 'initialized' to true.
 * If an other thread has switched field 'initialized' to false,
 * it will not be stopped.
 * <p/>
 * Field can be null.
 */
public class BlockedField<C> {
    private C value;
    private boolean initialized = false;

    public synchronized C get() {
        while (!initialized) {
            try {
                wait();
            } catch (InterruptedException e) {
                // do nothing!
            }
        }
        return value;
    }

    public synchronized void set(C value) {
        this.value = value;
        initialized = true;
        notifyAll();
    }

    public synchronized void clear() {
        initialized = false;
        value = null;
    }

}
