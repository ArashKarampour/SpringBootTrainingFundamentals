package com.example.store.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

}
