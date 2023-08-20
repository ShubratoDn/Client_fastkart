package com.hashedin.fastkart.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hashedin.fastkart.enums.ProductType;

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
@ToString(exclude = {"users","bidsList"})
@Entity
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer productId;

    private String name;

    private String description;

    private Integer minBidAmount;

    @Enumerated(EnumType.STRING)
    private ProductType productType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDateTime;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @JsonBackReference //Because this class is Child
    private Users users;

    @OneToMany(mappedBy = "products")
    @JsonManagedReference //Because this class is Parent
    private List<Bids> bidsList = new ArrayList<>();

}
