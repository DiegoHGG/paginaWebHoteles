package com.uva.roomBookingAuth.Model;

public class User {
    private Integer id;
    private String name;
    private String email;
    private String password;
    private UserStatus status;

    public User() {
    }

    public User(String name, String email, UserStatus status) {
        this.name = name;
        this.email = email;
        this.status = status;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserStatus getStatus() {
        return this.status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }
}
