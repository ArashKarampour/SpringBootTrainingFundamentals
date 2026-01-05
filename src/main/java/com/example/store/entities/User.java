package com.example.store.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "users") // this will bind this entity to the table with the name users
public class User {
    @Id // this will set the id as primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // because the primary id was auto_incremented in the database (best practice)
    private Long id;

    @Column(nullable = false, name = "name") // this will bind this field to the column in database with the name of name (best practice)
    private String name;
    @Column(nullable = false, name = "email")
    private String email;
    @Column(nullable = false, name = "password")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
