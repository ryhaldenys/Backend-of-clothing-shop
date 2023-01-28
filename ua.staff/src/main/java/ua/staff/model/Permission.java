package ua.staff.model;

public enum Permission {
    ADVANCED("advanced"),
    SIMPLE("simple");

    private final String permission;

    public String getPermission() {
        return permission;
    }

    Permission(String permission) {
        this.permission = permission;
    }
}

