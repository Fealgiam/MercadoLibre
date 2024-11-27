package com.mercadolibre.coupon.infrastructure.model.outputpoint.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CountryRs implements Serializable {

    private static final long serialVersionUID = 5414391183291423822L;

    private String id;

    private String name;

    @JsonProperty("country_id")
    private String countryId;

    @JsonProperty("default_currency_id")
    private String currency;

}
