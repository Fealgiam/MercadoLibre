package com.mercadolibre.coupon.infrastructure.outputpoint.jpa.repository;

import com.mercadolibre.coupon.infrastructure.model.outputpoint.entity.RedeemedProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RedeemedProductRepository extends JpaRepository<RedeemedProduct, Long> {

}
