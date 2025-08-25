package de.eshop.shared.domain;

/**
 * This Enum-Class is used to define a concrete type of {@link User}.
 */
public enum UserType {

    STAFF_MEMBER("STAFF_MEMBER"),
    CUSTOMER("CUSTOMER");

    UserType(String userTypeName) {
    }

}
