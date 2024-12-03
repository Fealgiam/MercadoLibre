package com.mercadolibre.coupon.infrastructure.outputpoint.jpa.repository;

import com.mercadolibre.coupon.infrastructure.model.outputpoint.entity.RedeemedProductDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedeemedProductRepository extends JpaRepository<RedeemedProductDAO, Long> {

    Page<RedeemedProductDAO> findAllByOrderByNumberRedeemedDesc(Pageable pageable);

    Page<RedeemedProductDAO> findByIdCountryOrderByNumberRedeemedDesc(String countryId, Pageable pageable);

}
