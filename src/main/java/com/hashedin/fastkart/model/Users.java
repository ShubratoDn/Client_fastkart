package com.hashedin.fastkart.model;

import java.util.ArrayList;
import java.util.List;

import com.hashedin.fastkart.enums.UserType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"productsList","bidsList"})
@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer userId;

    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "users")
    private List<Products> productsList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "users")
    private List<Bids> bidsList = new ArrayList<>();
}
