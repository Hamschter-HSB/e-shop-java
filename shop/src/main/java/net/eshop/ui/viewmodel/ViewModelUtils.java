package net.eshop.ui.viewmodel;

public final class ViewModelUtils {

    public static boolean currentUserIsStaffMember() {
        return "STAFF_MEMBER".equals(System.getProperty("CURRENT_USER"));
    }
}
