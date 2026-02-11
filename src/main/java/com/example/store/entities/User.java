package com.example.store.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public User(String name, String email, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true) // one user can have many addresses // mappedBy is used to specify the field in the Address entity that owns the relationship // cascade is used to specify that when we persist or delete a user, we also want to persist or delete its addresses automatically // orphanRemoval is used to specify that when we remove an address from the user's collection, we also want to delete it from the database
    private List<Address> addresses = new ArrayList<>(); // initialize the list to avoid null pointer exception but it works only with the default constructor for other constructors you have to initialize it manually

    // helper method to add address to user in main application
    public void addAddress(Address address) {
        this.addresses.add(address);
        address.setUser(this); // set the user in the address to this user // // this is important to keep bidirectional relationships in sync
    }

    public void removeAddress(Address address) {
        this.addresses.remove(address);
        address.setUser(null); // remove the user from the address // this is important to keep bidirectional relationships in sync
    }

    @ManyToMany // in many-to-many relationship each side could be the owner of the relationship but we have to specify one side as the owner (here we chose User as the owner)
    @JoinTable( // specify the join table for the many-to-many relationship (in the owner side)
        name = "user_tags", // name of the join table
        joinColumns = @JoinColumn(name = "user_id"), // foreign key column in the join table that references the user (note that joinColumns is for the owner side's forign key not the other one so it shoud be user_id)
        inverseJoinColumns = @JoinColumn(name = "tag_id") // foreign key column in the join table that references the tag
    )
    private Set<Tag> tags = new HashSet<>();


    public void addTag(String tagName) {
        Tag tag = new Tag(tagName);
        this.tags.add(tag);
        tag.getUsers().add(this); // maintain the bidirectional relationship
    }

    public void removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.getUsers().remove(this); // maintain the bidirectional relationship
    }

//    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE) // one-to-one relationship with Profile entity // mappedBy is used to specify the field in the Profile entity that owns the relationship // cascade is used to specify that when we delete a user, we also want to delete its profile automatically
//    private Profile profile;

    @ManyToMany
    @JoinTable(name = "wishlist", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Set<Product> wishlistProducts = new HashSet<>();

    public void addWishlistProduct(Product product) {
        this.wishlistProducts.add(product);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "email = " + email + ")";
    }
}
