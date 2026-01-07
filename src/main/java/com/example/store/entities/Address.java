package com.example.store.entities;

import jakarta.persistence.*;
import lombok.*;

@ToString
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, name = "street")
    private String street;

    @Column(nullable = false, name = "city")
    private String city;

    @Column(nullable = false, name = "zip")
    private String zip;

    @ManyToOne
    @JoinColumn(name = "user_id") // this is for the foreign key column in addresses table that references users table(users(id))
    @ToString.Exclude             // to avoid circular reference in toString method
    private User user;            // Address side(many side) is the owner of the relationship because it contains the foreign key for the user(one side).
}
