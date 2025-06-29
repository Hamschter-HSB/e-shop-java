package net.eshop.ui.events;

/**
 * {@link javax.security.auth.callback.Callback} interface for convenient returning to a UI.
 * <p>
 * Called when a {@link net.eshop.domain.User users} UI perspective shall be changed to a different UI.
 */
@FunctionalInterface
public interface UIBackListener {

    void onReturn();
}
