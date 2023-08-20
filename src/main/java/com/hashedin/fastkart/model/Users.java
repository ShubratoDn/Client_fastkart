package com.hashedin.fastkart.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hashedin.fastkart.enums.UserType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;


@Getter
@Setter
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

    @JsonIgnore
    public String getPassword() {
    	return this.password;
    }
    
    @JsonProperty
    public void setPassword(String password) {
    	this.password = password;
    }
    
    
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "users")
    @JsonManagedReference    
    private List<Products> productsList = new ArrayList<>();

    @JsonIgnore
    public List<Products> getProductsList() {
		return productsList;
	}

    @JsonProperty
	public void setProductsList(List<Products> productsList) {
		this.productsList = productsList;
	}

    @JsonIgnore
	public List<Bids> getBidsList() {
		return bidsList;
	}
    @JsonProperty
	public void setBidsList(List<Bids> bidsList) {
		this.bidsList = bidsList;
	}


	@OneToMany(cascade = CascadeType.ALL, mappedBy = "users")
    private List<Bids> bidsList = new ArrayList<>();
}
