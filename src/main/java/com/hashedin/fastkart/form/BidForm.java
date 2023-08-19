package com.hashedin.fastkart.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BidForm {

    private int bidAmount;

    private String sellerId;

    private String productId;
}
