package com.example.KodnestSalesSavvy.entities;

public class userdao {
    int userid;
    String username;
    String email;
    String role;

    public userdao() {
    }

    public userdao(int userid, String username, String email, String role) {
        this.userid = userid;
        this.username = username;
        this.email = email;
        this.role = role;
    }
}
