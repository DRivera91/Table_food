package com.example.daniel.table_food.Model;

public class User {
    private String Email;
    private String Password;
    private String Phone;

    public User() {
    }


    public User(String email, String password) {
        Password = password;
        Email = email;
    }

    public User(String email, String password, String phone) {
        Email = email;
        Password = password;
        Phone = phone;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}
