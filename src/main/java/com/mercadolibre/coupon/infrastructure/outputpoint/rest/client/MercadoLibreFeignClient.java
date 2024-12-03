package com.mercadolibre.coupon.infrastructure.outputpoint.rest.client;

import com.mercadolibre.coupon.infrastructure.model.outputpoint.rest.CountryRs;
import com.mercadolibre.coupon.infrastructure.model.outputpoint.rest.ProductRs;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@FeignClient(name = "mercadoLibreFeignClient", url = "${services.mercado-libre.url}")
public interface MercadoLibreFeignClient {

    // Country
    @GetMapping(value = "${services.mercado-libre.paths.fetch-country}")
    CountryRs getSite(@PathVariable("id") String siteId);

    @GetMapping(value = "${services.mercado-libre.paths.fetch-countries}")
    Set<CountryRs> getSites();

    // Product
    @GetMapping(value = "${services.mercado-libre.paths.fetch-product}")
    ProductRs getItem(@PathVariable("id") String itemId);

    @GetMapping(value = "${services.mercado-libre.paths.fetch-products}")
    Set<ProductRs> getItems(@RequestParam("ids") String itemIds);

}
