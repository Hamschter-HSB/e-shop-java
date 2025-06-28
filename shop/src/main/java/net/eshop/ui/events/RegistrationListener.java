package net.eshop.ui.events;

/**
 * {@link javax.security.auth.callback.Callback} interface for successful {@link net.eshop.domain.Customer} registrations.
 * <p>
 * Called after a {@link net.eshop.domain.StaffMember} has successfully been registered by an {@link net.eshop.domain.StaffMember}.
 */
@FunctionalInterface
public interface RegistrationListener {

    void onRegistrationSuccess();
}
