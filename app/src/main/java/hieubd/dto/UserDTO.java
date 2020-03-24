package hieubd.dto;

import java.io.Serializable;

public class UserDTO implements Serializable {
    private int id;
    private String username;
    private String password;
    private String fullName;
    private Role role;
    private int managerId;

    public UserDTO() {
    }

    public UserDTO(int id, String username, String password, String fullName, Role role, int managerId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.managerId = managerId;
    }

    public UserDTO(String username, String password, String fullName, Role role, int managerId) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.managerId = managerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
