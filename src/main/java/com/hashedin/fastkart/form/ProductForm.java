package com.hashedin.fastkart.form;

import com.hashedin.fastkart.enums.ProductType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductForm {

    private String name;
    private String description;
    private Integer minBidAmount;
    private ProductType category;
    private String sellerId;
}
