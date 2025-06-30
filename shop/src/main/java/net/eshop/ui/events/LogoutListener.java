package net.eshop.ui.events;

/**
 * {@link javax.security.auth.callback.Callback} interface for successful {@link net.eshop.domain.User user logouts}.
 * <p>
 * Called after a {@link net.eshop.domain.User} has successfully logged out.
 */
@FunctionalInterface
public interface LogoutListener {

    void onLogoutSuccess();
}
