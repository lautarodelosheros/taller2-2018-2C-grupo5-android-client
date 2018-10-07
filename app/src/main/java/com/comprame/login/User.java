package com.comprame.login;

public class User {

    private final String name;
    private final String firstname;
    private final String lastname;
    private final String password;
    private final String email;

    public User(String name
            , String firstName
            , String lastName
            , String password
            , String email) {
        this.name = name;
        this.firstname = firstName;
        this.lastname = lastName;
        this.password = password;
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
