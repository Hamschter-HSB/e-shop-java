package net.eshop.ui.events;

/**
 * {@link javax.security.auth.callback.Callback} interface for successful {@link net.eshop.domain.User user logins}.
 * <p>
 * Called after a {@link net.eshop.domain.User} has successfully logged in.
 */
@FunctionalInterface
public interface LoginListener {

    void onLoginSuccess();
}
