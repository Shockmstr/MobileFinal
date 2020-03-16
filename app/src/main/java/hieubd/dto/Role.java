package hieubd.dto;

public enum Role {
    User("User"),
    Manager("Manager"),
    Admin("Admin");
    private String value;

    Role(String value) {
        this.value = value;
    }
}
