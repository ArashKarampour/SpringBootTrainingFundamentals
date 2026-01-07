package com.example.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users") // this will bind this entity to the table with the name users
public class User {
    @Id // this will set the id as primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // because the primary id was auto_incremented in the database (best practice)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, name = "name") // this will bind this field to the column in database with the name of name (best practice)
    private String name;
    @Column(nullable = false, name = "email")
    private String email;
    @Column(nullable = false, name = "password")
    private String password;

    @OneToMany(mappedBy = "user") // one user can have many addresses // mappedBy is used to specify the field in the Address entity that owns the relationship
    private List<Address> addresses = new ArrayList<>(); // initialize the list to avoid null pointer exception but it works only with the default constructor for other constructors you have to initialize it manually

    // helper method to add address to user in main application
    public void addAddress(Address address) {
        this.addresses.add(address);
        address.setUser(this); // set the user in the address to this user
    }

    public void removeAddress(Address address) {
        this.addresses.remove(address);
        address.setUser(null); // remove the user from the address
    }
}
