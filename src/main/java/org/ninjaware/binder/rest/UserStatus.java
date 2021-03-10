package org.ninjaware.binder.rest;

public enum UserStatus {
    Initial("Initial"),
    Active("Active"),
    Suspended("Suspended"),
    Deleted("Deleted"),
    AwaitConfirmation("Await-confirmation");

    private final String label;

    private UserStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
