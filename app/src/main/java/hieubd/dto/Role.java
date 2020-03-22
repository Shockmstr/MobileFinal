package hieubd.dto;

import androidx.annotation.NonNull;

import java.io.Serializable;

public enum Role implements Serializable {
    User("User"),
    Manager("Manager"),
    Admin("Admin"),
    None("None");

    private String value;

    Role(String value) {
        this.value = value;
    }

    @NonNull
    @Override
    public String toString() {
        return value;
    }
}
