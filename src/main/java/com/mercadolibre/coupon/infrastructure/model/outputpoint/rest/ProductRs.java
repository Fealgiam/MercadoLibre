package com.mercadolibre.coupon.infrastructure.model.outputpoint.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductRs implements Serializable {

    @Serial
    private static final long serialVersionUID = -3328476288328020149L;

    private String id;

    private String title;

    @JsonProperty("site_id")
    private String countryCode;

    @JsonProperty("currency_id")
    private String currency;

    @JsonProperty("category_id")
    private String category;

    private Double price;

}
