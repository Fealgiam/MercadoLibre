package com.mercadolibre.coupon.infrastructure.model.outputpoint.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "redeemed_products")
public class RedeemedProductDAO implements Serializable {

    @Serial
    private static final long serialVersionUID = -8644694357611138726L;

    @Id
    @Column(name = "id_product")
    private String idProduct;

    @Column(name = "id_country")
    private String idCountry;

    @Column(name = "number_redeemed")
    private Integer numberRedeemed;

    @Builder.Default
    @Column(name = "update_date")
    private Timestamp updateDate = new Timestamp(System.currentTimeMillis());

}
