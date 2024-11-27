package com.mercadolibre.coupon.configuration;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.mercadolibre.coupon.infrastructure.outputpoint.rest.client")
public class FeignClientSupportConfiguration { }
