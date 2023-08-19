package com.hashedin.fastkart.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private Users users;

    @OneToMany(mappedBy = "products")
    private List<Bids> bidsList = new ArrayList<>();

}
