package com.example.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Builder    // using builder pattern for creating Profile objects
@ToString
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "bio")
    private String bio;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "loyalty_points")
    private Integer loyaltyPoints;

    @OneToOne(fetch = FetchType.LAZY) // lazy loading to avoid loading user data unless it is accessed. for one-to-one only the owner side should specify the fetch type for the other side you can delete it and the Profile object will not be shown for the user.
    @JoinColumn(name = "id") // id in profiles table is both primary key and foreign key referencing users(id)
    @MapsId // to indicate that the primary key of this entity is also a foreign key to the User entity
    @ToString.Exclude
    private User user;
}
